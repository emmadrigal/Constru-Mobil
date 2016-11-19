using System;
using System.Data;
using System.Data.SqlClient;
using System.Collections.Generic;
using System.Xml;
using EPATEC.Models;

namespace DatabaseConnection
{
    /// <summary>
    /// The class responsible to the connection with the database
    /// </summary>
    public class Connection
    {
        private static Connection instance;
        private SqlConnection myConnection;
        private ErrorHandler.ErrorHandler err;
        string connectionString;

        /// <summary>
        /// Allow to access to a static instance of this class
        /// </summary>
        public static Connection Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new Connection();
                }
                return instance;
            }
        }


        /// <summary>
        /// Constructor, it initialices the interface with the DB, it creates the SqlConnection in charge of the connection
        /// </summary>
        private Connection()
        {
            //Error handler in order to store errors
            err = new ErrorHandler.ErrorHandler();
            //Calls the data from the config file
            XmlDocument doc = new XmlDocument();
            //Path to the current place where the file is being executed

            //string path = System.Windows.Forms.Application.StartupPath;
            //doc.Load(path + "\\config.xml");

            //Calls each of the attributes
            /*string server = doc["DataBase"]["server"].InnerText;
            string DBname = doc["DataBase"]["database"].InnerText;
            string username = doc["DataBase"]["username"].InnerText;
            string password = doc["DataBase"]["password"].InnerText;
            //Builds the connection string based on the received attributes
            connectionString = "Persist Security Info=False;" +
                                       "User ID=" + username +
                                       ";PWD=" + password +
                                       ";Initial Catalog=" + DBname +
                                       ";Data Source=" + server; */

            connectionString = "Persist Security Info=False;" +
                                       "User ID=" + "TOM" +
                                       ";PWD=" + "password" +
                                       ";Initial Catalog=" + "EPATEC" +
                                       ";Data Source=" + "GHOST\\SQLSERVER";

            //connectionString = "Data Source=.;Initial Catalog=EPATEC;Integrated Security=True";
        }//End of the constructor

        //########## METHODS OF THE USER TABLE ##################

        /// <summary>
        /// Method insert an user into the databse
        /// </summary>
        /// <param name="usuario">The user who is going to be inserted</param>
        public void crear_Usuario(Usuario usuario)
        {
            //Creates parameters to be used
            SqlParameter[] myparm = new SqlParameter[6];
            myparm[0] = new SqlParameter("@Cedula", usuario.Id_usuario);
            myparm[1] = new SqlParameter("@Nombre", usuario.Nombre);
            myparm[2] = new SqlParameter("@Apellidos", usuario.Apellido);
            myparm[3] = new SqlParameter("@Residencia", usuario.Residencia);
            myparm[4] = new SqlParameter("@FechaNacimiento", SqlDbType.DateTime);
            myparm[5] = new SqlParameter("@Telefono", usuario.Telefono);
            //adds birth in the correct format
            myparm[4].Value = usuario.Fecha_de_Nacimiento;
            string command = "INSERT INTO USUARIO (Cedula, Nombre, Apellido, Grado_de_Penalizacion, Lugar_de_Residencia, Fecha_de_Nacimiento, Telefono) VALUES (@Cedula, @Nombre, @Apellidos, 0, @Residencia, @FechaNacimiento, @Telefono); ";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of the method


        /// <summary>
        /// Method recover the user whose id is cedula
        /// </summary>
        /// <param name="cedula">The id of the user wanted</param>
        /// <returns></returns>
        public Usuario get_Usuario(long cedula)
        {
            SqlParameter myparm = new SqlParameter("@Cedula", cedula);
            //Query to be made
            string command = "SELECT * FROM USUARIO WHERE Cedula = @Cedula;";
            using (myConnection = new SqlConnection(connectionString))
            {
                try
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            //If at least one object was found
                            if (reader.Read())
                            {
                                //Creates and returns the correct object
                                Usuario usuario = new Usuario();
                                usuario.Id_usuario = (long)reader["Cedula"];
                                usuario.Nombre = (string)reader["Nombre"];
                                usuario.Apellido = (string)reader["Apellido"];
                                usuario.Penalizacion = Int16.Parse(reader["Grado_de_Penalizacion"].ToString());
                                usuario.Residencia = (string)reader["Lugar_de_Residencia"];
                                usuario.Fecha_de_Nacimiento = ((DateTime)reader["Fecha_de_Nacimiento"]).ToString("MM-dd-yyyy");
                                usuario.Telefono = (int)reader["Telefono"];
                                return usuario;
                            }//End of if
                            else
                            {
                                return null;
                            }//End of else
                        }//End of using
                    }//End of using
                }catch(Exception e)
                {
                    System.Diagnostics.Debug.WriteLine("Error en get_Usuario: " + e.Message);
                    return null;
                }
            }//End of using
        }//End of method


        /// <summary>
        /// Get all the user of the dtabase
        /// </summary>
        /// <returns>list of user</returns>
        public List<Usuario> get_AllUsuario()
        {
            //Query to be executed
            string command = "SELECT * FROM USUARIO;";
            using (myConnection = new SqlConnection(connectionString))
            {
                try
                {
                    myConnection.Open();
                    System.Diagnostics.Debug.WriteLine("Connection successful");
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                //Creates and returns the correct object
                                List<Usuario> Users = new List<Usuario>();
                                while (reader.Read())
                                {
                                    //Adds al the found objects
                                    Usuario user = new Usuario();
                                    user.Id_usuario = (long)reader["Cedula"];
                                    user.Nombre = (string)reader["Nombre"];
                                    user.Apellido = (string)reader["Apellido"];
                                    user.Penalizacion = Int16.Parse(reader["Grado_de_Penalizacion"].ToString());
                                    user.Residencia = (string)reader["Lugar_de_Residencia"];
                                    user.Fecha_de_Nacimiento = ((DateTime)reader["Fecha_de_Nacimiento"]).ToString("MM-dd-yyyy");
                                    user.Telefono = (int)reader["Telefono"];
                                    Users.Add(user);
                                }
                                return Users;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }
                catch(Exception e)
                {
                    System.Diagnostics.Debug.WriteLine("Error en get_AllUsuario: " + e.Message);
                    return null;
                }
            }//End of using
        }//End of the method


        /// <summary>
        /// Delete an user of the database 
        /// </summary>
        /// <param name="cedula">id</param>
        public void eliminar_Usuario(long cedula)
        {
            string command = "DELETE FROM USUARIO WHERE Cedula = @Cedula;";
            SqlParameter myparm2 = new SqlParameter("@Cedula", cedula);
            ExecuteCommandWriteOneParam(command, myparm2);
        }//End of the method

        /// <summary>
        /// Update the name of a user whose id is cedula
        /// </summary>
        /// <param name="cedula">id of the user</param>
        /// <param name="nombre">new name of the user</param>
        public void update_Usuario_Nombre(long cedula, string nombre)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Nombre", nombre);
            myparm[1] = new SqlParameter("@Cedula", cedula);
            string command = "UPDATE USUARIO SET Nombre = @Nombre WHERE Cedula = @Cedula;";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the last name of a user
        /// </summary>
        /// <param name="cedula">id of the user</param>
        /// <param name="Apellido">new last name</param>
        public void update_Usuario_Apellido(long cedula, string Apellido)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Apellido", Apellido);
            myparm[1] = new SqlParameter("@Cedula", cedula);
            string command = "UPDATE USUARIO SET Apellido = @Apellido WHERE Cedula = @Cedula;";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the penalty degree of a userclient
        /// </summary>
        /// <param name="cedula">id of the user</param>
        /// <param name="penalizacion">value of the penalty</param>
        public void update_Usuario_Penalizacion(long cedula, int penalizacion)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Cedula", cedula);
            myparm[1] = new SqlParameter("@Grado_de_Penalizacion", penalizacion);
            string command = "UPDATE USUARIO SET Grado_de_Penalizacion = @Grado_de_Penalizacion WHERE Cedula = @Cedula;";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of the mthod

        /// <summary>
        /// Update the residence of a user
        /// </summary>
        /// <param name="cedula">id of the user</param>
        /// <param name="Residencia">new value</param>
        public void update_Usuario_Residencia(long cedula, string Residencia)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Residencia", Residencia);
            myparm[1] = new SqlParameter("@Cedula", cedula);
            string command = "UPDATE USUARIO SET Lugar_de_Residencia = @Residencia WHERE Cedula = @Cedula;";
            ExecuteCommandWrite(command, myparm);
        }//End of the mthod

        /// <summary>
        /// Update the born date of a user
        /// </summary>
        /// <param name="cedula"></param>
        /// <param name="Nacimiento"></param>
        public void update_Usuario_Nacimiento(long cedula, string Nacimiento)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Nacimiento", Nacimiento);
            myparm[1] = new SqlParameter("@Cedula", cedula);
            string command = "UPDATE USUARIO SET Fecha_de_Nacimiento = @Nacimiento WHERE Cedula = @Cedula;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the telephone number of a user
        /// </summary>
        /// <param name="cedula">id of the user</param>
        /// <param name="Telefono">new telephone number</param>
        public void update_Usuario_Telefono(long cedula, int Telefono)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Telefono", Telefono);
            myparm[1] = new SqlParameter("@Cedula", cedula);
            string command = "UPDATE USUARIO SET Telefono = @Telefono WHERE Cedula = @Cedula;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Recover all the user who are employees
        /// </summary>
        /// <returns>A list of the employees</returns>
        internal List<Usuario> get_AllEmployees()
        {
            //Query to be executed
            string command = "getUsers 'Empleado'";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();

                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                //Creates and returns the correct object
                                List<Usuario> Empleados = new List<Usuario>();
                                while (reader.Read())
                                {
                                    //Adds al the found objects
                                    Usuario empleado = new Usuario();
                                    empleado.Id_usuario = (long)reader["Cedula"];
                                    empleado.Nombre = (string)reader["Nombre"];
                                    empleado.Apellido = (string)reader["Apellido"];
                                    empleado.Penalizacion = Int16.Parse(reader["Grado_de_Penalizacion"].ToString());
                                    empleado.Residencia = (string)reader["Lugar_de_Residencia"];
                                    empleado.Fecha_de_Nacimiento = ((DateTime)reader["Fecha_de_Nacimiento"]).ToString("MM-dd-yyyy");
                                    empleado.Telefono = (int)reader["Telefono"];
                                    Empleados.Add(empleado);
                                }
                                return Empleados;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllEmployees: " + e.Message);
                return null;
            }
        }//End of the method

        /// <summary>
        /// Recover all the user who are providers
        /// </summary>
        /// <returns>list of provider</returns>
        internal List<Usuario> get_AllProviders()
        {
            //Query to be executed
            string command = "getUsers 'Proveedor'";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Usuario proveedor;
                            if (reader.HasRows)
                            {
                                //Creates and returns the correct object
                                List<Usuario> Proveedores = new List<Usuario>();
                                while (reader.Read())
                                {
                                    //Adds al the found objects
                                    //Adds al the found objects
                                    proveedor = new Usuario();
                                    proveedor.Id_usuario = (long)reader["Cedula"];
                                    proveedor.Nombre = (string)reader["Nombre"];
                                    proveedor.Apellido = (string)reader["Apellido"];
                                    proveedor.Penalizacion = Int16.Parse(reader["Grado_de_Penalizacion"].ToString());
                                    proveedor.Residencia = (string)reader["Lugar_de_Residencia"];
                                    proveedor.Fecha_de_Nacimiento = ((DateTime)reader["Fecha_de_Nacimiento"]).ToString("MM-dd-yyyy");
                                    proveedor.Telefono = (int)reader["Telefono"];
                                    Proveedores.Add(proveedor);
                                }
                                return Proveedores;
                            }
                            else
                            {
                                return null;
                            }
                        }
                    }
                }
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllProviders: " + e.Message);
                return null;
            }
        }//End of the method

        /// <summary>
        /// Recover all the user who are clients
        /// </summary>
        /// <returns>list of provider</returns>
        internal List<Usuario> get_AllClientes()
        {
            //Query to be executed
            string command = "getUsers 'Cliente'";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Usuario proveedor;
                            if (reader.HasRows)
                            {
                                //Creates and returns the correct object
                                List<Usuario> Proveedores = new List<Usuario>();
                                while (reader.Read())
                                {
                                    //Adds al the found objects
                                    //Adds al the found objects
                                    proveedor = new Usuario();
                                    proveedor.Id_usuario = (long)reader["Cedula"];
                                    proveedor.Nombre = (string)reader["Nombre"];
                                    proveedor.Apellido = (string)reader["Apellido"];
                                    proveedor.Penalizacion = Int16.Parse(reader["Grado_de_Penalizacion"].ToString());
                                    proveedor.Residencia = (string)reader["Lugar_de_Residencia"];
                                    proveedor.Fecha_de_Nacimiento = ((DateTime)reader["Fecha_de_Nacimiento"]).ToString("MM-dd-yyyy");
                                    proveedor.Telefono = (int)reader["Telefono"];
                                    Proveedores.Add(proveedor);
                                }
                                return Proveedores;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllClientes: " + e.Message);
                return null;
            }
        }//End of the method



        //############# OTHER METHODS OF QUERYS #######################

        /// <summary>
        /// Method recover the top 10 products which have been more sold
        /// </summary>
        /// <returns>list of products</returns>
        internal List<ProductosVentas> get_TopProductos()
        {
            string command = "SELECT TOP(10) PRODUCTO.Nombre_Producto, PRODUCTO.Id_Sucursal, COUNT(CONTIENE.Nombre_Producto) AS 'Cuenta' FROM (PRODUCTO LEFT JOIN CONTIENE ON CONTIENE.Nombre_Producto = PRODUCTO.Nombre_Producto)  GROUP BY PRODUCTO.Nombre_Producto, PRODUCTO.Id_Sucursal";

            command = "SELECT PRODUCTO.Nombre_Producto, SUM(CONTIENE.Cantidad) AS Total"
                       + " FROM(PRODUCTO JOIN CONTIENE ON CONTIENE.Nombre_Producto = PRODUCTO.Nombre_Producto)"
                       + "GROUP BY  PRODUCTO.Nombre_Producto "
                       + "ORDER BY Total desc";
            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();

                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        ProductosVentas producto;
                        if (reader.HasRows)
                        {
                            List<ProductosVentas> Productos = new List<ProductosVentas>();
                            while (reader.Read())
                            {
                                producto = new ProductosVentas();
                                producto.Nombre = (string)reader["Nombre_Producto"];
                                //producto.Sucursal = (long)reader["Id_Sucursal"];
                                producto.Cantidad = (int)reader["Total"];
                                Productos.Add(producto);
                            }
                            return Productos;
                        }
                        else
                        {
                            return null;
                        }
                    }//End of using
                }//End of using
            }//End of using
        }//End of the method

        
        /// <summary>
        /// Get the sales by sucursal
        /// </summary>
        /// <param name="sucursal">name of the branch</param>
        /// <returns>sales</returns>
        internal List<SucursalVentas> get_VentasSucursal()
        {
            string command = "SELECT SUCURSAL.Id_Sucursal, COUNT(PRODUCTO.Nombre_Producto) As Cuenta FROM SUCURSAL LEFT JOIN PRODUCTO ON PRODUCTO.Id_Sucursal = SUCURSAL.Id_Sucursal LEFT JOIN CONTIENE ON CONTIENE.Nombre_Producto = PRODUCTO.Nombre_Producto GROUP BY SUCURSAL.Id_Sucursal";

            command = "SELECT  S.Id_Sucursal, SUM(C.Cantidad) AS Cuenta "
            + " FROM SUCURSAL AS S LEFT JOIN PEDIDO AS P ON S.Id_Sucursal = P.Id_Sucursal JOIN CONTIENE AS C ON P.Id_Pedido = C.Id_pedido"
            + " GROUP BY S.Id_Sucursal";

            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();

                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        SucursalVentas Sucursal;
                        if (reader.HasRows)
                        {
                            List<SucursalVentas> Sucursales = new List<SucursalVentas>();
                            while (reader.Read())
                            {
                                Sucursal = new SucursalVentas();
                                Sucursal.Sucursal = (long)reader["Id_Sucursal"];
                                Sucursal.CantVentas = (int)reader["Cuenta"];
                                Sucursales.Add(Sucursal);
                            }
                            return Sucursales;
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            }
        }//end of method 

        /// <summary>
        /// Recover the products that have been more sold in a branch
        /// </summary>
        /// <param name="sucursal">name of the branch</param>
        /// <returns>products</returns>
        internal List<ProductosVentas> get_TopProductosSuc(long sucursal)
        {
            string command = "SELECT TOP(10) PRODUCTO.Nombre_Producto, PRODUCTO.Id_Sucursal, COUNT(CONTIENE.Nombre_Producto) AS 'Cuenta' FROM (PRODUCTO LEFT JOIN CONTIENE ON CONTIENE.Nombre_Producto = PRODUCTO.Nombre_Producto) WHERE PRODUCTO.Id_Sucursal = @id  GROUP BY PRODUCTO.Nombre_Producto, PRODUCTO.Id_Sucursal;";
            command = "SELECT TOP(5) PRODUCTO.Nombre_Producto, SUM(CONTIENE.Cantidad) AS Total "
                    + " FROM(PRODUCTO JOIN CONTIENE ON CONTIENE.Nombre_Producto = PRODUCTO.Nombre_Producto)"
                    + " WHERE CONTIENE.Id_Pedido IN (SELECT Id_Pedido FROM PEDIDO WHERE PEDIDO.Id_Sucursal = @id) "
                    + " GROUP BY  PRODUCTO.Nombre_Producto"
                    + " ORDER BY Total desc";
            SqlParameter param = new SqlParameter("@id", sucursal);

            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();

                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    comando.Parameters.Add(param);
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        ProductosVentas producto;
                        if (reader.HasRows)
                        {
                            List<ProductosVentas> Productos = new List<ProductosVentas>();
                            while (reader.Read())
                            {
                                producto = new ProductosVentas();
                                producto.Nombre = (string)reader["Nombre_Producto"];
                                //producto.Sucursal = (long)reader["Id_Sucursal"];
                                producto.Cantidad = (int)reader["Total"];
                                Productos.Add(producto);
                            }
                            return Productos;
                        }
                        else
                        {
                            return null;
                        }
                    }//End of using
                }//End of using
            }//End of using
        }//End of the mthod

       


        //############# METHODS FOR CATEGORIA TABLE ######################
        
        /// <summary>
        /// Add a category to the database
        /// </summary>
        /// <param name="categoria">new category</param>
        public void crear_Categoria(Categoria categoria)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Descripcion", categoria.Descripcion);
            myparm[1] = new SqlParameter("@Nombre", categoria.Nombre);
            string command = "INSERT INTO Categoria (Nombre, Descripcion) VALUES (@Nombre, @Descripcion); ";
            ExecuteCommandWrite(command, myparm);
        }//End of the mthod


        /// <summary>
        /// Update the desciption of a category
        /// </summary>
        /// <param name="nombre">name of the category</param>
        /// <param name="Descripcion">new value</param>
        public void update_Categoria_Descripcion(string nombre, string Descripcion)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Descripcion", Descripcion);
            myparm[1] = new SqlParameter("@nombre", nombre);
            string command = "UPDATE CATEGORIA SET Descripcion = @Descripcion WHERE nombre = @nombre;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method


        /// <summary>
        /// Recover a category of the database
        /// </summary>
        /// <param name="nombre">name</param>
        /// <returns>category</returns>
        public Categoria get_Categoria(string nombre)
        {
            SqlParameter myparm = new SqlParameter("@Nombre", nombre);
            try
            {
                string command = "SELECT * FROM CATEGORIA WHERE Nombre = @Nombre;";
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();

                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Categoria categoria = new Categoria();
                                categoria.Nombre = (string)reader["Nombre"];
                                categoria.Descripcion = (string)reader["Descripcion"];
                                return categoria;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            } catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en getCategoria: " + e.Message.ToString());
                return null;
            }//End of the catch
        }//End of the method


        /// <summary>
        /// Recover all the categories from the database
        /// </summary>
        /// <returns>Return a list of categories</returns>
        internal List<Categoria> get_AllCategoria()
        {
            //Creates and returns the correct object
            string command = "SELECT * FROM CATEGORIA;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Categoria categoria;
                            if (reader.HasRows)
                            {
                                List<Categoria> Categorias = new List<Categoria>();
                                while (reader.Read())
                                {
                                    categoria = new Categoria();
                                    categoria.Nombre = (string)reader["Nombre"];
                                    categoria.Descripcion = (string)reader["Descripcion"];
                                    Categorias.Add(categoria);
                                }
                                return Categorias;
                            }
                            else
                            {
                                return null;
                            }
                        }//end of using
                    }//end of using
                }//end of using
            } catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllCategoria: " + e.Message);
                return null;
            }//End of the catch
        }//End of the method


        /// <summary>
        /// Delete the category whose name is Nombre if there arent any product
        /// who belongs to that category
        /// </summary>
        /// <param name="Nombre"></param>
        public void eliminar_Categoria(string Nombre)
        {
            SqlParameter nombre = new SqlParameter("@Nombre", Nombre);
            int cuenta = 0;
            string command = "SELECT COUNT(*) AS Cuenta FROM PRODUCTO WHERE PRODUCTO.Nombre_Categoría = @Nombre;";
            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();

                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    comando.Parameters.Add(nombre);
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        if (reader.Read())
                            cuenta = (int)reader["Cuenta"];
                        else
                            cuenta = 0;
                    }//End of using
                }//End of using
            }//End of using
            if (cuenta == 0)
            {
                SqlParameter nombre2 = new SqlParameter("@Nombre", Nombre);
                string command2 = "DELETE FROM CATEGORIA WHERE Nombre = @Nombre;";
                ExecuteCommandWriteOneParam(command2, nombre2);
            }
        }//End of the method



        //################ METHODS OF SUCURSAL TABLE ############################
        
        /// <summary>
        /// Insert a branch in the DB
        /// </summary>
        /// <param name="id_Sucursal">new branch</param>
        public void crear_Sucursal(Sucursal sucursal)
        {
            SqlParameter id = new SqlParameter("@id", sucursal.id);
            string command = "INSERT INTO SUCURSAL (id_Sucursal) VALUES (@id); ";
            ExecuteCommandWriteOneParam(command, id);
        }//End of the method

        /// <summary>
        /// Delete a branch of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_Sucursal(long id_Sucursal)
        {
            SqlParameter id = new SqlParameter("@id", id_Sucursal);
            string command = "DELETE FROM SUCURSAL WHERE id_Sucursal = @id;";
            ExecuteCommandWriteOneParam(command, id);
        }//End of the method
        
        /// <summary>
        /// Get a branch of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>sucursal</returns>
        public Sucursal get_Sucursal(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM SUCURSAL WHERE Id_Sucursal = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Sucursal sucursal = new Sucursal();
                                sucursal.id = (long)reader["Id_Sucursal"];
                                return sucursal;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Sucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method
       
        /// <summary>
        /// Recover all the branches of the database
        /// </summary>
        /// <returns>list of branch</returns>
        public List<Sucursal> get_AllSucursal()
        {
            string command = "SELECT * FROM SUCURSAL;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<Sucursal> sucursales = new List<Sucursal>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    Sucursal sucursal = new Sucursal();
                                    sucursal.id = (long)reader["Id_Sucursal"];
                                    sucursales.Add(sucursal);
                                }//End of the while
                                return sucursales;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllSucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

       

        //################ METHODS OF ROL TABLE ############################

        /// <summary>
        /// Insert a role in the DB
        /// </summary>
        /// <param name="rol">new role</param>
        public void crear_Rol(Rol rol)
        {
            SqlParameter id = new SqlParameter("@nombre", rol.nombre);
            string command = "INSERT INTO ROL VALUES (@nombre); ";
            ExecuteCommandWriteOneParam(command, id);
        }//End of the method

        /// <summary>
        /// Delete a role of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_Rol(long id)
        {
            SqlParameter param = new SqlParameter("@id", id);
            string command = "DELETE FROM ROL WHERE Id_Rol = @id;";
            ExecuteCommandWriteOneParam(command, param);
        }//End of the method

        /// <summary>
        /// Get a role of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>sucursal</returns>
        public Rol get_Rol(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM ROL WHERE Id_Rol = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Rol rol = new Rol();
                                rol.Id_rol = (long)reader["Id_Rol"];
                                rol.nombre = (string)reader["Nombre"];
                                return rol;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Rol: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Recover all the roles of the database
        /// </summary>
        /// <returns>list of roles</returns>
        public List<Rol> get_AllRol()
        {
            string command = "SELECT * FROM ROL;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<Rol> sucursales = new List<Rol>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    Rol rol = new Rol();
                                    rol.Id_rol = (long)reader["Id_Rol"];
                                    rol.nombre = (string)reader["Nombre"];
                                    sucursales.Add(rol);
                                }//End of the while
                                return sucursales;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllRol: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Update the name of the role
        /// </summary>
        /// <param name="id">id of the role</param>
        /// <param name="newValue">new value</param>
        public void update_Rol_Nombre(long id, string newValue)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Nombre", newValue);
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE ROL SET Nombre = @Nombre WHERE Id_Rol = @id;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method



        //################ METHODS OF CONTIENE TABLE ############################

        /// <summary>
        /// Insert a Contain object in the DB
        /// </summary>
        /// <param name="rol">new contain</param>
        public void crear_Contiene(Contiene contiene)
        {
            SqlParameter[] myparm = new SqlParameter[3];
            //Add the parameter
            myparm[0] = new SqlParameter("@nombre_producto", contiene.Nombre_Producto);
            myparm[1] = new SqlParameter("@id_pedido", contiene.Id_pedido);
            myparm[2] = new SqlParameter("@cantidad", contiene.cantidad);
            string command = "INSERT INTO CONTIENE VALUES (@nombre_producto, @id_pedido, @cantidad);";
            ExecuteCommandWrite(command, myparm);
        }//End of the method


        /// <summary>
        /// Insert a Contain object in the DB
        /// </summary>
        /// <param name="rol">new contain</param>
        public void crear_Contiene(long userID, string time, string producto, string cantidad)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@id", userID);
            myparm[1] = new SqlParameter("@time", time);
            Pedido pedido = new Pedido();

            long id_Pedido = -1;
            string command = "SELECT * FROM PEDIDO WHERE Cedula_Cliente = @id AND Hora_de_Creación = @time;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.AddRange(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                id_Pedido = (long)reader["Id_Pedido"];
                            }
                        }//end using
                    }//end using
                }//end using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Pedido: " + e.Message);
            }//End of the catch


            SqlParameter[] parmContiene = new SqlParameter[3];
            //Add the parameter
            parmContiene[0] = new SqlParameter("@nombre_producto", producto);
            parmContiene[1] = new SqlParameter("@id_pedido", id_Pedido);
            parmContiene[2] = new SqlParameter("@cantidad", cantidad);
            string command2 = "INSERT INTO CONTIENE VALUES (@nombre_producto, @id_pedido, @cantidad);";
            ExecuteCommandWrite(command2, parmContiene);
        }//End of the method

        /// <summary>
        /// Delete a contain object of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_Contiene(long id)
        {
            SqlParameter param = new SqlParameter("@id", id);
            string command = "DELETE FROM CONTIENE WHERE Id_Contiene = @id;";
            ExecuteCommandWriteOneParam(command, param);
        }//End of the method

        /// <summary>
        /// Get a contain object of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>Contain object</returns>
        public Contiene get_Contiene(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM CONTIENE WHERE Id_Contiene = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Contiene contiene = new Contiene();
                                contiene.Id_contiene = (long)reader["Id_Contiene"];
                                contiene.Nombre_Producto = (string)reader["Nombre_Producto"];
                                contiene.Id_pedido = (long)reader["Id_Pedido"];
                                contiene.cantidad = (int)reader["Cantidad"];
                                return contiene;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Contiene: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Recover all the contais object of the database
        /// </summary>
        /// <returns>list of contains</returns>
        public List<Contiene> get_AllContiene()
        {
            string command = "SELECT * FROM CONTIENE;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<Contiene> lista = new List<Contiene>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    Contiene contiene = new Contiene();
                                    contiene.Id_contiene = (long)reader["Id_Contiene"];
                                    contiene.Nombre_Producto = (string)reader["Nombre_Producto"];
                                    contiene.Id_pedido = (long)reader["Id_Pedido"];
                                    contiene.cantidad = (int)reader["Cantidad"];
                                    lista.Add(contiene);
                                }//End of the while
                                return lista;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllContiene: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Update the amount of the contain
        /// </summary>
        /// <param name="id">id of the role</param>
        /// <param name="newValue">new value</param>
        public void update_Pedido_Telefono(long userID, string time, string newValue)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@cantidad", Int32.Parse(newValue));
            myparm[1] = new SqlParameter("@userID", userID);
            myparm[2] = new SqlParameter("@time", time);
            string command = "UPDATE PEDIDO SET Telefono_Preferido = @telefono WHERE Cedula_Cliente = @userID AND Hora_de_Creación = @time;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the amount of the contain
        /// </summary>
        /// <param name="id">id of the role</param>
        /// <param name="newValue">new value</param>
        public void update_Contiene_Cantidad(long userID, string time, string producto, string newValue)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@cantidad", Int32.Parse(newValue));
            myparm[1] = new SqlParameter("@userID", userID);
            myparm[2] = new SqlParameter("@time", time);
            myparm[3] = new SqlParameter("@producto", producto);
            string command = "UPDATE C SET Cantidad = @cantidad  FROM (CONTIENE C JOIN PEDIDO P ON C.Id_Pedido = P.Id_Pedido) WHERE P.Cedula_Cliente = @userID AND P.Hora_de_Creación = @time AND C.Nombre_Producto = @producto;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the amount of the contain
        /// </summary>
        /// <param name="id">id of the role</param>
        /// <param name="newValue">new value</param>
        public void update_Contiene_Cantidad(long id, string newValue)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@cantidad", Int32.Parse(newValue));
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE CONTIENE SET Cantidad = @cantidad WHERE Id_Contiene = @id;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method



        //################ METHODS OF EmpleadoSucursal TABLE ############################

        /// <summary>
        /// Insert an EmpleadoSucursal object in the DB
        /// </summary>
        /// <param name="emp_suc">new object</param>
        public void crear_EmpleadoSucursal(EmpleadoSucursal emp_suc)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            //Add the parameter
            myparm[0] = new SqlParameter("@id_empleado", emp_suc.Id_Empleado);
            myparm[1] = new SqlParameter("@id_sucursal", emp_suc.Id_Sucursal);
            string command = "INSERT INTO EmpleadoSucursal VALUES (@id_empleado, @id_sucursal);";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Delete a EmpleadoSucursal object of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_EmpleadoSucursal(long id)
        {
            SqlParameter param = new SqlParameter("@id", id);
            string command = "DELETE FROM EmpleadoSucursal WHERE Id_EmpleadoSucursal = @id;";
            ExecuteCommandWriteOneParam(command, param);
        }//End of the method

        /// <summary>
        /// Get a EmpleadoSucursal object of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>EmpleadoSucursala object</returns>
        public EmpleadoSucursal get_EmpleadoSucursal(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM EmpleadoSucursal WHERE Id_EmpleadoSucursal = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                EmpleadoSucursal emp_suc = new EmpleadoSucursal();
                                emp_suc.Id_EmpleadoSucursal = (long)reader["Id_EmpleadoSucursal"];
                                emp_suc.Id_Empleado = (long)reader["Id_Empleado"];
                                emp_suc.Id_Sucursal = (long)reader["Id_Sucursal"];
                                return emp_suc;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_EmpleadoSucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Recover all the EmpleadoSucursal of the database
        /// </summary>
        /// <returns>list of EmpleadoSucursal</returns>
        public List<EmpleadoSucursal> get_AllEmpleadoSucursal()
        {
            string command = "SELECT * FROM EmpleadoSucursal;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<EmpleadoSucursal> sucursales = new List<EmpleadoSucursal>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    EmpleadoSucursal emp_suc = new EmpleadoSucursal();
                                    emp_suc.Id_EmpleadoSucursal = (long)reader["Id_EmpleadoSucursal"];
                                    emp_suc.Id_Empleado = (long)reader["Id_Empleado"];
                                    emp_suc.Id_Sucursal = (long)reader["Id_Sucursal"];
                                    sucursales.Add(emp_suc);
                                }//End of the while
                                return sucursales;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllEmpleadoSucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method




        //################ METHODS OF Producto_Sucursal TABLE ############################

        /// <summary>
        /// Insert an Producto_Sucursal object in the DB
        /// </summary>
        /// <param name="prod_suc">new object</param>
        public void crear_Producto_Sucursal(Producto_Sucursal prod_suc)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            //Add the parameter
            myparm[0] = new SqlParameter("@nombre_producto", prod_suc.Nombre_Producto);
            myparm[1] = new SqlParameter("@id_sucursal", prod_suc.Id_Sucursal);
            string command = "INSERT INTO Producto_Sucursal VALUES (@nombre_producto, @id_sucursal);";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Delete a Producto_Sucursal object of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_Producto_Sucursal(long id)
        {
            SqlParameter param = new SqlParameter("@id", id);
            string command = "DELETE FROM Producto_Sucursal WHERE Id_Pp = @id;";
            ExecuteCommandWriteOneParam(command, param);
        }//End of the method

        /// <summary>
        /// Get a Producto_Sucursal object of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>Producto_Sucursal object</returns>
        public Producto_Sucursal get_Producto_Sucursal(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM Producto_Sucursal WHERE Id_Pp = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Producto_Sucursal pro_suc = new Producto_Sucursal();
                                pro_suc.Id_Pp = (long)reader["Id_Pp"];
                                pro_suc.Nombre_Producto = (string)reader["Nombre_Producto"];
                                pro_suc.Id_Sucursal = (long)reader["Id_Sucursal"];
                                return pro_suc;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Producto_Sucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Recover all the Producto_Sucursal of the database
        /// </summary>
        /// <returns>list of Producto_Sucursal</returns>
        public List<Producto_Sucursal> get_AllProducto_Sucursal()
        {
            string command = "SELECT * FROM Producto_Sucursal;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<Producto_Sucursal> sucursales = new List<Producto_Sucursal>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    Producto_Sucursal pro_suc = new Producto_Sucursal();
                                    pro_suc.Id_Pp = (long)reader["Id_Pp"];
                                    pro_suc.Nombre_Producto = (string)reader["Nombre_Producto"];
                                    pro_suc.Id_Sucursal = (long)reader["Id_Sucursal"];
                                    sucursales.Add(pro_suc);
                                }//End of the while
                                return sucursales;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllProducto_Sucursal: " + e.Message);
                return null;
            }//End of catch
        }//End of the method



        //################ METHODS OF Rol_Usuario TABLE ############################

        /// <summary>
        /// Insert an Producto_Sucursal object in the DB
        /// </summary>
        /// <param name="rol_usuario">new object</param>
        public void crear_Rol_Usuario(Rol_Usuario rol_usuario)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            //Add the parameter
            myparm[0] = new SqlParameter("@usuario", rol_usuario.Id_usuario);
            myparm[1] = new SqlParameter("@rol", rol_usuario.Id_rol);
            string command = "INSERT INTO Rol_Usuario VALUES (@usuario, @rol);";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Delete a Rol_Usuario object of the database based on his id
        /// </summary>
        /// <param name="id">id</param>
        public void eliminar_Rol_Usuario(long id)
        {
            SqlParameter param = new SqlParameter("@id", id);
            string command = "DELETE FROM Rol_Usuario WHERE Id_Rol_Usuario = @id;";
            ExecuteCommandWriteOneParam(command, param);
        }//End of the method

        /// <summary>
        /// Get a Rol_Usuario object of the database whose id is specified
        /// by the parameter
        /// </summary>
        /// <param name="id">id</param>
        /// <returns>Rol_Usuario object</returns>
        public Rol_Usuario get_Rol_Usuario(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            string command = "SELECT * FROM Rol_Usuario WHERE Id_Rol_Usuario = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Rol_Usuario rol_usuario = new Rol_Usuario();
                                rol_usuario.Id_rol_usuario = (long)reader["Id_Rol_Usuario"];
                                rol_usuario.Id_usuario = (long)reader["usuario"];
                                rol_usuario.Id_rol = (long)reader["rol"];
                                return rol_usuario;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Rol_Usuario: " + e.Message);
                return null;
            }//End of catch
        }//End of the method

        /// <summary>
        /// Recover all the Rol_Usuario of the database
        /// </summary>
        /// <returns>list of Rol_Usuario</returns>
        public List<Rol_Usuario> get_AllRol_Usuario()
        {
            string command = "SELECT * FROM Rol_Usuario;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            List<Rol_Usuario> lista = new List<Rol_Usuario>();
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    Rol_Usuario rol_usuario = new Rol_Usuario();
                                    rol_usuario.Id_rol_usuario = (long)reader["Id_Rol_Usuario"];
                                    rol_usuario.Id_usuario = (long)reader["usuario"];
                                    rol_usuario.Id_rol = (long)reader["rol"];
                                    lista.Add(rol_usuario);
                                }//End of the while
                                return lista;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllRol_Usuario: " + e.Message);
                return null;
            }//End of catch
        }//End of the method


        //############### METHODS OF PRODUCT #############################
        /// <summary>
        /// Create a Product and insert it into the DB
        /// </summary>
        /// <param name="producto">Product</param>
        public void crear_Producto(Producto producto)
        {
            SqlParameter[] myparm = new SqlParameter[8];
            //Add the parameter
            myparm[0] = new SqlParameter("@Nombre", producto.nombre);
            myparm[1] = new SqlParameter("@Sucursal", producto.id_Sucursal);
            myparm[2] = new SqlParameter("@Proovedor", producto.Cedula_Provedor);
            myparm[3] = new SqlParameter("@Categoria", producto.categoria);
            myparm[4] = new SqlParameter("@Descripcion", producto.Descripcion);
            myparm[5] = new SqlParameter("@Exento", producto.Exento);
            myparm[6] = new SqlParameter("@Cantidad", producto.Cantidad_Disponible);
            myparm[7] = new SqlParameter("@Precio", producto.Precio);
            string command = "INSERT INTO PRODUCTO VALUES (@Nombre, @Sucursal, @Proovedor, @Categoria, @Descripcion, @Exento, @Cantidad, @Precio); ";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of method

        /// <summary>
        /// Get a product whose name is nombre
        /// </summary>
        /// <param name="nombre">name </param>
        /// <returns>product</returns>
        public List<Producto> get_Producto(string nombre)
        {
            SqlParameter myparm = new SqlParameter("@Nombre", nombre);
            string command = "SELECT * FROM PRODUCTO WHERE Nombre_Producto = @Nombre;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                Producto producto = new Producto();
                                producto.nombre = (string)reader["Nombre_Producto"];
                                producto.id_Sucursal = (long)reader["Id_Sucursal"];
                                producto.Cedula_Provedor = (long)reader["Cedula_Provedor"];
                                producto.categoria = (string)reader["Nombre_Categoría"];
                                producto.Descripcion = (string)reader["Descripción"];
                                producto.Exento = ((bool)reader["Exento"]);
                                producto.Cantidad_Disponible = (int)reader["Cantidad_Disponible"];
                                producto.Precio = (int)reader["Precio"];
                                List<Producto> output = new List<Producto>();
                                output.Add(producto);
                                return output;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Producto: " + e.Message);
                return null;
            }//End of the catch
        }//End of the method

        /// <summary>
        /// Get all the products of the DB
        /// </summary>
        /// <returns>list of products</returns>
        public List<Producto> get_AllProducto()
        {
            string command = "SELECT * FROM PRODUCTO";
            try { 
            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();
                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        Producto producto;
                        if (reader.HasRows)
                        {
                            List<Producto> Productos = new List<Producto>();
                            while (reader.Read())
                            {
                                producto = new Producto();
                                    producto.nombre = (string)reader["Nombre_Producto"];
                                    producto.id_Sucursal = (long)reader["Id_Sucursal"];
                                    producto.Cedula_Provedor = (long)reader["Cedula_Provedor"];
                                    producto.categoria = (string)reader["Nombre_Categoría"];
                                    producto.Descripcion = (string)reader["Descripción"];
                                    producto.Exento = ((bool)reader["Exento"]);
                                    producto.Cantidad_Disponible = (int)reader["Cantidad_Disponible"];
                                    producto.Precio = (int)reader["Precio"];
                                    Productos.Add(producto);
                            }
                            return Productos;
                        }
                        else
                        {
                            return null;
                        }
                    }//End of using
                }//End of using
            }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_AllProducto: " + e.Message);
                return null;
            }//End of the catch
        }//End of the mthod

        /// <summary>
        /// Update the name of a product
        /// </summary>
        /// <param name="id">name of the product</param>
        /// <param name="Nombre">new name</param>
        public void update_Producto_Nombre(string id, string Nombre)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Nombre", Nombre);
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE PRODUCTO SET Nombre_Producto = @Nombre WHERE Nombre_Producto = @id;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the description of a product
        /// </summary>
        /// <param name="id">name of the product</param>
        /// <param name="Descripcion">new description</param>
        public void update_Producto_Descripcion(string id, string Descripcion)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Descripcion", Descripcion);
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE PRODUCTO SET Descripción = @Descripcion WHERE Nombre_Producto = @id;";
            //Executes the command
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the amount available of a product
        /// </summary>
        /// <param name="id">name of the product</param>
        /// <param name="Cantidad">new amount available</param>
        public void update_Producto_Cantidad(string id, int Cantidad)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@Cantidad", Cantidad);
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE PRODUCTO SET Cantidad_Disponible = @Cantidad WHERE Nombre_Producto = @id;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Update the price of a product
        /// </summary>
        /// <param name="id">name of the product</param>
        /// <param name="Cantidad">new amount available</param>
        public void update_Producto_Precio(string id, int precio)
        {
            SqlParameter[] myparm = new SqlParameter[2];
            myparm[0] = new SqlParameter("@precio", precio);
            myparm[1] = new SqlParameter("@id", id);
            string command = "UPDATE PRODUCTO SET Precio = @precio WHERE Nombre_Producto = @id;";
            ExecuteCommandWrite(command, myparm);
        }//End of the method

        /// <summary>
        /// Delete a product of the DB based on his name
        /// </summary>
        /// <param name="Nombre"></param>
        public void eliminar_Producto(string Nombre)
        {
            SqlParameter nombre = new SqlParameter("@Nombre", Nombre);
            SqlParameter nombre2 = new SqlParameter("@Nombre", Nombre);
            string command2 = "DELETE FROM CONTIENE WHERE Nombre_Producto = @Nombre;";
            ExecuteCommandWriteOneParam(command2, nombre);
            string command = "DELETE FROM PRODUCTO WHERE Nombre_Producto = @Nombre;";
            ExecuteCommandWriteOneParam(command, nombre2);
        }//End of the method

        /// <summary>
        /// Get the products who provide a provider
        /// </summary>
        /// <param name="proveedor">id of the provider</param>
        /// <returns>list of products</returns>
        public List<Producto> get_Producto_By_Proveedor(long proveedor)
        {
            SqlParameter myparm = new SqlParameter("@id", proveedor);
            string command = "SELECT * FROM PRODUCTO WHERE Cedula_Provedor = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Producto producto;
                            if (reader.HasRows)
                            {
                                List<Producto> Productos = new List<Producto>();
                                while (reader.Read())
                                {
                                    producto = new Producto();
                                    producto.nombre = (string)reader["Nombre_Producto"];
                                    producto.id_Sucursal = (long)reader["Id_Sucursal"];
                                    producto.Cedula_Provedor = (long)reader["Cedula_Provedor"];
                                    producto.categoria = (string)reader["Nombre_Categoría"];
                                    producto.Descripcion = (string)reader["Descripción"];
                                    producto.Exento = ((bool)reader["Exento"]);
                                    producto.Cantidad_Disponible = (int)reader["Cantidad_Disponible"];
                                    producto.Precio = (int)reader["Precio"];
                                    Productos.Add(producto);
                                }
                                return Productos;
                            }
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Producto_By_Proveedor: " + e.Message);
                return null;
            }//End of the catch
        }//End of the methos

        /// <summary>
        /// Get the products who belong to a category
        /// </summary>
        /// <param name="categoria"></param>
        /// <returns>list of products</returns>
        internal List<Producto> get_Producto_By_Categoria(string categoria)
        {
            string command = "SELECT * FROM PRODUCTO WHERE PRODUCTO.Nombre_Categoría = @nombre;";
            SqlParameter param = new SqlParameter("@nombre", categoria);
            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();
                using (SqlCommand comando = new SqlCommand(command, myConnection))
                {
                    comando.Parameters.Add(param);
                    using (SqlDataReader reader = comando.ExecuteReader())
                    {
                        Producto producto;
                        if (reader.HasRows)
                        {
                            List<Producto> Productos = new List<Producto>();
                            while (reader.Read())
                            {
                                producto = new Producto();
                                producto.nombre = (string)reader["Nombre_Producto"];
                                producto.id_Sucursal = (long)reader["Id_Sucursal"];
                                producto.Cedula_Provedor = (long)reader["Cedula_Provedor"];
                                producto.categoria = (string)reader["Nombre_Categoría"];
                                producto.Descripcion = (string)reader["Descripción"];
                                producto.Exento = ((bool)reader["Exento"]);
                                producto.Cantidad_Disponible = (int)reader["Cantidad_Disponible"];
                                producto.Precio = (int)reader["Precio"];
                                Productos.Add(producto);
                            }
                            return Productos;
                        }
                        else
                        {
                            return null;
                        }
                    }//End of using
                }//End of using
            }//End of using
        }//End of the method


        //################### METHODS OF PEDIDO ##############################
        /// <summary>
        /// Insert a order into the database
        /// </summary>
        /// <param name="pedido">order to enter</param>
        public void crear_Pedido(Pedido pedido)
        {
            string horaActual = DateTime.Now.ToString();
            //Crea el nuevo pedidoi
            SqlParameter[] myparm = new SqlParameter[4];
            myparm[0] = new SqlParameter("@Cedula", pedido.Cedula_Cliente);
            myparm[1] = new SqlParameter("@Sucursal", pedido.id_Sucursal);
            myparm[2] = new SqlParameter("@Telefono", pedido.Telefono);
            myparm[3] = new SqlParameter("@Hora", horaActual);
            string command = "INSERT INTO PEDIDO (Cedula_Cliente, Id_Sucursal, Telefono_Preferido, Hora_de_Creación) VALUES (@Cedula, @Sucursal, @Telefono, @Hora); ";
            ExecuteCommandWrite(command, myparm);
            using (myConnection = new SqlConnection(connectionString))
            {
                myConnection.Open();
                try
                {
                    //Obtiene el id creado por la base de datos
                    SqlParameter[] myparm2 = new SqlParameter[2];
                    myparm2[0] = new SqlParameter("@Cedula", pedido.Cedula_Cliente);
                    myparm2[1] = new SqlParameter("@Hora", horaActual);

                    string comandoid = "SELECT PEDIDO.Id_Pedido FROM PEDIDO WHERE Cedula_Cliente = @Cedula AND Hora_de_Creación = @Hora;";
                    SqlCommand commandID = new SqlCommand(comandoid, myConnection);
                    commandID.Parameters.AddRange(myparm2);
                    long id = 0;
                    using (SqlDataReader reader = commandID.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            id = (long)reader["Id_Pedido"];
                        }//end of if
                    }//end of using

                    //Insert each one of the relations between Pedido and Producto 
                    string command_Productos = "INSERT INTO CONTIENE (Nombre_Producto, Id_Pedido, Cantidad) VALUES (@Producto, @Pedido, @Cantidad); ";
                    SqlCommand comando = new SqlCommand(command_Productos, myConnection);
                    comando.Parameters.Add("@Pedido", SqlDbType.BigInt);
                    comando.Parameters.Add("@Producto", SqlDbType.NVarChar);
                    comando.Parameters.Add("@Cantidad", SqlDbType.Int);

                    foreach (ProductoPedido producto in pedido.productos)
                    {
                        try
                        {
                            comando.Parameters["@Producto"].Value = producto.nombre;
                            comando.Parameters["@Cantidad"].Value = producto.Quantity;
                            comando.Parameters["@Pedido"].Value = id;
                            comando.ExecuteNonQuery();
                        }
                        catch (Exception ex)
                        {
                            err.ErrorMessage = ex.Message.ToString();
                        }
                    }//End of for
                }//End of try
                catch (Exception ex)
                {
                    err.ErrorMessage = ex.Message.ToString();
                }
            }//End of using
        }//End of the method

        /// <summary>
        /// Get a order with the id specified by the parameter 
        /// </summary>
        /// <param name="id">id of the order</param>
        /// <returns>the order required</returns>
        public Pedido get_Pedido(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            Pedido pedido = new Pedido();
            string command = "SELECT * FROM PEDIDO WHERE Id_Pedido = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                pedido.id_Pedido = (long)reader["Id_Pedido"];
                                pedido.Cedula_Cliente = (long)reader["Cedula_Cliente"];
                                pedido.id_Sucursal = (long)reader["Id_Sucursal"];
                                pedido.Telefono = (int)reader["Telefono_Preferido"];
                                pedido.Hora = ((DateTime)reader["Hora_de_Creación"]).ToString();
                            }
                            else
                            {
                                return null;
                            }
                        }//end using
                    }//end using
                }//end using
                command = "SELECT PRODUCTO.Nombre_Producto, PRODUCTO.Descripción FROM PRODUCTO JOIN CONTIENE ON PRODUCTO.Nombre_Producto = CONTIENE.Nombre_Producto JOIN PEDIDO ON CONTIENE.Id_Pedido = PEDIDO.Id_Pedido WHERE PEDIDO.Id_Pedido = @id;";
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        SqlParameter myparm2 = new SqlParameter("@id", id);
                        comando.Parameters.Add(myparm2);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                pedido.productos = new List<ProductoPedido>();
                                if (reader.Read())
                                {
                                    ProductoPedido producto = new ProductoPedido();
                                    producto.nombre = (string)reader["Nombre_Producto"];
                                    producto.Quantity = (int)reader["Cantidad"];
                                    pedido.productos.Add(producto);
                                }
                            }//End of if
                        }//End of Using
                    }//End of Using
                }//End of Using
                return pedido;
            }catch(Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Pedido: " + e.Message);
                return null;
            }//End of the catch
}//End of method

        /// <summary>
        /// Delete a Order of the database based on the client who made the order and the time
        /// </summary>
        /// <param name="id">id of the client</param>
        public void eliminar_Pedido(long id)
        {
            SqlParameter myparm = new SqlParameter("@id", id);
            //Elimina todas las solicitudes de producto relacionadas con este producto
            string command_Productos = "DELETE FROM CONTIENE WHERE Id_Pedido = @id;";
            ExecuteCommandWriteOneParam(command_Productos, myparm);
            //Elimina el Pedido indicado
            string command = "DELETE FROM PEDIDO WHERE PEDIDO.Id_Pedido = @id;";
            SqlParameter myparm2 = new SqlParameter("@id", id);
            ExecuteCommandWriteOneParam(command, myparm2);
        }//End of the method
		
		/// <summary>
        /// Get the order of a client
        /// </summary>
        /// <param name="cedula">id of the client</param>
        /// <returns>the list of order</returns>
        public List<Pedido> get_AllPedido()
        {
            string command = "SELECT * FROM PEDIDO;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Pedido pedido;
                            if (reader.HasRows)
                            {
                                List<Pedido> PedidosCliente = new List<Pedido>();
                                while (reader.Read())
                                {
                                    pedido = new Pedido();
                                    long id = (long)reader["Id_Pedido"];
                                    pedido.id_Pedido = id;
                                    pedido.Cedula_Cliente = (long)reader["Cedula_Cliente"];
                                    pedido.id_Sucursal = (long)reader["Id_Sucursal"];
                                    pedido.Telefono = (int)reader["Telefono_Preferido"];
                                    pedido.Hora = ((DateTime)reader["Hora_de_Creación"]).ToString();

                                    pedido.productos = new List<ProductoPedido>();
                                    using (SqlConnection myConnection2 = new SqlConnection(connectionString))
                                    {
                                        myConnection2.Open();
                                        string command2 = "SELECT * FROM PRODUCTO JOIN CONTIENE ON PRODUCTO.Nombre_Producto = CONTIENE.Nombre_Producto WHERE CONTIENE.Id_Pedido = @id";
                                        using (SqlCommand comando2 = new SqlCommand(command2, myConnection2))
                                        {
                                            comando2.Parameters.AddWithValue("@id", id);
                                            using (SqlDataReader reader2 = comando2.ExecuteReader())
                                            {
                                                ProductoPedido producto;
                                                if (reader2.HasRows)
                                                {
                                                    while (reader2.Read())
                                                    {
                                                        producto = new ProductoPedido();
                                                        producto.nombre = (string)reader2["Nombre_Producto"];
                                                        producto.Quantity = (int)reader2["Cantidad"];
                                                        pedido.productos.Add(producto);
                                                    }//End of while
                                                }//End of if
                                            }//End of using
                                        }//End of using
                                    }//End of using
                                    PedidosCliente.Add(pedido);
                                }//End of while
                                return PedidosCliente;
                            }//End of if
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            } catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_PedidoCliente: " + e.Message);
                return null;
            }//End of the catch
        }//End of the mthod

        /// <summary>
        /// Get the order of a client
        /// </summary>
        /// <param name="cedula">id of the client</param>
        /// <returns>the list of order</returns>
        public List<Pedido> get_Pedido_By_Cliente(long cedula)
        {
            string command = "SELECT * FROM PEDIDO WHERE Cedula_Cliente = @cedula;";
            SqlParameter myparm = new SqlParameter("@cedula", cedula);
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Pedido pedido;
                            if (reader.HasRows)
                            {
                                List<Pedido> PedidosCliente = new List<Pedido>();
                                while (reader.Read())
                                {
                                    pedido = new Pedido();
                                    long id = (long)reader["Id_Pedido"];
                                    pedido.id_Pedido = id;
                                    pedido.Cedula_Cliente = (long)reader["Cedula_Cliente"];
                                    pedido.id_Sucursal = (long)reader["Id_Sucursal"];
                                    pedido.Telefono = (int)reader["Telefono_Preferido"];
                                    pedido.Hora = ((DateTime)reader["Hora_de_Creación"]).ToString();

                                    pedido.productos = new List<ProductoPedido>();
                                    using (SqlConnection myConnection2 = new SqlConnection(connectionString))
                                    {
                                        myConnection2.Open();
                                        string command2 = "SELECT * FROM PRODUCTO JOIN CONTIENE ON PRODUCTO.Nombre_Producto = CONTIENE.Nombre_Producto WHERE CONTIENE.Id_Pedido = @id";
                                        using (SqlCommand comando2 = new SqlCommand(command2, myConnection2))
                                        {
                                            comando2.Parameters.AddWithValue("@id", id);
                                            using (SqlDataReader reader2 = comando2.ExecuteReader())
                                            {
                                                ProductoPedido producto;
                                                if (reader2.HasRows)
                                                {
                                                    while (reader2.Read())
                                                    {
                                                        producto = new ProductoPedido();
                                                        producto.nombre = (string)reader2["Nombre_Producto"];
                                                        producto.Quantity = (int)reader2["Cantidad"];
                                                        pedido.productos.Add(producto);
                                                    }//End of while
                                                }//End of if
                                            }//End of using
                                        }//End of using
                                    }//End of using
                                    PedidosCliente.Add(pedido);
                                }//End of while
                                return PedidosCliente;
                            }//End of if
                            else
                            {
                                return null;
                            }
                        }//End of using
                    }//End of using
                }//End of using
            } catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_PedidoCliente: " + e.Message);
                return null;
            }//End of the catch
        }//End of the mthod

        /// <summary>
        /// Get all the orders that belongs to a sucursal
        /// </summary>
        /// <param name="sucursal">name of the susursal</param>
        /// <returns>list of orders</returns>
        internal List<Pedido> get_Pedido_By_Sucursal(long sucursal)
        {
            string command = "SELECT * FROM PEDIDO WHERE PEDIDO.Id_Sucursal = @id;";
            SqlParameter param = new SqlParameter("@id", sucursal);
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    myConnection.Open();

                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(param);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Pedido pedido;
                            if (reader.HasRows)
                            {
                                List<Pedido> pedidos = new List<Pedido>();
                                while (reader.Read())
                                {
                                    pedido = new Pedido();
                                    pedido.id_Pedido = (long)reader["Id_Pedido"];
                                    pedido.Telefono = (int)reader["Telefono_Preferido"];
                                    pedido.Hora = ((DateTime)reader["Hora_de_Creación"]).ToString();
                                    pedido.id_Sucursal = (long)reader["Id_Sucursal"];
                                    pedidos.Add(pedido);
                                }
                                return pedidos;
                            }
                            else
                            {
                                return null;
                            }
                        }// end using
                    }// end using
                }// end using
            } catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_Pedido_By_Sucursal: " + e.Message);
                return null;
            }//End of the catch
        }//End of the method

        /// <summary>
        /// Get all the orders of a office
        /// </summary>
        /// <param name="id_Empleado"></param>
        /// <returns>list of orders</returns>
        public List<Pedido> get_PedidoSucursal(long id_Empleado)
        {
            SqlParameter myparm = new SqlParameter("@id", id_Empleado);
            string command = "SELECT PEDIDO.Id_Pedido, PEDIDO.Cedula_Cliente, PEDIDO.Telefono_Preferido, PEDIDO.Hora_de_Creación FROM PEDIDO JOIN SUCURSAL ON PEDIDO.Id_Sucursal = SUCURSAL.Id_Sucursal JOIN USUARIO ON SUCURSAL.ID_Sucursal = USUARIO.Cedula WHERE USUARIO.Cedula = @id;";
            try
            {
                using (myConnection = new SqlConnection(connectionString))
                {
                    //Open the connection with the DB
                    myConnection.Open();
                    using (SqlCommand comando = new SqlCommand(command, myConnection))
                    {
                        comando.Parameters.Add(myparm);
                        using (SqlDataReader reader = comando.ExecuteReader())
                        {
                            Pedido pedido;
                            if (reader.Read())
                            {
                                List<Pedido> Productos = new List<Pedido>();
                                while (reader.Read())
                                {
                                    pedido = new Pedido();
                                    pedido.id_Pedido = (int)reader["Id_Pedido"];
                                    pedido.Cedula_Cliente = (int)reader["Cedula_Cliente"];
                                    pedido.Telefono = (int)reader["Telefono_Preferido"];
                                    pedido.Hora = ((DateTime)reader["Hora_de_Creación"]).ToString();
                                    Productos.Add(pedido);
                                }
                                return Productos;
                            }
                            else
                            {
                                return null;
                            }//End of the else
                        }//End of using
                    }//End of using
                }//End of using
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error en get_PedidoSucursal: " + e.Message);
                return null;
            }//End of the catch
        }//End of the method

       


        //########### OTHER METHODS ##########################

        /// <summary>
        /// This method executes a command 
        /// </summary>
        /// <param name="command">command that is going to be executed</param>
        /// <param name="param">the parameters of the query</param>
        public void ExecuteCommandWrite(string command, SqlParameter[] parms)
        {
            using (myConnection = new SqlConnection(connectionString))
            {
                try
                {
                    myConnection.Open();
                    SqlCommand comando = new SqlCommand(command, myConnection);
                    comando.Parameters.AddRange(parms);
                    comando.ExecuteNonQuery();
                }
                catch (Exception ex)
                {
                    err.ErrorMessage = ex.Message.ToString();
                }//End of catch
            }//End of using
        }//End of the method

        /// <summary>
        /// This method executes a command 
        /// </summary>
        /// <param name="command">command that is going to be executed</param>
        /// <param name="param">the parameters of the query</param>
        public void ExecuteCommandWriteOneParam(string command, SqlParameter param)
        {
            using (myConnection = new SqlConnection(connectionString))
            {
                try
                {
                    myConnection.Open();
                    SqlCommand comando = new SqlCommand(command, myConnection);
                    //Add the parameter
                    comando.Parameters.Add(param);
                    comando.ExecuteNonQuery();
                }
                catch (Exception ex)
                {
                    err.ErrorMessage = ex.Message.ToString();
                    System.Diagnostics.Debug.WriteLine("Error with the connection: " + ex.Message.ToString());
                }
            }//end of using
        }//end of the mthod

        /// <summary>
        /// Get a exception when any error happen
        /// </summary>
        /// <returns></returns>
        public string GetException()
        {
            return err.ErrorMessage.ToString();
        }//end of the mthod
    }
}//END

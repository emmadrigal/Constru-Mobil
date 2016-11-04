using System.Collections.Generic;

//Contiene las clases para el manejo temporal de los datos en memoria
namespace EPATEC2.Company
{
    public class Cliente
    {
        /// Propiedad de Cedula
        public long Cedula_Cliente{ get; set; }

        /// Propiedad de Nombre
        public string Nombre { get; set; }

        /// Propiedad de Apellidos
        public string Apellidos { get; set; }

        /// Propiedad de Nombre
        public int Penalizacion { get; set; }

        /// Propiedad de Nombre
        public string Residencia { get; set; }

        /// Propiedad de Nombre
        public string Nacimiento { get; set; }

        /// Propiedad de Nombre
        public string Telefono { get; set; }

    }

    public class Sucursal
    {
        /// Propiedad de id
        public int id { get; set; }
    }

    public class Producto
    {
         /// Propiedad de nombre
        public string nombre { get; set; }

        /// Propiedad de nombre
        public long id_Sucursal { get; set; }

        /// Propiedad de nombre
        public long Cedula_Provedor { get; set; }

        public string Descripcion { get; set; }

        public bool Exento { get; set; }

        public int Precio { get; set; }

        public int Cantidad_Disponible { get; set; }

        public string categoria { get; set; }
    }

    public class Proovedor
    {
        /// Propiedad de Cedula
        public long Cedula_Proveedor { get; set; }

        /// Propiedad de Nombre
        public string Nombre { get; set; }

        /// Propiedad de Apellidos
        public string Apellidos { get; set; }

        /// Propiedad de Nombre
        public string Residencia { get; set; }

        /// Propiedad de Nombre
        public string Nacimiento { get; set; }

    }

    public class ProductoPedido
    {
        public string nombre { get; set; }
        public int Quantity { get; set; }
    }


    public class Pedido
    {
        /// Propiedad de Cedula
        public long id_Pedido { get; set; }

        /// Propiedad de Nombre
        public long Cedula_Cliente { get; set; }

        /// Propiedad de Apellidos
        public long id_Sucursal { get; set; }

        /// Propiedad de Nombre
        public string Telefono { get; set; }

        /// Propiedad de Nombre
        public string Hora { get; set; }

        public List<ProductoPedido> productos { get; set; }
    }
   

    public class Empleado
    {
        /// Propiedad de id
        public long id_Empleado { get; set; }

        /// Propiedad de id
        public long id_Sucursal { get; set; }

        /// Propiedad de id
        public string Nombre { get; set; }

        /// Propiedad de id
        public string puesto { get; set; }
    }

    public class Categoria
    {
         /// Propiedad de id
        public string Nombre { get; set; }

        /// Propiedad de id
        public string Descripcion { get; set; }
    }

    public class ProductosVentas
    {
        public string Nombre { get; set; }
        public long Sucursal { get; set; }
        public int Cantidad { get; set; }
    }

    public class SucursalVentas
    {
        public long Sucursal { get; set; }
        public int CantVentas { get; set; }
    }
}

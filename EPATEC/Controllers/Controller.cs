using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC2.Controllers
{
    [RoutePrefix("Cliente")]
    public class ClienteController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getCliente(int id)
        {
            var product = Connection.Instance.get_Cliente(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllClients")]
        [HttpGet]
        public  IHttpActionResult getAllClients()
        {
            var products = Connection.Instance.get_AllClientes();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutCliente(int id, string campo, string newValue)
        {
            if (campo == "nombre"){
                Connection.Instance.update_Nombre_Cliente(id, newValue);
            }
            else if (campo == "apellido"){
                Connection.Instance.update_Apellido_Cliente(id, newValue);
            }
            else if (campo == "penalizacion")
            {
                Connection.Instance.update_Penalizacion_Cliente(id, Byte.Parse(newValue));
            }
            else if (campo == "residencia")
            {
                Connection.Instance.update_Residencia_Cliente(id, newValue);
            }
            else if (campo == "nacimiento")
            {
                Connection.Instance.update_Nacimiento_Cliente(id, newValue);
            }
            else if (campo == "telefono")
            {
                Connection.Instance.update_Telefono_Cliente(id, Int32.Parse(newValue));
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postCliente(Company.Cliente cliente)
        {
            if (ModelState.IsValid && cliente != null)
            {
                Connection.Instance.crear_Cliente(cliente);
                return Ok();
            }
            else
            {
                return NotFound();
            }
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteCliente(int id)
        {
            Connection.Instance.eliminar_Cliente(id);
            return Ok();
        }
    }

    [RoutePrefix("Producto")]
    public class ProductoController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getProducto(string id)
        {
            var product = Connection.Instance.get_Producto(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllProducts")]
        [HttpGet]
        public IHttpActionResult getAllProducto()
        {
            var products = Connection.Instance.get_AllProducts();
            return Ok(products);
        }

        [Route("~/getAllProductsCat{id}")]
        [HttpGet]
        public IHttpActionResult getAllProductoCat(string id)
        {
            var products = Connection.Instance.get_AllProducsCat(id);
            return Ok(products);
        }

        [Route("~/getAllProductosProveedor{id}")]
        [HttpGet]
        public IHttpActionResult getAllProductosProveedor(int id)
        {
            var products = Connection.Instance.get_AllProductsProv(id);
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutProducto(string id, string campo, string newValue)
        {
            if (campo == "nombre")
            {
                Connection.Instance.update_Nombre_Producto(id, newValue);
            }
            else if (campo == "descripcion")
            {
                Connection.Instance.update_Descripcion_Producto(id, newValue);
            }
            else if (campo == "cantidad")
            {
                Connection.Instance.update_Cantidad_Producto(id, Byte.Parse(newValue));
            }
            else if (campo == "precio")
            {
                Connection.Instance.update_Precio_Producto(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Company.Producto producto)
        {
            Connection.Instance.crear_Producto(producto);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteProducto(string id)
        {
            Connection.Instance.eliminar_Producto(id);
            return Ok();
        }
    }

    [RoutePrefix("Categoria")]
    public class CategoriaController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getCategoria(string id)
        {
            var product = Connection.Instance.get_Categoria(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllCategories")]
        [HttpGet]
        public IHttpActionResult getAllCategories()
        {
            var products = Connection.Instance.get_AllCategories();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutCategoria(string id, string campo, string newValue)
        {
            if (campo == "descripcion")
            {
                Connection.Instance.update_Descripcion_Categoria(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Company.Categoria categoria)
        {
            Connection.Instance.crear_Categoria(categoria);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteCategoria(string id)
        {
            Connection.Instance.eliminar_Categoria(id);
            return Ok();
        }
    }

    [RoutePrefix("Empleado")]
    public class EmpleadoController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getEmpleado(int id)
        {
            var product = Connection.Instance.get_Empleado(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllEmployees")]
        [HttpGet]
        public IHttpActionResult getAllEmpleado()
        {
            var products = Connection.Instance.get_AllEmployees();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutEmpleado(int id, string campo, string newValue)
        {
            if (campo == "nombre")
            {
                Connection.Instance.update_Nombre_Empleado(id, newValue);
            }
            else if (campo == "puesto")
            {
                Connection.Instance.update_Puesto_Empleado(id, newValue);
            }
            else if (campo == "sucursal")
            {
                Connection.Instance.update_Sucursal_Empleado(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Company.Empleado empleado)
        {
            Connection.Instance.crear_Empleado(empleado);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteEmpleado(int id)
        {
            Connection.Instance.eliminar_Empleado(id);
            return Ok();
        }
    }

    [RoutePrefix("Proveedor")]
    public class ProveedorController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getProveedor(int id)
        {
            var product = Connection.Instance.get_Proveedor(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllProviders")]
        [HttpGet]
        public IHttpActionResult getAllProveedor()
        {
            var products = Connection.Instance.get_AllProviders();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutEmpleado(int id, string campo, string newValue)
        {
            if (campo == "nombre")
            {
                Connection.Instance.update_Nombre_Proveedor(id, newValue);
            }
            else if (campo == "apellido")
            {
                Connection.Instance.update_Apellidos_Proveedor(id, newValue);
            }
            else if (campo == "residencia")
            {
                Connection.Instance.update_Residencia_Proveedor(id, newValue);
            }
            else if (campo == "nacimiento")
            {
                Connection.Instance.update_Nacimiento_Proveedor(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Company.Proovedor proveedor)
        {
            Connection.Instance.crear_Proveedor(proveedor);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteProveedor(int id)
        {
            Connection.Instance.eliminar_Proveedor(id);
            return Ok();
        }
    }

    [RoutePrefix("Pedido")]
    public class PedidoController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getPedido(int id)
        {
            var product = Connection.Instance.get_Pedido(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllProducts/{id}")]
        [HttpGet]
        public IHttpActionResult getAllPedidos(int id)
        {
            var products = Connection.Instance.get_AllPedidosSuc(id);
            return Ok(products);
        }

        [Route("~/PedidoCliente/{id}")]
        [HttpGet]
        public IHttpActionResult getPedidoCliente(int id)
        {
            var products = Connection.Instance.get_PedidoCliente(id);
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Company.Pedido pedido)
        {
            Connection.Instance.crear_Pedido(pedido);
            return Ok();
        }


        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deletePedido(int id)
        {
            Connection.Instance.eliminar_Pedido(id);
            return Ok();
        }
    }

    public class StatisticsController : ApiController
    {
        [Route("~/getProductosMasVendidos")]
        [HttpGet]
        public IHttpActionResult getProductosMasVendidos()
        {
            var products = Connection.Instance.get_TopProductos();
            return Ok(products);
        }

        [Route("~/getTopProductosSucursal/{id}")]
        [HttpGet]
        public IHttpActionResult getTopProductosSucursal(int id)
        {
            var products = Connection.Instance.get_TopProductosSuc(id);
            return Ok(products);
        }

        [Route("~/getVentasSucursal/{id}")]
        [HttpGet]
        public IHttpActionResult getVentasSucursal(int id)
        {
            var products = Connection.Instance.get_VentasSucursal(id);
            return Ok(products);
        }
    }
}

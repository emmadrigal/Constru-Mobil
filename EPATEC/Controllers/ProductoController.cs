using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
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

        [Route("~/getAllProducto")]
        [HttpGet]
        public IHttpActionResult getAllProducto()
        {
            var products = Connection.Instance.get_AllProducto();
            return Ok(products);
        }

        [Route("~/getAllProductsCat/{id}")]
        [HttpGet]
        public IHttpActionResult getAllProductoCat(string id)
        {
            var products = Connection.Instance.get_Producto_By_Categoria(id);
            return Ok(products);
        }

        [Route("~/getAllProductosProveedor/{id}")]
        [HttpGet]
        public IHttpActionResult getAllProductosProveedor(long id)
        {
            var products = Connection.Instance.get_Producto_By_Proveedor(id);
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutProducto(string id, string campo, string newValue)
        {
            if (campo == "Descripción")
            {
                Connection.Instance.update_Producto_Descripcion(id, newValue);
            }
            else if (campo == "Cantidad_Disponible")
            {
                Connection.Instance.update_Producto_Cantidad(id, Int32.Parse(newValue));
            }
            else if (campo == "Precio")
            {
                Connection.Instance.update_Producto_Precio(id, Int32.Parse(newValue));
            }
            else if (campo == "Nombre")
            {
                Connection.Instance.update_Producto_Nombre(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Models.Producto producto)
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
}

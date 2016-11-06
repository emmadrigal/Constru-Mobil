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
            if (campo == "Descripción" || campo == "Cantidad_Disponible" || campo == "Precio")
            {
                Connection.Instance.update_Producto(id, campo, newValue);
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

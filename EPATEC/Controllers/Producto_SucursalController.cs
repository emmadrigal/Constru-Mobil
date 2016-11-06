using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Producto_Sucursal")]
    public class Producto_SucursalController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getProducto_Sucursal(long id)
        {
            var product = Connection.Instance.get_Producto_Sucursal(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllProducto_Sucursal")]
        [HttpGet]
        public IHttpActionResult getAllProducto_Sucursal()
        {
            var products = Connection.Instance.get_AllProducto_Sucursal();
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto_Sucursal([FromBody]Models.Producto_Sucursal productosucursal)
        {
            Connection.Instance.crear_Producto_Sucursal(productosucursal);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteProducto_Sucursal(long id)
        {
            Connection.Instance.eliminar_Producto_Sucursal(id);
            return Ok();
        }

    }
}

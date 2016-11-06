using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Sucursal")]
    public class SucursalController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getSucursal(int id)
        {
            var product = Connection.Instance.get_Sucursal(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllSucursal")]
        [HttpGet]
        public IHttpActionResult getAllCategories()
        {
            var products = Connection.Instance.get_AllSucursal();
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postProducto([FromBody]Models.Sucursal sucursal)
        {
            Connection.Instance.crear_Sucursal(sucursal);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteSucursal(int id)
        {
            Connection.Instance.eliminar_Sucursal(id);
            return Ok();
        }
    }

}

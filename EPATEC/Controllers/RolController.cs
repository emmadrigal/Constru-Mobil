using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Rol")]
    public class RolController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getRol(long id)
        {
            var product = Connection.Instance.get_Rol(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllRol")]
        [HttpGet]
        public IHttpActionResult getAllRol()
        {
            var products = Connection.Instance.get_AllRol();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutRol(long id, string campo, string newValue)
        {
            if (campo == "nombre ")
            {
                Connection.Instance.update_Rol(id, campo, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postRol([FromBody]Models.Rol rol)
        {
            Connection.Instance.crear_Rol(rol);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteRol(long id)
        {
            Connection.Instance.eliminar_Rol(id);
            return Ok();
        }
    }
}

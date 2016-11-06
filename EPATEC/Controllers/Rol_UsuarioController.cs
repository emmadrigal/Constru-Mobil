using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Rol_Usuario")]
    public class Rol_UsuarioController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getRol_Usuario(long id)
        {
            var product = Connection.Instance.get_Rol_Usuario(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllRol_Usuario")]
        [HttpGet]
        public IHttpActionResult getAllRol()
        {
            var products = Connection.Instance.get_AllRol_Usuario();
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postRol([FromBody]Models.Rol_Usuario rol_usuario)
        {
            Connection.Instance.crear_Rol_Usuario(rol_usuario);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteRol_Usuario(long id)
        {
            Connection.Instance.eliminar_Rol_Usuario(id);
            return Ok();
        }
    }
}

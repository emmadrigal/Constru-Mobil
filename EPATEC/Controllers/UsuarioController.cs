using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Usuario")]
    public class UsuarioController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getUsuario(long id)
        {
            var product = Connection.Instance.get_Usuario(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllUsuario")]
        [HttpGet]
        public IHttpActionResult getAllUsuario()
        {
            var products = Connection.Instance.get_AllUsuario();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutUsuario(long id, string campo, string newValue)
        {
            if (campo == "Grado_de_Penalizacion" || campo == "Lugar_de_Residencia " || campo == "Telefono ")
            {
                Connection.Instance.update_Usuario(id, campo, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postUsuario([FromBody]Models.Usuario usuario)
        {
            Connection.Instance.crear_Usuario(usuario);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteUsuario(long id)
        {
            Connection.Instance.eliminar_Usuario(id);
            return Ok();
        }

    }
}

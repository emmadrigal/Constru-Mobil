using DatabaseConnection;
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

        [Route("~/getAllEmpleados")]
        [HttpGet]
        public IHttpActionResult getAllEmpleados()
        {
            var products = Connection.Instance.get_AllEmployees();
            return Ok(products);
        }

        [Route("~/getAllProveedores")]
        [HttpGet]
        public IHttpActionResult getAllProveedores()
        {
            var products = Connection.Instance.get_AllProviders();
            return Ok(products);
        }

        [Route("~/getAllClientes")]
        [HttpGet]
        public IHttpActionResult getAllClientes()
        {
            var products = Connection.Instance.get_AllClientes();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutUsuario(long id, string campo, string newValue)
        {
           
            if (campo == "Nombre")
            {
                Connection.Instance.update_Usuario_Nombre(id, newValue);
            }
            else if (campo == "Apellido")
            {
                Connection.Instance.update_Usuario_Apellido(id, newValue);
            }
            else if (campo == "Grado_de_Penalizacion")
            {
                Connection.Instance.update_Usuario_Penalizacion(id, Int32.Parse(newValue));
            }
            else if (campo == "Lugar_de_Residencia")
            {
                Connection.Instance.update_Usuario_Residencia(id, newValue);
            }
            else if (campo == "Nacimiento")
            {
                Connection.Instance.update_Usuario_Nacimiento(id, newValue);
            }
            else if (campo == "Telefono")
            {
                Connection.Instance.update_Usuario_Telefono(id, Int32.Parse(newValue));
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

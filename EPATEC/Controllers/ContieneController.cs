using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Contiene")]
    public class ContieneController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getContiene(long id)
        {
            var product = Connection.Instance.get_Contiene(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllContiene")]
        [HttpGet]
        public IHttpActionResult getAllContiene()
        {
            var products = Connection.Instance.get_AllContiene();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutContiene(long id, string campo, string newValue)
        {
            if (campo == "Cantidad")
            {
                Connection.Instance.update_Contiene_Cantidad(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postContiene([FromBody]Models.Contiene contiene)
        {
            Connection.Instance.crear_Contiene(contiene);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteContiene(long id)
        {
            Connection.Instance.eliminar_Contiene(id);
            return Ok();
        }
    }
}

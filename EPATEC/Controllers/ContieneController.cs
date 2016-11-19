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


        [HttpPut]
        [Route("")]
        public IHttpActionResult updateContieneSinID([FromBody]Models.ContieneSinId contiene)
        {
            //Se asume que siempre se hace un update sobre la cantidad
            Connection.Instance.update_Contiene_Cantidad(contiene.userID, contiene.time, contiene.producto, contiene.cantidad);
            return Ok();
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

        /*

        [HttpPost]
        [Route("")]
        public IHttpActionResult postContiene([FromBody]Models.Contiene contiene)
        {
            Connection.Instance.crear_Contiene(contiene);
            return Ok();
        }
        */
		
		[HttpPost]
        [Route("")]
        public IHttpActionResult postContiene([FromBody]Models.ContieneSinId contiene)
        {
            Connection.Instance.delete_Contiene(contiene.userID, contiene.time, contiene.producto);
            Connection.Instance.crear_Contiene(contiene.userID, contiene.time, contiene.producto, contiene.cantidad);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteContiene(long id)
        {
            Connection.Instance.eliminar_Contiene(id);
            return Ok();
        }

        [Route("")]
        [HttpDelete]
        public IHttpActionResult deleteContiene([FromBody]Models.ContieneSinId contiene)
        {
            Connection.Instance.delete_Contiene(contiene.userID, contiene.time, contiene.producto);
            return Ok();
        }
    }
}

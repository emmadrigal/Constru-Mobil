using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("EmpleadoSucursal")]
    public class EmpleadoSucursalController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getEmpleadoSucursal(long id)
        {
            var product = Connection.Instance.get_EmpleadoSucursal(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllEmpleadoSucursal")]
        [HttpGet]
        public IHttpActionResult getAllEmpleadoSucursal()
        {
            var products = Connection.Instance.get_AllEmpleadoSucursal();
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postEmpleadoSucursal([FromBody]Models.EmpleadoSucursal empleadosucursal)
        {
            Connection.Instance.crear_EmpleadoSucursal(empleadosucursal);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteEmpleadoSucursal(long id)
        {
            Connection.Instance.eliminar_EmpleadoSucursal(id);
            return Ok();
        }

    }
}

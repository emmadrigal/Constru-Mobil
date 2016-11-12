using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("SucursalVentas")]
    public class SucursalVentasController : ApiController
    {
        [Route("~/getSucursalVentas")]
        [HttpGet]
        public IHttpActionResult getAllProducto_Sucursal()
        {
            var products = Connection.Instance.get_VentasSucursal();
            return Ok(products);
        }
    }//End of the class
}//End of the namespace
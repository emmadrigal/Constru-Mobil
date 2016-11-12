using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("ProductosVentas")]
    public class ProductosVentasController : ApiController
    {
        [Route("~/getAllProductosVentas")]
        [HttpGet]
        public IHttpActionResult getAllProducto_Sucursal()
        {
            var products = Connection.Instance.get_TopProductos();
            return Ok(products);
        }

        [Route("~/getProductosVentas_By_Sucursal/{id}")]
        [HttpGet]
        public IHttpActionResult getAllProductoCat(long id)
        {
            var products = Connection.Instance.get_TopProductosSuc(id);
            return Ok(products);
        }
    }//End of the class
}//End of namespace
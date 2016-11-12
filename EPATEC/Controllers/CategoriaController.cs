using DatabaseConnection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Categoria")]
    public class CategoriaController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getCategoria(string id)
        {
            var product = Connection.Instance.get_Categoria(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllCategories")]
        [HttpGet]
        public IHttpActionResult getAllCategories()
        {
            var products = Connection.Instance.get_AllCategoria();
            return Ok(products);
        }

        [Route("{id}/{campo}/{newValue}")]
        [HttpPut]
        public IHttpActionResult PutCategoria(string id, string campo, string newValue)
        {
            if (campo == "Descripción")
            {
                Connection.Instance.update_Categoria_Descripcion(id, newValue);
            }
            return Ok();
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postCategoria([FromBody]Models.Categoria categoria)
        {
            Connection.Instance.crear_Categoria(categoria);
            return Ok();
        }

        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deleteCategoria(string id)
        {
            Connection.Instance.eliminar_Categoria(id);
            return Ok();
        }
    }
}

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EPATEC.Controllers
{
    [RoutePrefix("Pedido")]
    public class PedidoController : ApiController
    {
        [Route("{id}")]
        [HttpGet]
        public IHttpActionResult getPedido(long id)
        {
            var product = Connection.Instance.get_Pedido(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [Route("~/getAllPedidoSucursal/{id}")]
        [HttpGet]
        public IHttpActionResult getAllPedidos(long id)
        {
            var products = Connection.Instance.get_AllPedidosSuc(id);
            return Ok(products);
        }

        [Route("~/PedidoCliente/{id}")]
        [HttpGet]
        public IHttpActionResult getPedidoCliente(long id)
        {
            var products = Connection.Instance.get_PedidoCliente(id);
            return Ok(products);
        }

        [HttpPost]
        [Route("")]
        public IHttpActionResult postPedido([FromBody]Models.Pedido pedido)
        {
            Connection.Instance.crear_Pedido(pedido);
            return Ok();
        }


        [Route("{id}")]
        [HttpDelete]
        public IHttpActionResult deletePedido(long id)
        {
            Connection.Instance.eliminar_Pedido(id);
            return Ok();
        }
    }
}
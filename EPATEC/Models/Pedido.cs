using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Pedido
    {
        /// Propiedad de Cedula
        public long id_Pedido { get; set; }

        /// Propiedad de Nombre
        public long Cedula_Cliente { get; set; }

        /// Propiedad de Apellidos
        public long id_Sucursal { get; set; }

        /// Propiedad de Nombre
        public int Telefono { get; set; }

        /// Propiedad de Nombre
        public string Hora { get; set; }

        public List<ProductoPedido> productos { get; set; }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Contiene
    {
        public long Id_contiene { get; set; }
        public string Nombre_Producto { get; set; }
        public long Id_pedido { get; set; }
        public int cantidad { get; set; }
    }
}
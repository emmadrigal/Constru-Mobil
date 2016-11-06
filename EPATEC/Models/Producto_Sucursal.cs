using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Producto_Sucursal
    {
        public long Id_Pp { get; set; }
        public long Id_Sucursal { get; set; }
        public string Nombre_Producto { get; set; }
    }
}
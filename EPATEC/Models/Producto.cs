using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Producto
    {
        /// Propiedad de nombre
        public string nombre { get; set; }

        /// Propiedad de nombre
        public long id_Sucursal { get; set; }

        /// Propiedad de nombre
        public long Cedula_Provedor { get; set; }

        public string Descripcion { get; set; }

        public bool Exento { get; set; }

        public int Precio { get; set; }

        public int Cantidad_Disponible { get; set; }

        public string categoria { get; set; }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Rol_Usuario
    {
        public long Id_rol_usuario { get; set; }
        public long Id_usuario { get; set; }
        public long Id_rol { get; set; }
    }
}
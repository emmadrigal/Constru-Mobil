using System;
using System.Data;
using System.Data.SqlClient;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class Usuario
    {
        public long Id_usuario { get; set; }
        public string Nombre { get; set; }
        public string Apellido { get; set; }
        public int Penalizacion { get; set; }
        public string Residencia { get; set; }
        public string Fecha_de_Nacimiento { get; set; }
        public int Telefono { get; set; }
    }
}
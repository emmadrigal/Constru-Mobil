using System;
using System.Text;

namespace ErrorHandler
{
    //Class responsible for handling error messages
    public class ErrorHandler
    {
        static StringBuilder errMessage = new StringBuilder();

        //Make class immutable
        static ErrorHandler()
        {
        }
        /// <summary>
        /// Prperty - holds exception messages encountered 
        /// at code execution
        /// </summary>
        public string ErrorMessage
        {
            get { return errMessage.ToString(); }
            set
            {
                errMessage.AppendLine(value);
                System.Diagnostics.Debug.Print(value);
                Console.WriteLine(errMessage);
            }
        }
    }
}

package construtec.contru_mobil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import Database.DBHandler;
import models.Usuario;

public class RegisterUser extends AppCompatActivity {
    private EditText id;
    private EditText name;
    private EditText lastName;
    private TextView birth;
    private EditText residence;
    private EditText phone;
    private Spinner spinner;
    private String origin;

    static int dia;
    static int mes;
    static int año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        id        = (EditText) findViewById(R.id.userId);
        name      = (EditText) findViewById(R.id.userName);
        lastName  = (EditText) findViewById(R.id.userLastName);
        birth     = (TextView) findViewById(R.id.birth);
        residence = (EditText) findViewById(R.id.residence);
        phone    = (EditText) findViewById(R.id.phoneNumber);

        spinner = (Spinner)  findViewById(R.id.Role);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Roles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        Intent intent = getIntent();
        origin = intent.getStringExtra("origin");

        if(origin.equals("login")){
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    String imc_met = spinner.getSelectedItem().toString();

                    if(imc_met.equals("Cliente")){
                        phone.setVisibility(View.VISIBLE);
                        phone.setText("");
                    }
                    else{
                        phone.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
            });
            spinner.setVisibility(View.VISIBLE);
        } else if(origin.equals("newUser")){
            spinner.setVisibility(View.GONE);
            phone.setVisibility(View.VISIBLE);
            phone.setText("");
        }
        else if(origin.equals("newSupplier")){
            spinner.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
        }



        Date fecha = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        año = cal.get(Calendar.YEAR);
    }


    public void dateChooser(View view){
        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                selectedmonth = selectedmonth + 1;
                String newDate = Integer.toString(selectedyear) +"-" +   Integer.toString(selectedmonth) +"-" + Integer.toString(selectedday);
                birth.setText(newDate);
            }

        },
                año, mes, dia);
        mDatePicker.show();
    }

    /**
     * Login method, it is called when the correct button is pressed and checks if the user is correct
     * @param view that calls this method
     */
    public void LogIn(View view){
        Usuario usuario=  new Usuario();
        if(!id.getText().toString().isEmpty())
            usuario.Cedula = Integer.parseInt(id.getText().toString());
        else {
            id.setError("Cedula es requerida");
            return;
        }
        usuario.Nombre   = name.getText().toString();
        usuario.Apellido = lastName.getText().toString();
        usuario.Lugar_de_Residencia = residence.getText().toString();
        usuario.Fecha_de_Nacimiento= birth.getText().toString();

        if(!phone.getText().toString().isEmpty())
            usuario.Telefono = Integer.parseInt(phone.getText().toString());
        else
            usuario.Telefono = 0;

        DBHandler db = DBHandler.getSingletonInstance(this);
        db.addUsuario(usuario);
        if(spinner.getSelectedItem().toString().equals("Cliente")){
            db.addRol(usuario.Cedula, "Cliente");
        }
        else{
            db.addRol(usuario.Cedula, "Proveedor");
        }
        //TODO check if user id already exists

        if(origin.equals("login")){
            Intent intent = new Intent(this, MainScreen.class);
            intent.putExtra("id", usuario.Cedula);
            finish();
            startActivity(intent);
        } else{
            finish();
        }
    }
}

package com.example.emmanuel.construmobil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterUser extends AppCompatActivity {
    private EditText phone;
    private Spinner spinner;
    private String origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        spinner = (Spinner)  findViewById(R.id.Role);
        phone    = (EditText) findViewById(R.id.phoneNumber);

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



        //TODO set birth as a date dialog
    }

    /**
     * Login method, it is called when the correct button is pressed and checks if the user is correct
     * @param view that calls this method
     */
    public void LogIn(View view){
        //TODO send data to database for storage
        //TODO check if user id already exists

        if(origin.equals("login")){
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);

        } else{
            finish();
        }
    }
}

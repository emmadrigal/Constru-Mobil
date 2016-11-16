package com.example.emmanuel.construmobil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import Database.DBHandler;
import models.Usuario;

public class LoginActivity extends AppCompatActivity {
    // UI references
    private EditText mUserName;
    private EditText mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.userName);

        mUserId = (EditText) findViewById(R.id.userId);
    }

    /**
     * Login method, it is called when the correct button is pressed and checks if the user is correct
     * @param view that calls this method
     */
    public void LogIn(View view){
        String userName = mUserName.getText().toString();
        String userId = mUserId.getText().toString();

        if(!userId.equals("")){
            if(userExists(userName, Integer.parseInt(userId))){
                Intent intent = new Intent(this, MainScreen.class);
                intent.putExtra("id", Long.parseLong(userId));
                startActivity(intent);
            }else{
                mUserName.setError("User Name and Id don't match");
            }
        }
    }

    /**
     * Sign up method, it calls the correspoding window and for the activity
     * @param view that calls this function
     */
    public void SignUp(View view){
        Intent intent = new Intent(this, RegisterUser.class);
        intent.putExtra("origin", "login");
        startActivity(intent);
    }


    /**
     * Methods that checks if a user exists
     * @param name of the user that wishes to login
     * @param userId of the user that wishes to login
     * @return boolean indicating if the user exists on the Database
     */
    private boolean userExists(String name, int userId){
        DBHandler db = DBHandler.getSingletonInstance(this);

        Usuario usuario=  db.getUsuario(userId);

        return usuario.Nombre.equals(name);

    }
}

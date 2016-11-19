package epatec.construmovil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        //Inicializa la base de datos
        DBHandler.getSingletonInstance(this);
        //Inicia el ciclo de conección
        startSync();
    }

    public void startSync(){
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(true) {
                        if (isNetworkAvailable()){
                            Log.i("web", "hay internet");
                            DBHandler.getSingletonInstance(null).SyncDB();
                        }else{
                            Log.i("web", "NO hay internet");
                        }
                        sleep(60000);
                    }
                } catch (InterruptedException e) {
                    Log.i("Sync Cycle", e.getMessage());
                }
            }
        };
        thread.start();
    }

    /**
     *Check all connectivities whether available or not
     * @return state of current connection to the Internet
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
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
    private boolean userExists(String name, int userId) {
        DBHandler db = DBHandler.getSingletonInstance(this);

        Usuario usuario = db.getUsuario(userId);

        return (usuario != null) && (usuario.Nombre.equals(name));
    }
}

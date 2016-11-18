package prueba.appprueba;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import prueba.appprueba.models.*;

public class MainActivity extends AppCompatActivity {

    private static DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = DBHelper.getSingletonInstance(this);
        DB.clearDatabase("SUCURSAL");
        DB.addSucursal(10);
        DB.addSucursal(20);
        DB.getAllSucursales();

        Log.i("esto_es_un_msj", String.valueOf(isNetworkAvailable()));

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(10000);
                        Log.i("hay_internet", String.valueOf(isNetworkAvailable()));
                        if (isNetworkAvailable()){
                            //DB.SyncDB();
                        }
                    }
                } catch (InterruptedException e) {}
            }
        };

        thread.start();
    }

    // Check all connectivities whether available or not
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
}

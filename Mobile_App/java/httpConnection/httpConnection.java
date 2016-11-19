package httpConnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class httpConnection  {
    private static httpConnection singleton;
    //TODO get this information from user
    public static String serviceIp = "192.168.43.163";
    public static  String port      = "1515";
    private final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    /**
     * Construtor
     */
    private httpConnection(){}

    /**
     * Returns a sigleton instance of this class
     * @return singleton instance of this class
     */
    public static httpConnection getConnection(){
        if(singleton != null){
            return singleton;
        }
        else{
            return singleton = new httpConnection();
        }
    }

    /**
     * Makes an GET request based on an url, returns the WebService Responce
     * @param url  where request is going to be made
     * @return String with the response
     */
    public String sendGet(String url){
        url = "http://" + serviceIp + ":" + port + "/" + url;
        String respuesta = "";
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            respuesta =  response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }



    /**
     * Sends a json to the Web Service in order to create a new entity
     * @param url where the request is going to be made
     * @param json to be sent to the WebService
     */
    public boolean sendPost(String url, String json){
        url = "http://" + serviceIp + ":" + port + "/" + url;
        Log.i("http", url);
        if(json != null){
            Log.i("http", json);
        }
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request  request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            Response response = client.newCall(request).execute();

            Log.d("codigo", Integer.toString(response.code()));
            //Si el codigo es 2xx es exito, 3xx es redirección, 4xx es error de cliente y 5xx es error de servidor
            return (response.code() < 400);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes the value of an user's attribute
     * @param url contains the URL to be sent
     */
    public boolean sendPut(String url){
        url = "http://" + serviceIp + ":" + port + "/" + url;
        Log.i("http", url);
        try {
            RequestBody body = RequestBody.create(null, new byte[0]);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            Response response = client.newCall(request).execute();

            Log.d("codigo", Integer.toString(response.code()));

            //Si el codigo es 2xx es exito, 3xx es redirección, 4xx es error de cliente y 5xx es error de servidor
            return (response.code() < 400);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendDelete(String url){
        url  = "http://" + serviceIp + ":" + port + "/" + url;
        Log.i("http", url);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();

            Log.d("codigo", Integer.toString(response.code()));
            //Si el codigo es 2xx es exito, 3xx es redirección, 4xx es error de cliente y 5xx es error de servidor
            return (response.code() < 400);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

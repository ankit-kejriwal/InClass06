package com.example.inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button button;

 // comment new da ds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] category = {"entertainment","general","health","science","sports","technology"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Category");
                builder.setItems(category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

//    private class GetSimpleAsync extends AsyncTask<String, Void, ArrayList<Article>> {
//        @Override
//        protected ArrayList<Article> doInBackground(String... params) {
//            StringBuilder stringBuilder = new StringBuilder();
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//            ArrayList<Article> result = new ArrayList<Article>();
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        stringBuilder.append(line);
//                    }
////                    String json = stringBuilder.toString();
////                    JSONObject root = new JSONObject(json);
////                    JSONArray persons = root.getJSONArray("persons");
////                    for (int i = 0; i < persons.length(); i++) {
////                        JSONObject personJSON = persons.getJSONObject(i);
////                        Person person = new Person();
////                        person.name = personJSON.getString("name");
////                        person.id = personJSON.getLong(("id"));
////                        person.age = personJSON.getInt("age");
////
////                        JSONObject addressJSON = personJSON.getJSONObject("address");
////
////                        Address address = new Address();
////                        address.line1 = addressJSON.getString("line1");
////                        address.city = addressJSON.getString("city");
////                        address.state = addressJSON.getString("state");
////                        address.zip = addressJSON.getString("zip");
////                        person.address = address;
////                        result.add(person);
////
////                    }
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            catch (JSONException e) {
////                e.printStackTrace();
////            }
//            finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return result;
//        }
//    }

}

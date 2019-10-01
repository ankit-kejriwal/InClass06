package com.example.inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
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
    TextView textViewCategory;
    ImageView imageViewnext;
    ImageView imageViewprevious;
    TextView textViewTitle;
    TextView textViewDate;
    ImageView imageViewURL;
    EditText editTextDesc;
    TextView textViewcount;
    ProgressDialog progressDialog;

    ArrayList<Article> articleList = new ArrayList<>();
    int currentIndex = 0;

 // comment new da ds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDate = findViewById(R.id.textViewDate);
        imageViewURL = findViewById(R.id.imageViewURL);
        editTextDesc = findViewById(R.id.editTextDesc);
        imageViewnext = findViewById(R.id.imageViewNEXT);
        imageViewprevious = findViewById(R.id.imageViewPrev);
        textViewcount = findViewById(R.id.textViewCount);
        editTextDesc.setEnabled(false);

        imageViewprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex--;
                if(currentIndex < 0){
                    currentIndex = articleList.size()-1;
                }
                setDisplay(currentIndex);
            }
        });

        imageViewnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex++;
                if(currentIndex > (articleList.size()-1)) {
                    currentIndex = 0;
                }
                setDisplay(currentIndex);
            }
        });

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] category = {"entertainment","general","health","science","sports","technology"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Category");
                builder.setItems(category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://newsapi.org/v2/top-headlines?country=us&category="+category[which]+"&apiKey=5c72617fea584de7bb9521936989e3c5";
                        new GetSimpleAsync().execute(url);
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

    private class GetSimpleAsync extends AsyncTask<String, Void, ArrayList<Article>> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.setMax(10);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<Article> result = new ArrayList<Article>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String json = stringBuilder.toString();
//                    Log.d("demo",json);
                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");
                    for(int i=0;i<20;i++){
                        JSONObject articleJSON = articles.getJSONObject(i);
                        Article article = new Article();
                        article.title = articleJSON.getString("title");
                        article.publishedAt = articleJSON.getString("publishedAt");
                        article.urlToImage = articleJSON.getString("urlToImage");
                        article.description = articleJSON.getString("description");
                        result.add(article);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            progressDialog.dismiss();
            super.onPostExecute(articles);
            articleList = articles;
            setDisplay(currentIndex);
        }
    }

    private void setDisplay(int index) {
        Log.d("demo",articleList.get(index).toString());
        textViewTitle.setText(articleList.get(index).getTitle());
        textViewDate.setText(articleList.get(index).getPublishedAt());
        editTextDesc.setText(articleList.get(index).getDescription());
        textViewcount.setText(currentIndex+1 +" out of "+ articleList.size());
        Picasso.get().load(articleList.get(index).getUrlToImage()).into(imageViewURL);
    }



}

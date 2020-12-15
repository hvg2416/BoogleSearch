package com.example.booglesearch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.net.URL;


public class booksList extends AppCompatActivity {
    static ListView listView;
    static EditText editText;
    static ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        listView = (ListView) findViewById(R.id.list_view_books_list);
        editText = (EditText) findViewById(R.id.search_text);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    public void search_button_function_1(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            spinner.setVisibility(View.VISIBLE);
            Log.d("DEBUG_MESSAGE: ","VISIBLE");
            //Autohiding Keyboard after Search button is clicked
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            //Clearing the Edit Text
            String requestBaseURL = "https://www.googleapis.com/books/v1/volumes?";
            String entered_text, requestURL = null;
            EditText editText = (EditText) findViewById(R.id.search_text);
            Uri buildURI;
            entered_text = editText.getText().toString();
            //Clearing text entered in the edit text after clicking Search Button
            editText.getText().clear();
            //Log.d("DEBUG_MESSAGE: ",entered_text);
            if (entered_text == null || entered_text == "") {
                Toast.makeText(this, "Enter Search Keyword", Toast.LENGTH_SHORT).show();
            }
            Log.d("DEBUG_MESSAGE: ", "inside onCreate");
            buildURI = Uri.parse(requestBaseURL).buildUpon().appendQueryParameter("q", entered_text).appendQueryParameter("key", "YOUR_API_KEY").build();
            try {
                requestURL = new URL(buildURI.toString()).toString();
                Log.d("FINAL URL: ", requestURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Showing ProgressBar Spinner

            AsyncTaskClass asyncTaskClass = new AsyncTaskClass(this);
            asyncTaskClass.execute(requestURL);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}

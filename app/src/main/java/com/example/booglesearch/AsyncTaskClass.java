package com.example.booglesearch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncTaskClass extends AsyncTask<String,Void, ArrayList<booksListData>> {
    private Context ctx;
    public AsyncTaskClass(Context ct)
    {
        ctx = ct;
    }
    JSONArray items;
    @Override
    protected ArrayList<booksListData> doInBackground(String... strings) {
        //Extracting data from Google Books API using HttpURLConnection and JSON Parsing
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine())!=null)
            {
                builder.append(line+"\n");
            }
            String jsonDataString = builder.toString();
            //Parsing the extracted JSON Data
            ArrayList<booksListData> adapter_data = new ArrayList<booksListData>();
            JSONObject root_data = new JSONObject(jsonDataString);
            items = root_data.getJSONArray("items");
            int c = 0;
            //Log.d("DEBUG_MESSAGE: ","before for loop");
            for(int i = 0; i < items.length(); i++)
            {
                Log.d("DEBUG_MESSAGE: ","entered FOR loop with i = "+Integer.toString(i));
                if((items.getJSONObject(i).getJSONObject("volumeInfo").has("description")) && (items.getJSONObject(i).getJSONObject("volumeInfo").has("imageLinks"))
                    && (items.getJSONObject(i).getJSONObject("volumeInfo").has("authors")) && (items.getJSONObject(i).getJSONObject("accessInfo").has("webReaderLink")))
                {
                    booksListData tmp_data = new booksListData();
                    tmp_data.setTitle(items.getJSONObject(i).getJSONObject("volumeInfo").getString("title"));
                    tmp_data.setAuthor(items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").getString(0));
                    tmp_data.setDescription(items.getJSONObject(i).getJSONObject("volumeInfo").getString("description"));
                    tmp_data.setSmallThumbnail(items.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail"));
                    tmp_data.setWebReaderLink(items.getJSONObject(i).getJSONObject("accessInfo").getString("webReaderLink"));
                    if(tmp_data.getDescription()!=null && tmp_data.getSmallThumbnail()!=null && tmp_data.getTitle()!=null && tmp_data.getAuthor()!=null && tmp_data.getWebReaderLink()!=null)
                    {
                        Log.d("TITLE: ",tmp_data.getTitle());
                        Log.d("Author: ",tmp_data.getAuthor());
                        Log.d("Description: ",tmp_data.getDescription());
                        Log.d("Small Thumbnail: ",tmp_data.getSmallThumbnail());
                        Log.d("DEBUG_MESSAGE: ",Integer.toString(c));
                        adapter_data.add(c++,tmp_data);
                    }
                    else
                    {
                        Log.d("DEBUG_MESSAGE: ","error FOUND");
                    }
                }
            }
            return adapter_data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<booksListData> booksListData) {
        custom_adapter_books_list_view adapter = new custom_adapter_books_list_view(ctx,booksListData);
        if(adapter!=null)
        {
            Log.d("DEBUG_MESSAGE: ","adapter ! = NULL");
            booksList.listView.setAdapter(adapter);
        }
    }
}

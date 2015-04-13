package com.example.morrisson.oop3;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView1);
        new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"); // http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml// http://www.hostelworld.com/travel-features/155859/top-destinations-for-inter-railing
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // can support more than one process at a time
    private class DownloadData extends AsyncTask<String, Void, String> { // (url, process not needed, what we return back- contents)

        String myData;// = "http://www.hostelworld.com/travel-features/155859/top-destinations-for-inter-railing";
        protected String doInBackground(String...urls){
            try{
                myData = downloadData(urls[0]);

            } catch(IOException e){
                return "Unable to download source file";
            }

            return myData;
        }

        protected void onPostExecute(String result){

            if (myData != null) {
                Log.d("OnPostExecute", myData);
                textView.setText(myData);
            }

        }

        private String downloadData(String theUrl) throws IOException{ // if theres an error we want to throw it back to the try block to deal with it
            int BUFFER_SIZE = 50000; // the url ywe want to download from
            InputStream is = null;

            String contents = "";
            try{
                URL url = new URL(theUrl);
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // give up after 10000 milliseconds (10 secs)
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET"); // standard for accessing data
                conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d("DownloadData", "The response returned is:" + response);
                is = conn.getInputStream(); //

                InputStreamReader isr = new InputStreamReader(is);
                int charRead;
                char[] inputBuffer = new char[BUFFER_SIZE];
                try{
                    while((charRead = isr.read(inputBuffer))>0)
                    {
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        contents += readString; // continually add whatever we read onto the contents string
                        inputBuffer = new char[BUFFER_SIZE]; // read 2000 bytes at a time- clear out buffer again
                    }
                    return contents;

                } catch (IOException e){
                    e.printStackTrace(); // prints to screen where it crashed and what method was called
                    return null;

                }


            } finally{
                if(is != null)
                    is.close(); // either way close the file
            }

        }


    }
    }

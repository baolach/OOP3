package com.example.morrisson.oop3;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
//import android.R

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Button btnParse;
    ListView listApps;
    String myData;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnParse = (Button) findViewById(R.id.btnParse);
        listApps = (ListView) findViewById(R.id.listApps);

        btnParse.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                ParseApplications parse = new ParseApplications(myData);
                boolean operationStatus = parse.process();
                if(operationStatus)
                {
                    ArrayList<Application> allApps = parse.getApplications();

                    ArrayAdapter<Application> adapter = new ArrayAdapter<Application>(MainActivity.this, R.layout.list_item, allApps); // error here as you must create layout > res.layout.rightclick.new android xml
                    listApps.setVisibility(listApps.VISIBLE);
                    listApps.setAdapter(adapter);
                }else
                {
                    Log.d("MainActivity","Error parsing file");
                }
            }
        });

        new DownloadData().execute("http://www.hostelworld.com/travel-features/155859/top-destinations-for-inter-railing");//http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml // http://www.hostelworld.com/travel-features/155859/top-destinations-for-inter-railing
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
    private class DownloadData extends AsyncTask<String, Void, String>
    { // (url, process not needed, what we return back- contents)

        String Data;// = "http://www.hostelworld.com/travel-features/155859/top-destinations-for-inter-railing";
        protected String doInBackground(String...urls){
            try{
                Data = downloadData(urls[0]);

            } catch(IOException e){
                return "Unable to download source file";
            }

            return ""; //Data ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        protected void onPostExecute(String result){

            if (myData != null) {
                Log.d("OnPostExecute", Data);
                myData = Data;
            }

        }

        private String downloadData(String theUrl) throws IOException{ // if theres an error we want to throw it back to the try block to deal with it
            int BUFFER_SIZE = 500000; // the url ywe want to download from
            InputStream is = null;

            String contents = "";
            try{
                URL url = new URL(theUrl);
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100000); // give up after 10000 milliseconds (10 secs)
                conn.setConnectTimeout(150000);
                conn.setRequestMethod("GET"); // standard for accessing data
                conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d("DownloadData", "The response returned is:" + response);
                is = conn.getInputStream(); //

                InputStreamReader isr = new InputStreamReader(is);
                int charRead;
                char[] inputBuffer = new char[BUFFER_SIZE];
                try
                {
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
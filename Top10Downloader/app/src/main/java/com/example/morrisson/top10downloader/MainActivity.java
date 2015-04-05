package com.example.morrisson.top10downloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    Button btnParse;
    ListView listApps;
    String xmlData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnParse = (Button) findViewById(R.id.btnParse);
        listApps = (ListView) findViewById(R.id.listApps);

        btnParse.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ParseApplications parse = new ParseApplications(xmlData);
                boolean operationStatus = parse.process();
                if(operationStatus)
                {
                    ArrayList<Application> allApps = parse.getApplications();

                    ArrayAdapter<Application> adapter = new ArrayAdapter<Application>(MainActivity.this, R.layout.list_item, allApps); // error here as you must create layout > res.layout.rightclick.new android xml
                    listApps.setVisibility(listApps.VISIBLE);
                    listApps.setAdapter(adapter);
                }else{
                    Log.d("MainActivity","Error parsing file");
                }
            }


        });
        new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
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

    private class DownloadData extends AsyncTask<String, Void, String>{ // ( URL, progress of download, what we return back - contents of xml file)
        String myXmlData;
        protected String doInBackground(String...urls){ // one level down from public
            try{
                myXmlData = downloadXML(urls[0]);

            } catch(IOException e){ // catching an error and returns gracefully without crashing
                return "Unable to download xml file";
            }
            return "";
        }

        protected void onPostExecute(String result){
            Log.d("OnPostExecute", myXmlData);
            xmlData = myXmlData; // value gets updated when the entire file is downloaded

        }

        private String downloadXML(String theUrl) throws  IOException{ // throw it back to the calling method, back to where the error occured
            int BUFFER_SIZE = 2000; // how many characters at a time we try to download from the file at one time
            InputStream is= null;

            String xmlContents = "";
            try{
                URL url = new URL(theUrl);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection(); // open a reference to this website
                conn.setReadTimeout(10000); // give up after 10000 milliseconds (10 secs)
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET"); // standard for accessing data
                conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d("DownloadXML", "The response returned is:" + response);
                is = conn.getInputStream(); //

                InputStreamReader isr = new InputStreamReader(is); // passing that connection
                int charRead;
                char[] inputBuffer = new char[BUFFER_SIZE]; // a character array to store the characters read in- new character array of [2000]
                try{
                    while((charRead = isr.read(inputBuffer))>0) // (input stream reader) number of characters read into the buffer
                    {
                        String readString = String.copyValueOf(inputBuffer,0,charRead);
                        xmlContents += readString;
                        inputBuffer = new char[BUFFER_SIZE];

                    }
                    return xmlContents; // the string of xml contents will be returned
                } catch(IOException e){
                    e.printStackTrace();
                    return null;
                }

            }finally { // either way
                if(is != null)
                    is.close(); // whether or not there is an error, the file willbe closed
            }

        }

    }
}



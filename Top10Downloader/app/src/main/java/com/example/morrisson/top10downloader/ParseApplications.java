package com.example.morrisson.top10downloader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

// make sense of the data being passed to it
public class ParseApplications {
    private String data; // internally stores the xml being sent over
    private ArrayList<Application> applications; // contains all the applications- an array of applications (stores details for each app)

    public ArrayList<Application> getApplications() { // it will come back and call this to get the applications
        return applications;
    }

    public boolean process(){
        boolean operationStatus = true; // only changes if theres an error
        Application currentRecord = null;
        boolean inEntry= false;// tells us whether we are in the <entry> of the xml or not
        String textValue = "";

        try{ // using an xml parsing which returns errors if there are some


            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName(); // retrieving strings like <entry> <data> <summary>
               if(eventType == XmlPullParser.START_TAG){ // true or false status variable to see whether were in the event or not
                    if(tagName.equalsIgnoreCase("entry")) // processing file- check if were in the section of <entry>- if so set to true
                    {
                        inEntry = true;
                        currentRecord = new Application();
                    }
                }else if(eventType == XmlPullParser.TEXT){ // saving the text value
                    textValue = xpp.getText(); // recording the data in this variable but only saving sepcific things

                }else if(eventType == XmlPullParser.END_TAG){ // saving our record and creating a new object
                    if(inEntry)
                    {
                        if(tagName.equalsIgnoreCase("entry")) // if we get to the end entry </entry>
                        {
                            applications.add(currentRecord);
                            inEntry = false;
                        }
                        if(tagName.equalsIgnoreCase("name"))
                        {
                            currentRecord.setName(textValue);
                        }
                        else if(tagName.equalsIgnoreCase("artist"))
                        {
                            currentRecord.setArtist(textValue);
                        }
                        else if(tagName.equalsIgnoreCase("releaseDate"))
                        {
                            currentRecord.setReleaseDate(textValue);
                        }
                    }
                }

                eventType = xpp.next(); // move onto the next tag/section
            }




        } catch(Exception e){
            e.printStackTrace();
            operationStatus = false; // if any errors or something goes wrong, turn booleans to false
        }

        for(Application app : applications){
            Log.d("LOG", "*************");
            Log.d("LOG", app.getName());
            Log.d("LOG", app.getReleaseDate());

        }

        return operationStatus;
    }

    public ParseApplications(String xmlData){
        data= xmlData;
        applications = new ArrayList<Application>(); // initialises the array so its ready to write data to it
    }
}

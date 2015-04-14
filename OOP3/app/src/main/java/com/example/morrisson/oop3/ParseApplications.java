package com.example.morrisson.oop3;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

// make sense of the data being passed to it
public class ParseApplications{
    private String alldata; // internally stores the xml being sent over
    private ArrayList<Application> applications; // contains all the applications- an array of applications (stores details for each app)

    public ArrayList<Application> getApplications()
    { // it will come back and call this to get the applications

        return applications;
    }

    public boolean process()
    {
        boolean operationStatus = true; // only changes if theres an error
        Application currentRecord = null;
        boolean inh2= false;// tells us whether we are in the <h2> of the xml or not
        String textValue = "";

        try{ // using an xml parsing which returns errors if there are some


            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(this.alldata)); // converts data into a format that can be read by the parser
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                String tagName = xpp.getName(); // retrieving strings like <entry> <data> <summary>
                if(eventType == XmlPullParser.START_TAG)
                { // true or false status variable to see whether were in the event or not
                    if(tagName.equalsIgnoreCase("h2")) // processing file- check if were in the section of <entry>- if so set to true
                    {
                        inh2 = true;
                        currentRecord = new Application();
                    }
                }
                    ////////////////// this may not work as it may just take the first bit of writing
                    else if(eventType == XmlPullParser.TEXT){ // saving the text value
                        textValue = xpp.getText(); // recording the data in this variable but only saving sepcific things

                    }else if(eventType == XmlPullParser.END_TAG){ // saving our record and creating a new object
                        if(inh2)
                        {
                            if(tagName.equalsIgnoreCase("h2")) // if we get to the end h2 </h2>
                            {
                                applications.add(currentRecord); // if you get to h2 you add to file and then close
                                inh2 = false;
                            }
                            if(tagName.equalsIgnoreCase("city"))
                            {
                                currentRecord.setCity(textValue);
                            }
                            else if(tagName.equalsIgnoreCase("country"))
                            {
                                currentRecord.setCountry(textValue);
                            }

                        }
                }

                eventType = xpp.next(); // move onto the next tag/section
            }




        } catch(Exception e){
            e.printStackTrace();
            operationStatus = false; // if any errors or something goes wrong, turn booleans to false
        }

        for(Application app : applications){ // goes through all the applications objects in the array list and puts the current object into application so we can use the getters and setters
            Log.d("LOG", "*************"); // start of new record- make it look nice
            Log.d("LOG", app.getCity());
            Log.d("LOG", app.getCountry());

        }

        return operationStatus;
    }

    public ParseApplications(String myData){
        alldata = myData;
        applications = new ArrayList<Application>();

    }


}

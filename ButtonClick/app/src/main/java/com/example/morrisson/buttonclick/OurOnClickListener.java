/**
 * Created by MORRISSON on 03/04/2015.
 */
package com.example.morrisson.buttonclick;


import android.view.View;

public class OurOnClickListener implements View.OnClickListener {
    private int count;
    MainActivity caller;

    public OurOnClickListener(MainActivity activity){
        this.caller = activity; // makes sure were talking about this class
        this.count = 0;
    }

    public void onClick(View view) // if button is clicked we'll update the texView to say something else
    {
        count = count + 1;
        String outputString = "The button has been clicked " + count + " time";
        if(count != 1){
            outputString += "s";
            caller.theTextView.setText("The button has been tapped" + count + " time.");

        }


    }
}

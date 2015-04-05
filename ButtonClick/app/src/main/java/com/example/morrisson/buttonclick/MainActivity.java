package com.example.morrisson.buttonclick;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
{
    Button ourButton;
    TextView theTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ourButton = (Button) findViewById(R.id.button1); // telling the button what to do when its tapped
        ourButton.setOnClickListener(new OurOnClickListener(this)); // anytime the button is tapped we want it to run the code

        theTextView = (TextView) findViewById(R.id.textView1);
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
        if (id == R.id.action_settings)
        {
            Toast toastMessage = Toast.makeText(this, "THe settings menu item was taooed", Toast.LENGTH_LONG);
            toastMessage.show(); // show the message we just screated on the screen
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

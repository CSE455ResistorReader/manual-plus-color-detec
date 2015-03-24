package com.example.abe.resistorreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.content.Intent;
import android.widget.EditText;

import android.content.ActivityNotFoundException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.math.BigDecimal;


public class MainActivity2 extends ActionBarActivity {
    //implements View.OnClickListener

    private static EditText screen;
    private static EditText screen2;
    private static EditText screen3;
    private static EditText screen5;
    Button btnClick;
    Button btnInstClick;
    private TextView screen6;
    private TextView screen7;

    public static float resistance = 0, tolerance = 0, tolerancePercent = 0;
    public static float upperRange = 0, lowerRange = 0;

    private static float[] toleranceValue = {0, 1, 2, 0, 5, 0.5f, 0.25f, 0.1f, 10, 0, 5, 10, 20};

    private static int black = 0, brown = 1, red = 2, orange = 3, yellow = 4, green = 5,
            blue = 6, violet = 7, gray = 8, white = 9, gold = 10, silver = 11, none = 12;

    private static int[] colors = { black, brown, red, orange, yellow, green,
            blue, violet, gray, white, gold, silver, none};

    private static float[][] colorValues = {{0, 1},
            {1, 10}, {2, 100}, {3, 1000},
            {4, 10000}, {5, 100000}, {6, 1000000},
            {7, 10000000}, {8, 100000000}, {9, 1000000000},

    };

    public static int fb1 = 0, fb2 = 0, fb3 = 0, fb5 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        screen = (EditText) findViewById(R.id.FBand);// get to screen for Fband
        screen2 = (EditText) findViewById(R.id.SBand);// get to screen for Fband
        screen3 = (EditText) findViewById(R.id.TBand);

        screen5 = (EditText) findViewById(R.id.TolBand);
        screen6 = (TextView) findViewById(R.id.Results);
        screen7 = (TextView) findViewById(R.id.Tolerance);

        btnClick = (Button)findViewById(R.id.buttonCalculate);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonCalculate:
                        ColorAssign();
                        displayResult();
                        break;
                }
            }
        });

        btnInstClick = (Button) findViewById(R.id.buttonInstructions);
        btnInstClick.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                switch (v.getId()){
                                                    case R.id.buttonInstructions:
                                                        btnInstClick();
                                                        break;
                                                }
                                            }

                                        }
        );

        // btnClick.setOnClickListener(this);



    }

  /*  public void getMe(View v) {
        switch (v.getId()){
            case R.id.buttonInstructions:
                btnInstClick();
                break;
        }
    }*/

    public static void getColors() {
        fb1 = 0; fb2 = 0; fb3 = 0; fb5 = 0;

        String FBand1 = screen.getText().toString();
        FBand1 = FBand1.toLowerCase();
        switch (FBand1) {
            case "black":
                fb1 = black;
                break;
            case "brown":
                fb1 = brown;
                break;
            case "red":
                fb1 = red;
                break;
            case "orange":
                fb1 = orange;
                break;
            case "yellow":
                fb1 = yellow;
                break;
            case "green":
                fb1 = green;
                break;
            case "blue":
                fb1 = blue;
                break;
            case "violet":
                fb1 = violet;
                break;
            case "gray":
                fb1 = gray;
                break;
            case "white":
                fb1 = white;
                break;
        }

        //int fb1 = Integer.valueOf(FBand1);

        String FBand2 = screen2.getText().toString();
        FBand2 = FBand2.toLowerCase();
        switch (FBand2) {
            case "black":
                fb2 = black;
                break;
            case "brown":
                fb2 = brown;
                break;
            case "red":
                fb2 = red;
                break;
            case "orange":
                fb2 = orange;
                break;
            case "yellow":
                fb2 = yellow;
                break;
            case "green":
                fb2 = green;
                break;
            case "blue":
                fb2 = blue;
                break;
            case "violet":
                fb2 = violet;
                break;
            case "gray":
                fb2 = gray;
                break;
            case "white":
                fb2 = white;
                break;
        }
        //int fb2 = Integer.valueOf(FBand2);

        String FBand3 = screen3.getText().toString();
        FBand3 = FBand3.toLowerCase();
        switch (FBand3) {
            case "black":
                fb3 = black;
                break;
            case "brown":
                fb3 = brown;
                break;
            case "red":
                fb3 = red;
                break;
            case "orange":
                fb3 = orange;
                break;
            case "yellow":
                fb3 = yellow;
                break;
            case "green":
                fb3 = green;
                break;
            case "blue":
                fb3 = blue;
                break;
            case "violet":
                fb3 = violet;
                break;
            case "gray":
                fb3 = gray;
                break;
            case "white":
                fb3 = white;
                break;
        }
        //int  fb3 = Integer.valueOf(FBand3);

        String FBand5 = screen5.getText().toString();
        FBand5 = FBand5.toLowerCase();
        switch (FBand5) {
            case "gold":
                fb5 = gold;
                break;
            case "silver":
                fb5 = silver;
                break;
            case "none":
                fb5 = none;
                break;
        }
        //int fb5 = Integer.valueOf(FBand5);

    }

    public static void ColorAssign( ) {
        tolerance = 0; resistance = 0; tolerancePercent = 0;

        getColors();

        if (fb1 != gold && fb1 != silver && fb1 != none) {
            for (int i = 0; i < colors.length; i++) {
                if (fb1 == colors[i])
                    resistance += colorValues[i][0] * 10;
            }
            for (int i = 0; i < colors.length; i++) {
                if (fb2 == colors[i])
                    resistance += colorValues[i][0];
            }
            for (int i = 0; i < colors.length; i++) {
                if (fb3 == colors[i])
                    resistance *= colorValues[i][1];
            }
            for (int i = 0; i < colors.length; i++) {
                if (fb5 == colors[i] && colors[i] != 0)
                    tolerance += toleranceValue[i];
            }
        }
        else {
            int firstTemp = fb5, secondTemp = fb3, thirdTemp = fb2, fourthTemp = fb1;

            for (int i = 0; i < colors.length; i++) {
                if (firstTemp == colors[i])
                    resistance += colorValues[i][0] * 10; }
            for (int i = 0; i < colors.length; i++) {
                if (secondTemp == colors[i])
                    resistance += colorValues[i][0]; }
            for (int i = 0; i < colors.length; i++) {
                if (thirdTemp == colors[i])
                    resistance *= colorValues[i][1]; }
            for (int i = 0; i < colors.length; i++) {
                if (fourthTemp == colors[i] && colors[i] != 0)
                    tolerance += toleranceValue[i]; }
        }

        tolerancePercent += tolerance;

    }

    public void displayResult(){
        String FR = String.valueOf("Resistance is: " + resistance); // changing float to string
        screen6.setText(FR);// display string

        String FT = String.valueOf("Tolerance is: +/-" + tolerancePercent + "%"); // changing float to string
        screen7.setText(FT);
    }

    public void btnInstClick(){
        startActivity(new Intent("com.example.abe.resistorreader.MainActivity3"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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

}

package com.example.expensetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // xml elements
    private EditText numField;
    private Button goalBtn;
    private Button addBtn;
    private TextView currentLbl;
    private TextView allTimeLbl;
    private TextView goalLbl;
    private TextView remainingLbl;
    private TextView dispAlltime;
    private TextView dispGoal;
    private TextView dispRemaining;
    private Button reset;
    private Button wipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // LINK XML COMPONENTS
        numField = findViewById(R.id.numField);
        goalBtn = findViewById(R.id.goalBtn);
        addBtn = findViewById(R.id.addBtn);
        currentLbl = findViewById(R.id.currentLbl);
        allTimeLbl = findViewById(R.id.allTimeLbl);
        goalLbl = findViewById(R.id.goalLbl);
        remainingLbl = findViewById(R.id.remainingLbl);
        dispAlltime = findViewById(R.id.dispAllTime);
        dispGoal = findViewById(R.id.dispGoal);
        dispRemaining = findViewById(R.id.dispRemaining);
        reset = findViewById(R.id.reset);
        wipe = findViewById(R.id.wipe);
        //

        // SHARED PREFERENCES
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        // SET THE LABELS
        updateLbls();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredNum();
            }
        });

        numField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                enteredNum();
                return true;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        goalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGoal();
            }
        });

        wipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipe();
            }
        });
    }

    public void enteredNum(){
        // take the user input, put it to double, take the sharedpref stuff, bring them out and turn to double
        // add them, turn back to string, save, update fields
        String enteredStr = numField.getText().toString();
        double entered = Double.parseDouble(enteredStr);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //ADJUSTING CURRENT
        String savedCurr = sharedPref.getString("Current", "0");
        double savedCurrDbl = Double.parseDouble(savedCurr);
        savedCurrDbl += entered;
        savedCurr = String.format("%.2f", savedCurrDbl);

        //ADJUST ALLTIME
        String savedAllTime = sharedPref.getString("All Time", "0");
        double savedAllTimeDbl = Double.parseDouble(savedAllTime);
        savedAllTimeDbl += entered;
        savedAllTime = String.format("%.2f", savedAllTimeDbl);

        // SET NEW VALUES
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Current", savedCurr);
        editor.putString("All Time", savedAllTime);
        editor.apply();

        //UPDATE
        updateLbls();
        numField.requestFocus();
        numField.selectAll();
    }

    public void updateLbls(){
        //RETRIEVE THE SAVED VALUES AND DISPLAY THEM
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String savedCurr = sharedPref.getString("Current", "0");
        String savedAllTime = sharedPref.getString("All Time", "0");
        String savedGoal = sharedPref.getString("Goal", "0");


        // CALCULATE THE NEW REMAINING
        double savedCurrDbl = Double.parseDouble(savedCurr);
        double savedGoalDbl = Double.parseDouble(savedGoal);
        double remainingDbl = savedGoalDbl - savedCurrDbl;
        String remaining = String.format("%.2f", remainingDbl);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Remaining", remaining);
        editor.apply();



        currentLbl.setText("$" + savedCurr); // FORMAT
        dispAlltime.setText("$" + savedAllTime); // FORMAT
        dispGoal.setText("$" + savedGoal);

        dispRemaining.setText("$" + remaining);
    }

    public void updateGoal(){
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String enteredGoal = numField.getText().toString();
        double enteredGoalDbl = Double.parseDouble(enteredGoal);
        enteredGoal = String.format("%.2f", enteredGoalDbl);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Goal", enteredGoal);
        editor.apply();

        updateLbls();

    }

    public void reset(){
        //SETS THE CURRENT BACK TO ZERO
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String zero = "0.00";
        editor.putString("Current", zero);
        editor.apply();

        updateLbls();
    }

    public void wipe(){
        String zero = "0.00";
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("Current", zero);
        editor.putString("All Time", zero);
        editor.putString("Goal", zero);

        editor.apply();

        updateLbls();
    }
}
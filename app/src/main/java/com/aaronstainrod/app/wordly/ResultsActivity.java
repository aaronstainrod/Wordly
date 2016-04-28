package com.aaronstainrod.app.wordly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    ///Variables
    public static String output, result;

    //Views
    TextView result_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        output = (String) intent.getExtras().get(result);

        result_display = (TextView) findViewById(R.id.result_display);
        result_display.setText(output);
    }
}

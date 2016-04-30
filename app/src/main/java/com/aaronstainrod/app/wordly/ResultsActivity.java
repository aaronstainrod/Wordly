package com.aaronstainrod.app.wordly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultsActivity extends AppCompatActivity {

    ///Variables
    public static String output, result;
    Interpreter in;

    //Views
    TextView result_text_view;
    ListView results_drawer_list;

    //Toolbar
    Toolbar results_toolbar;

    JSONObject info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        //Sets up navigation drawer
        String[] activities = getResources().getStringArray(R.array.activities);
        results_drawer_list = (ListView) findViewById(R.id.results_left_drawer);
        results_drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, activities));
        results_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Sets up Toolbar
        results_toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(results_toolbar);


        //Sets Text
        Intent intent = getIntent();
        output = (String) intent.getExtras().get(result);
        result_text_view = (TextView) findViewById(R.id.result_text_view);
        result_text_view.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results_action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switchActivities(position);
        }
    }

    public void switchActivities(int position){
        Intent intent;

        switch(position) {
            case 1:
                intent = new Intent(this, SynonymsActivity.class);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(this, AntonymsActivity.class);
                startActivity(intent);
                break;

            case 3:
                intent = new Intent(this, RhymesActivity.class);
                startActivity(intent);
                break;

            case 4:
                intent = new Intent(this, SentencesActivity.class);
                startActivity(intent);
                break;

            case 5:
                intent = new Intent(this, SyllablesActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
}

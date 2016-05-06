package com.aaronstainrod.app.wordly;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    ///Variables
    public String[] results_info;
    TextView results_parameter_view, results_text_view;
    ListView results_drawer_list;
    Toolbar results_toolbar;
    private boolean isChecked;
    private RelativeLayout layout;
    private DrawerLayout results_drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        layout = (RelativeLayout) findViewById(R.id.results_layout);
        results_drawer_layout = (DrawerLayout) findViewById(R.id.results_drawer_layout);


        //Gets previous activity's output
        Intent intent = getIntent();
        results_info = (String[]) intent.getExtras().get("results_info");
        isChecked = (boolean) intent.getExtras().get("isChecked");

        //Sets text for input view
        results_parameter_view = (TextView) findViewById(R.id.results_parameter_view);
        results_parameter_view.setText(results_info[0].toUpperCase());
        results_parameter_view.setShadowLayer(1, 0, 0, Color.BLACK);

        //Sets Text for results
        results_text_view = (TextView) findViewById(R.id.result_text_view);
        results_text_view.setText(results_info[1]);

        //Sets up navigation drawer
        String[] activities = getResources().getStringArray(R.array.activities);
        results_drawer_list = (ListView) findViewById(R.id.results_left_drawer);
        results_drawer_list.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, activities));
        results_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Sets up Toolbar
        results_toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(results_toolbar);

        adjustMode(isChecked);
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
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I learned about the " + results_info[0].toUpperCase() + " today!");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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
                intent = new Intent(this, ExamplesActivity.class);
                startActivity(intent);
                break;

            case 5:
                intent = new Intent(this, SyllablesActivity.class);
                startActivity(intent);
                break;

            default:
                results_drawer_layout.closeDrawers();
                break;
        }

    }

    public void adjustMode(boolean isChecked) {
        isChecked = getIntent().getBooleanExtra("isChecked",isChecked);
        if (isChecked) {
            layout.setBackgroundColor(Color.DKGRAY);
            results_text_view.setTextColor(Color.WHITE);
        } else {
            layout.setBackgroundColor(Color.WHITE);
            results_text_view.setTextColor(Color.BLUE);
        }
    }
}

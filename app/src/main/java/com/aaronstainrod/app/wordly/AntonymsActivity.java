package com.aaronstainrod.app.wordly;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AntonymsActivity extends AppCompatActivity {

    //Logging purposes
    private final String LOG_TAG = AntonymsActivity.class.getSimpleName();

    //Views
    private ListView antonyms_drawer_list;
    private DrawerLayout antonyms_drawer_layout;
    private Toolbar antonyms_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antonyms);

        //Sets up navigation drawer
        String[] activities = getResources().getStringArray(R.array.activities);
        antonyms_drawer_list = (ListView) findViewById(R.id.antonyms_left_drawer);
        antonyms_drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, activities));
        antonyms_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Navigation Drawer
        antonyms_drawer_layout = (DrawerLayout) findViewById(R.id.antonyms_drawer_layout);

        //Toolbar
        Toolbar antonyms_toolbar = (Toolbar) findViewById(R.id.antonyms_toolbar);
        setSupportActionBar(antonyms_toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_title:
                return true;

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
    };

    public void switchActivities(int position){
        Intent intent;

        switch(position) {
            case 0:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(this, SynonymsActivity.class);
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
                antonyms_drawer_layout.closeDrawer(antonyms_drawer_list);
                break;
        }

    }
    public void onClickFind(View view) {
        //Gets user input
        EditText user_text = (EditText) findViewById(R.id.antonyms_user_input);
        String user_input = user_text.getText().toString().replaceAll(" ", "");

        new FetchAntonyms().execute(user_input);
        antonyms_drawer_layout.closeDrawer(antonyms_drawer_list);
    }


    private class FetchAntonyms extends AsyncTask<String,Void,String> {

        public String antonym;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the Words Query
                URL url = new URL("https://wordsapiv1.p.mashape.com/words/"
                        + params[0]
                        + "/antonyms?mashape-key=3T1qHC6ngcmshDplvZw8GjjqLdvCp1urXZCjsnFhgmA8XEKfjX"
                );

                // Create the request to open Words, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.i(LOG_TAG, line);
                }
                if (buffer.length() == 0) {
                    Log.i(LOG_TAG, "Null");
                    return null;
                }
                //Response from Yoda Speak API
                antonym = buffer.toString();
                Log.i(LOG_TAG, antonym);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e.getMessage(), e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            if (antonym == null) {
                Log.e(LOG_TAG, "Error retrieving antonym");
            }
            return antonym;
        }

        protected void onPostExecute(String result) {
            antonym = result;
            Intent intent = new Intent(AntonymsActivity.this, ResultsActivity.class);
            intent.putExtra(ResultsActivity.output, result);
            startActivity(intent);
        }
    }
}

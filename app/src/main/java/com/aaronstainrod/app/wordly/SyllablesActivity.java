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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyllablesActivity extends AppCompatActivity {

    private String user_parameter;

    //Logging purposes
    private final String LOG_TAG = SyllablesActivity.class.getSimpleName();

    //Views
    private ListView syllables_drawer_list;
    private DrawerLayout syllables_drawer_layout;
    private Toolbar syllables_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllables);

        //Sets up navigation drawer
        String[] activities = getResources().getStringArray(R.array.activities);
        syllables_drawer_list = (ListView) findViewById(R.id.syllables_left_drawer);
        syllables_drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, activities));
        syllables_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Navigation Drawer
        syllables_drawer_layout = (DrawerLayout) findViewById(R.id.syllables_drawer_layout);

        //Toolbar
        Toolbar syllables_toolbar = (Toolbar) findViewById(R.id.syllables_toolbar);
        setSupportActionBar(syllables_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_mode:
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

            default:
                syllables_drawer_layout.closeDrawer(syllables_drawer_list);
                break;
        }

    }
    public void onClickFind(View view) {
        //Gets user input
        EditText user_text = (EditText) findViewById(R.id.syllables_user_input);
        String user_input = user_text.getText().toString().replaceAll(" ", "");

        new FetchSyllables().execute(user_input);
        syllables_drawer_layout.closeDrawer(syllables_drawer_list);
    }


    private class FetchSyllables extends AsyncTask<String,Void,String> {

        public String syllable;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the Words Query
                URL url = new URL("https://wordsapiv1.p.mashape.com/words/"
                        + params[0]
                        + "/syllables?mashape-key=3T1qHC6ngcmshDplvZw8GjjqLdvCp1urXZCjsnFhgmA8XEKfjX"
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
                //Response from WordAPI
                String response = buffer.toString();
                JSONArray list = new JSONObject(response).getJSONObject("syllables").getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {
                    syllable += "\"" + list.getString(i) + "\" \t \t";
                }

                syllable = syllable.substring(4).trim();

                Log.i(LOG_TAG, syllable);
            } catch (JSONException e) {
                e.printStackTrace();
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
            if (syllable == null) {
                Log.e(LOG_TAG, "Error retrieving syllables");
            }

            user_parameter = "pronunciation of \"" + params[0] + "\"";

            return syllable;
        }

        protected void onPostExecute(String result) {
            String[] results_info = {user_parameter, result};
            Intent intent = new Intent(SyllablesActivity.this, ResultsActivity.class);
            intent.putExtra("results_info", results_info);
            startActivity(intent);
        }
    }
}

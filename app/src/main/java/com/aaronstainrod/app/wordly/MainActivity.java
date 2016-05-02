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

public class MainActivity extends AppCompatActivity {

    //Variables
    private String user_parameter;

    //Logging purposes
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    //Views
    private ListView main_drawer_list;
    private DrawerLayout main_drawer_layout;

    //private ShareActionProvider
    private Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up navigation drawer
        String[] activities = getResources().getStringArray(R.array.activities);
        main_drawer_list = (ListView) findViewById(R.id.main_left_drawer);
        main_drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, activities));
        main_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Navigation Drawer
        main_drawer_layout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        //Toolbar
        Toolbar main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
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
                break;
        }

    }
    public void onClickFind(View view) {
        //Gets user input
        EditText user_text = (EditText) findViewById(R.id.main_user_input);
        String user_input = user_text.getText().toString().replaceAll(" ", "");

        new FetchDefinition().execute(user_input);
        main_drawer_layout.closeDrawer(main_drawer_list);
    }


    private class FetchDefinition extends AsyncTask<String,Void,String> {

        public String definition, output;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the Words Query
                URL url = new URL("https://wordsapiv1.p.mashape.com/words/"
                        + params[0]
                        + "/definitions?mashape-key=3T1qHC6ngcmshDplvZw8GjjqLdvCp1urXZCjsnFhgmA8XEKfjX"
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
                }
                if (buffer.length() == 0) {
                    Log.i(LOG_TAG, "Null");
                    return null;
                }
                //Response from WordsAPI
                String response = buffer.toString();
                JSONArray definitions = new JSONObject(response).getJSONArray("definitions");

                for (int i = 0; i < definitions.length(); i++) {
                    JSONObject d = definitions.getJSONObject(i);
                    definition += d.getString("partOfSpeech") + ":\n" + d.getString("definition") + "\n \n";
                }
                //Removes the word "null" from the beginning of the string
                definition = definition.substring(4);

            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return "Sorry, couldn't get anything for you";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {}
                }
            }
            if (definition == null) {
                Log.e(LOG_TAG, "Error retrieving definition");
            }

            user_parameter = "Definition(s) of \"" + params[0] + "\"";

            return definition;

        }

        protected void onPostExecute(String result) {
            String[] results_info = {user_parameter, result};
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("results_info", results_info);
            startActivity(intent);
        }
    }
}

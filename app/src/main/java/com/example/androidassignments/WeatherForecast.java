package com.example.androidassignments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "WeatherForecast";

    ProgressBar progress;
    ImageView imageView;
    TextView current_temp;
    TextView min_temp;
    TextView max_temp;
    List<String> cityList;
    TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        current_temp = findViewById(R.id.currentTemp);
        min_temp = findViewById(R.id.minTemp);
        max_temp = findViewById(R.id.maxTemp);
        imageView = findViewById(R.id.picture);
        cityName = findViewById(R.id.cityName);
        // make the progress bar initially visible upon creation
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        // calls get_a_city, which takes the city selected from the list and creates a ForecastQuery object from it
        get_a_city();
    }

    public void get_a_city() {

        // "cities" array in strings.xml
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));

        final Spinner citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);

        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                new ForecastQuery(cityList.get(i)).execute("Selected a city; created a ForecastQuery object");
                cityName.setText("Current Weather in " + cityList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
    }

    private class ForecastQuery extends AsyncTask <String, Integer, String> {

        private String min;
        private String max;
        private String current;
        private Bitmap picture;
        protected String city;
//        protected String city = "ottawa";

        ForecastQuery (String city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("onPreExecute", " is called");
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i("Incoming parameters", strings[0] + "---------------------------") ;
            try {
                // openweather api call: q = city name, appid = API key, mode = response format, units = units of measurement
                URL url = new URL(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "q=" + this.city + "," + "ca&" +
                                "APPID=79cecf493cb6e52d25bb7b7050ff723c&" +
                                "mode=xml&" +
                                "units=metric");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                InputStream in = httpsURLConnection.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    int type;
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                current = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                // sleep to show progress bar loading
                                Thread.sleep(700);
                                min = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                Thread.sleep(700);
                                max = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                                Thread.sleep(700);
                            } else if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";
                                Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);
                                if (fileExistence(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "Found the file locally");
                                    picture = BitmapFactory.decodeStream(fis);
                                } else {
                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                    picture = getImage(new URL(iconUrl));

                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    picture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    httpsURLConnection.disconnect();
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return " do background ended";

        }

        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i ("Progress bar value",  values[0] +"-------------------------------") ;
            // makes the progress bar visible each time a new city is selected
            progress.setVisibility(View.VISIBLE);
            // values can be 25, 50, 75, or 100
            progress.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String a) {
            Log.i ("onPostExecute", a.toString() + "------------------");
            // after execution, make the progress bar invisible
            progress.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(picture);
            current_temp.setText(current + "C\u00b0");
            min_temp.setText(min + "C\u00b0");
            max_temp.setText(max + "C\u00b0");
            if (current == null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WeatherForecast.this);
                // sets the dialog box's message, title, and the 2 buttons (yes and no)
                builder.setMessage(R.string.cityDialog)
                        // if user clicks yes, then finish() is called
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int id) {
                            }
                        })
                        .show();
            }
        }

    }
}
package com.materialapp.gauravtandon.sunshine.app;

/**
 * Created by Gaurav Tandon on 3/01/2015.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    ArrayAdapter<String> stringArrayAdapter =null;
    View rootView = null;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        //add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle action bar item  click here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        // you specify the parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if(id == R.id.action_refresh ){
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link android.app.Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private void updateWeather() {
        //String urlString = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        //fetchWeatherTask.execute("94043");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        String locationString = sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        String tempUnitString = sharedPreferences.getString(getString(R.string.pref_temp_unit_key),getString(R.string.pref_temp_unit_default));
        fetchWeatherTask.execute(locationString,tempUnitString);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        final ArrayList<String> stringArrayList = new ArrayList<String>();
//        stringArrayList.add("Today - Sunny - 88/63");
//        stringArrayList.add("Tomorrow - Foggy - 70/46");
//        stringArrayList.add("Wednesday - Rainy - 81/53");
//        stringArrayList.add("Thursday - Cloudy - 72/60");
//        stringArrayList.add("Friday - Foggy - 73/50");
//        stringArrayList.add("Saturday - Sunny - 84/70");

        stringArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,new ArrayList<String>());

        final ListView view;
        view = (ListView) rootView.findViewById(R.id.listview_forecast);
        view.setAdapter(stringArrayAdapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {

                String forecast = stringArrayAdapter.getItem(position);
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent detailActivityIntent = new Intent();
                detailActivityIntent.setClassName("com.materialapp.gauravtandon.sunshine.app" , "com.materialapp.gauravtandon.sunshine.app.DetailActivity" );
                ComponentName detailActivityComponent = new ComponentName("com.materialapp.gauravtandon.sunshine.app" , "com.materialapp.gauravtandon.sunshine.app.DetailActivity" );
                detailActivityIntent.setComponent(detailActivityComponent);
                detailActivityIntent.setAction(Intent.ACTION_VIEW);
                detailActivityIntent.putExtra("forecastDetailStr", forecast);
                startActivity(detailActivityIntent);
            }
        });

        //MenuView menuView = (MenuView) inflater.inflate(R.menu.forecastfragment,container,false);


        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param strings The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings!=null){
                stringArrayAdapter.clear();
                for(String dayForeCastStr : strings){
                    stringArrayAdapter.add(dayForeCastStr);
                }
            }
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String[] doInBackground(String... params) {

            int size = params.length;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String locationStr = null;
            String tempUnitStr = null;
            for(int i =0; i<size; i++){
                if(i==0){
                    locationStr = params[i].toString();
                }else if(i==1){
                    tempUnitStr = params[i].toString();
                }

                }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q", locationStr)
                        .appendQueryParameter("mode","json")
                        //.appendQueryParameter("unit","metric")
                        .appendQueryParameter("units", tempUnitStr)
                        .appendQueryParameter("cnt", "7");
                String urlStr = builder.build().toString();
                Log.i(LOG_TAG,"URLString :" + urlStr);
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                URL url = new URL(urlStr);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //forecastJsonStr = null;
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //forecastJsonStr = null;
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                e.printStackTrace();
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                //forecastJsonStr = null;
                return null;
            } finally{
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
                Log.i(LOG_TAG,"Forecast JSON String:" +forecastJsonStr);

            }

            String[] weatherFormatedDataList = null;
            try{
                weatherFormatedDataList = getWeatherDataFromJson(forecastJsonStr, 7);

//                String geoLocationString = getGeoLocationFromJson(forecastJsonStr);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
//                sharedPreferences.edit().putString(getString(R.string.geo_location_str_key), getString(R.string.geo_location_str_default));
            }
            catch (JSONException e){
                e.printStackTrace();
                Log.e(LOG_TAG, e.getMessage());
            }
            finally {
                int i=1;
                for(String str : weatherFormatedDataList){
                    Log.i(LOG_TAG, "Weather Data day["+i+"] = " + str);
                    i++;
                }
            }


            return weatherFormatedDataList;
        }

        private String getGeoLocationFromJson(String forecastJsonStr) throws JSONException{
            String geoUriStr = "";
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_CITY = "city";
            final String OWM_COORD = "coord";
            final String OWM_LON = "lon";
            final String OWM_LAT = "lat";
            final String ZOOM_LEVEL = "?z=11"; // Highest zoom is 23
            final String GEO_STR = "geo:";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONObject coordJson = forecastJson.getJSONObject(OWM_CITY).getJSONObject(OWM_COORD);
            geoUriStr= GEO_STR + geoUriStr + coordJson.getString(OWM_LON)+ "," + coordJson.getString(OWM_LAT) + ZOOM_LEVEL;

            return geoUriStr;
        }


        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
            return format.format(date).toString();
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DATETIME = "dt";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            String[] resultStrs = new String[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime = dayForecast.getLong(OWM_DATETIME);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            return resultStrs;
        }
    }


}
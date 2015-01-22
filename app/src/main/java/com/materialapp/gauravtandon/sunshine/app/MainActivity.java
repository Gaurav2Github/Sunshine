package com.materialapp.gauravtandon.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG,"OnDestroy triggered");
        super.onDestroy();
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        Log.i(LOG_TAG,"OnStart triggered");
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        Log.i(LOG_TAG,"OnResume triggered");
        super.onPostResume();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        Log.i(LOG_TAG,"OnPause triggered");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG,"OnStop triggered");
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        Log.i(LOG_TAG,"OnCreate triggered");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            //Intent settingActivityIntent = new Intent();
            //settingActivityIntent.setClassName("com.materialapp.gauravtandon.sunshine.app" , "com.materialapp.gauravtandon.sunshine.app.SettingActivity" );
            //ComponentName settingActivityComponent = new ComponentName("com.materialapp.gauravtandon.sunshine.app" , "com.materialapp.gauravtandon.sunshine.app.SettingActivity" );
            //settingActivityIntent.setComponent(settingActivityComponent);
            //settingActivityIntent.setAction(Intent.ACTION_VIEW);
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }else {
            if (id == R.id.action_map) {
                openPreferredLocationInMap();
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(item.getActionView().getContext());
//                String uriStr = sharedPreferences.getString(getString(R.string.geo_location_str_key), getString(R.string.geo_location_str_default));
//                Uri.Builder builder = new Uri.Builder();
//                builder.appendPath(uriStr);
//                Uri geoUri = builder.build();
//                intent.setData(geoUri);
//                //if(intent.resolveActivity(getActivity().getPackageManager())!= null){
//                startActivity(intent);
                //}
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap(){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String location = sharedPrefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        //Using the URI scheme showing a location found on
        // intent can be detailed in  the "Common Intents" page
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .appendQueryParameter("z","11")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Log.d(LOG_TAG, "Couldn't call " + location + ", no Application found to start this intent");
        }
    }




}

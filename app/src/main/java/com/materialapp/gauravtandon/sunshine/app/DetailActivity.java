package com.materialapp.gauravtandon.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);



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
            //startActivity(settingActivityIntent);
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

        private String mForecastStr;

        private ShareActionProvider mShareActionProvider;

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        /**
         * Initialize the contents of the Activity's standard options menu.  You
         * should place your menu items in to <var>menu</var>.  For this method
         * to be called, you must have first called {@link #setHasOptionsMenu}.  See
         * {@link PlaceholderFragment#onCreateOptionsMenu(android.view.Menu) Activity.onCreateOptionsMenu}
         * for more information.
         *
         * @param menu     The options menu in which you place your items.
         * @param inflater
         * @see #setHasOptionsMenu
         * @see #onPrepareOptionsMenu
         * @see #onOptionsItemSelected
         */
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            //super.onCreateOptionsMenu(menu, inflater);
            //inflate the menu; tis adds item to action bar if they are present
            inflater.inflate(R.menu.detailfragment,menu);

            //Retrive the share menu item.
            MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set change the share intent.
            mShareActionProvider =  (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);

            //Attach an intent to this ShareActionProvider. You can update ths at any time,
            // like when user selects a new piece of data they might like to share.
            if(mShareActionProvider!=null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }else{
                Log.d(LOG_TAG, "Share Action provider is null?");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent detailIntent = getActivity().getIntent();
            mForecastStr = detailIntent.getStringExtra("forecastDetailStr");
            TextView forecastDetailTextView = (TextView) rootView.findViewById(R.id.forecastDetailTextView);
            if(mForecastStr!=null) {
                forecastDetailTextView.setText(mForecastStr);
            }
            return rootView;
        }

        private Intent createShareForecastIntent(){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
            return  shareIntent;
        }
    }
}

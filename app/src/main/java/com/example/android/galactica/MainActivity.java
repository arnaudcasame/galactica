package com.example.android.galactica;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>, SharedPreferences.OnSharedPreferenceChangeListener{

    private final int NEWS_LOADER_ID = 1;
    private final String GUARDIAN_URL = "https://content.guardianapis.com/search?page-size=30&show-tags=contributor&section=weather&q=weather&api-key=5876e108-6070-4a62-a12b-4905781e00ce";
    private final String GUARDIAN_URL_NO_SECTION = "https://content.guardianapis.com/search?page-size=30&show-tags=contributor&q=weather&api-key=5876e108-6070-4a62-a12b-4905781e00ce";


    private TextView no_connection;
    private TextView no_data;
    private View loadingIndicator;

    private ListView list;
    private ArrayList<News> newsList;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list_view);
        newsList = new ArrayList<News>();
        newsAdapter = new NewsAdapter(this, newsList);
        list.setAdapter(newsAdapter);

        loadingIndicator = findViewById(R.id.loading_warning);
        no_connection = findViewById(R.id.no_internet_warning);
        no_data = findViewById(R.id.no_data_warning);

        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connectionInfo = connectManager.getActiveNetworkInfo();

        if (connectionInfo != null && connectionInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);

            no_connection.setVisibility(View.VISIBLE);
            no_connection.setText(R.string.no_internet_connection);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getLink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        newsAdapter.clear();
        loadingIndicator.setVisibility(View.GONE);
        if(news != null){
            if(!news.isEmpty()){
                newsAdapter.addAll(news);
            }else {
                no_data.setVisibility(View.VISIBLE);
                no_data.setText(R.string.no_data_connection);
            }
        } else {
            no_data.setVisibility(View.VISIBLE);
            no_data.setText(R.string.server_response);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent preferences = new Intent(this, SettingsActivity.class);
                startActivity(preferences);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.search_by_key)) ||
                key.equals(getString(R.string.search_number_page_key))){
            newsAdapter.clear();

            no_data.setVisibility(View.GONE);

            View loadingIndicator = findViewById(R.id.loading_warning);
            loadingIndicator.setVisibility(View.VISIBLE);

            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPrefs.getString(
                getString(R.string.search_by_key),
                getString(R.string.search_by_default_section));

        String pageSize = sharedPrefs.getString(
                getString(R.string.search_number_page_key),
                getString(R.string.search_by_default_page_number)
        );



        if(section.equals("random")){
            Uri baseUri = Uri.parse(GUARDIAN_URL_NO_SECTION);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("q", "news");
            uriBuilder.appendQueryParameter("page-size", pageSize);
            return new NewsLoader(this, uriBuilder.toString());
        }else{
            Uri baseUri = Uri.parse(GUARDIAN_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("q", section);
            uriBuilder.appendQueryParameter("section", section);
            uriBuilder.appendQueryParameter("page-size", pageSize);
            return new NewsLoader(this, uriBuilder.toString());
        }
    }
}

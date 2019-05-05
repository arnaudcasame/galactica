package com.example.android.galactica;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Arnaud Casamé on 7/12/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public NewsLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if(url == null){
            return null;
        }

        List<News> news = HttpHandler.makeServiceCall(url);
        return news;
    }
}

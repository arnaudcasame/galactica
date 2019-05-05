package com.example.android.galactica;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arnaud Casamé on 7/8/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.card_title);
        title.setText(currentNews.getTitle());

        TextView description = (TextView) listItemView.findViewById(R.id.card_desc);
        description.setText(currentNews.getDescription());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        if(!currentNews.getAuthor().isEmpty()){
            author.setText(getContext().getString(R.string.written_by) + " " + currentNews.getAuthor());
        }else{
            author.setVisibility(View.GONE);
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(getContext().getString(R.string.published_on) + " " + currentNews.getDate());

        return listItemView;
    }
}

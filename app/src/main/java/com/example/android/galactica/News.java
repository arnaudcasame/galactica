package com.example.android.galactica;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arnaud Casamé on 7/8/2018.
 */

public class News {
    private String title;
    private String description;
    private String link;
    private String author;
    private Date date;

    public News(String title, String description, String link, String date, String author){
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.date = convertDate(date);
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getLink(){
        return link;
    }

    public Date getDate(){
        return date;
    }

    public String getAuthor(){
        return author;
    }

    private Date convertDate(String strFormat){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(strFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}

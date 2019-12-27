package com.example.booksearch;

import android.content.Context;
import android.net.Uri;

import java.net.URI;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {
    private String mUrl;
    public BooksLoader(Context context,String url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
       if(mUrl==null)
           return null;
       List<Books> books = QueryUtils.fetchBooksData(mUrl);
       return books;
    }
}

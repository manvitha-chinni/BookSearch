package com.example.booksearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>>
        {
    private static final String GOOGLE_BOOKS_URL ="https://www.googleapis.com/books/v1/volumes?q=";
    private static  String GOOGLE_BOOKS_REQUEST_URL="";
    private static final int BOOKS_LOADER_ID=1;
    private BooksAdapter mAdapter;
    LoaderManager loaderManager;
    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    ProgressBar progressBar;
    TextView emptyState;
    String queryString="";
    TextView reTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyState = (TextView) findViewById(R.id.empty_view);


        ListView listView= (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(emptyState);
        mAdapter = new BooksAdapter(this,new ArrayList<Books>());
        listView.setAdapter(mAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.edit_text_view);
        progressBar=(ProgressBar) findViewById(R.id.loading_indicator);



        reTry = (TextView) findViewById(R.id.reTry);
        reTry.setText("");

        reTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo =connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    reTry.setText("");
                    emptyState.setText("search to find books");
                    loaderManager= getSupportLoaderManager();
                    loaderManager.initLoader(BOOKS_LOADER_ID,null,MainActivity.this);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    emptyState.setText("No Internet Connection");
                    reTry.setText("RETRY");
                }

            }
        });
        connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            reTry.setText("");
            emptyState.setText("search to find books");
            loaderManager= getSupportLoaderManager();
            loaderManager.initLoader(BOOKS_LOADER_ID,null,MainActivity.this);

        }
        else{
            progressBar.setVisibility(View.GONE);
            emptyState.setText("No Internet Connection");
            reTry.setText("RETRY");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.clear();
                if(query=="")
                    queryString="";
                else
                    queryString=query;
                emptyState.setText("");
                networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    reTry.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    GOOGLE_BOOKS_REQUEST_URL=GOOGLE_BOOKS_URL+query;
                    loaderManager.restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                }
                else{
                    mAdapter.clear();
                    emptyState.setText("No Internet Connection");
                    reTry.setText("RETRY");
                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books currentBook=mAdapter.getItem(position);
                String[] data = new String[7];
                data[0]=currentBook.getMimage_uri();
                data[1]=currentBook.getMbook_title();
                data[2]=currentBook.getMbook_author();
                data[3]=""+currentBook.getMrating();
                data[4]=currentBook.getMbook_url();
                data[5]=currentBook.getMdescription();
                data[6]=position+"";
                Intent intent=new Intent(MainActivity.this,Description.class);
                intent.putExtra("message",data);
                startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BooksLoader(this,GOOGLE_BOOKS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> data) {
        mAdapter.clear();
        progressBar.setVisibility(View.GONE);
        if(data!=null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        else{
            TextView empty_text = (TextView) findViewById(R.id.empty_view);
            empty_text.setText("books is not available");
        }
        if(queryString==""){
            emptyState.setText("search to find books");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        mAdapter.addAll(new ArrayList<Books>());
    }
}


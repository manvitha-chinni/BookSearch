package com.example.booksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Description extends AppCompatActivity {
    BooksAdapter mAdapter;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        setContentView(R.layout.activity_description);
        TextView title =(TextView)findViewById(R.id.title);
        TextView author =(TextView)findViewById(R.id.author);
        TextView rating =(TextView)findViewById(R.id.rating);
        TextView link =(TextView)findViewById(R.id.book_link);
        TextView description =(TextView)findViewById(R.id.description);
        Intent intent=getIntent();
        String[] data =intent.getStringArrayExtra("message");
        title.setText(data[1]);
        author.setText(data[2]);
        rating.setText(data[3]);
        link.setText(data[4]);
        description.setText(data[5]);
        url=data[4];

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(websiteIntent);

            }
        });


    }
}

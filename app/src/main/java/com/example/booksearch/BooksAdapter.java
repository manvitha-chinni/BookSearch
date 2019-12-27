package com.example.booksearch;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BooksAdapter extends ArrayAdapter<Books> {
    public BooksAdapter(Context context, List<Books> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_list_item, parent, false);
        }
        Books books = getItem(position);

        ImageView book_image= (ImageView) listItemView.findViewById(R.id.book_image);
        book_image.setImageResource(R.drawable.book_search_icon);
       // book_image.setImageURI(Uri.parse(books.getMimage_uri()));

        TextView book_title = (TextView) listItemView.findViewById(R.id.book_name);
        String name=books.getMbook_title();
        book_title.setText(name);

        TextView book_author = (TextView) listItemView.findViewById(R.id.author_name);
        String autor=books.getMbook_author();
        book_author.setText(autor);

        TextView book_rating = (TextView) listItemView.findViewById(R.id.rating);
        String formatedRating = formatMagnitude(books.getMrating());
        if(formatedRating!="0.0")
            book_rating.setText(formatedRating);
        else
            book_rating.setText("N/A");

        return listItemView;

    }


    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}

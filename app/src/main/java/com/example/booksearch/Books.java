package com.example.booksearch;

public class Books {

    private String mimage_uri;
    private String mbook_url;
    private String mbook_title;
    private String mbook_author;
    private Double mrating;
    private String mdescription;

    public  Books(String image_uri,String book_title,String book_author,Double rating,String book_url,String description){
        mimage_uri = image_uri;
        mbook_title = book_title;
        mbook_author = book_author;
        mrating=rating;
        mbook_url=book_url;
        mdescription=description;
    }
    public String getMimage_uri() {
        return mimage_uri;
    }
    public String getMbook_title() {
        return mbook_title;
    }
    public String getMbook_author() {
        return mbook_author;
    }

    public Double getMrating() {
        return mrating;
    }
    public String getMbook_url() {
        return mbook_url;
    }
    public String getMdescription() {
        return mdescription;
    }
}

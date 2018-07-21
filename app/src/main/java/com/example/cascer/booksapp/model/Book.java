package com.example.cascer.booksapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private String title, thumbnail, author, desc;
    private int rating;

    public Book(String title, String thumbnail, String author, int rating, String desc) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.author = author;
        this.desc = desc;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getRating() {
        return rating;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", author='" + author + '\'' +
                ", desc='" + desc + '\'' +
                ", rating=" + rating +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.thumbnail);
        dest.writeString(this.author);
        dest.writeString(this.desc);
        dest.writeInt(this.rating);
    }

    protected Book(Parcel in) {
        this.title = in.readString();
        this.thumbnail = in.readString();
        this.author = in.readString();
        this.desc = in.readString();
        this.rating = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}

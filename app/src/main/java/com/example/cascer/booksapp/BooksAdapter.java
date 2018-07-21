package com.example.cascer.booksapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cascer.booksapp.model.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookHolder> {

    private Context context;
    private List<Book> dataSet;

    public BooksAdapter(Context context) {
        this.context = context;
        this.dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = dataSet.get(position);
        Glide.with(context)
                .load(book.getThumbnail())
                .dontAnimate()
                .error(R.drawable.ic_broken_image_black_24dp)
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .centerCrop()
                .fitCenter()
                .into(holder.ivThumbnail);
        holder.tvTitle.setText(book.getTitle());
//        holder.tvRating.setText(String.valueOf(book.getRating()));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void addAll(List<Book> books) {
        if (dataSet != null) {
            for (Book result : books) {
                add(result);
            }
        }
    }

    public void updateList(List<Book> list) {
        if (list.size() != dataSet.size() || !dataSet.containsAll(list)) {
            dataSet = list;
            notifyDataSetChanged();
        }
    }

    public void add(Book postFeed) {
        dataSet.add(postFeed);
        notifyItemInserted(dataSet.size() - 1);
    }

    public Book getItem(int position) {
        return dataSet.get(position);
    }


    void clear() {
        dataSet.clear();
        notifyDataSetChanged();
    }

    class BookHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.tv_title)
        TextView tvTitle;/*
        @BindView(R.id.tv_rating)
        TextView tvRating;
*/

        public BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

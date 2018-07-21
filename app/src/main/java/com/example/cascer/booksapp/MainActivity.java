package com.example.cascer.booksapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.cascer.booksapp.model.Book;
import com.example.cascer.booksapp.utility.GridSpacingItemDecoration;
import com.example.cascer.booksapp.utility.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rv_books)
    RecyclerView rvBooks;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;

    private BooksAdapter adapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());

        bookList = new ArrayList<>();
        adapter = new BooksAdapter(this);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int i, KeyEvent event) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getBooks();
                }
                return false;
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBooks();
            }
        });

        rvBooks.setAdapter(adapter);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvBooks.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        rvBooks.addOnItemTouchListener(new RecyclerTouchListener(this, rvBooks,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        Book book = adapter.getItem(position);
                        intent.putExtra("book", book);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        getBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getBooks() {
        Log.d(TAG, "getBooks is called ");
        bookList.clear();
        adapter.clear();

        String query = etSearch.getText().toString();

        AndroidNetworking.get("https://www.googleapis.com/books/v1/volumes?q={{keyword}}")
                .addPathParameter("keyword", query)
                .setTag("Books")
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {

                            String author = "", thumbnail = "", title = "", desc = "";
                            int rating = 0;
                            if (response.getInt("totalItems") != 0) {
                                JSONArray items = response.getJSONArray("items");
                                Log.d(TAG, "onResponse Item: " + items.toString());
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject book = items.getJSONObject(i);
                                    JSONObject info = book.getJSONObject("volumeInfo");
                                    desc = info.optString("description");
                                    if (desc != null) {
                                        desc = info.optString("description");
                                    } else desc = "This is description";
                                    title = info.getString("title");
                                    JSONArray authors = info.optJSONArray("authors");
                                    if (authors != null) {
                                        for (int j = 0; j < authors.length(); j++) {
                                            author = authors.getString(j).trim();
                                        }
                                    }
                                    JSONObject image = info.optJSONObject("imageLinks");
                                    if (image != null) {
                                        thumbnail = image.getString("smallThumbnail");
                                    }
                                    int ratingCount = info.optInt("ratingsCount");
                                    if (ratingCount != 0) {
                                        rating = info.optInt("ratingsCount");
                                    } else rating = 0;
                                    bookList.add(new Book(title, thumbnail, author, rating, desc));
                                }
                                Log.d(TAG, "onResponse: " + bookList.toString());
                                adapter.updateList(bookList);
                            } else Log.d(TAG, "onResponse not succes!: " + response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,
                                    "Exception terjadi: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse Error: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        String error = "";
                        try {
                            JSONObject o = new JSONObject(anError.getErrorBody());
                            error = o.getString("message");
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = anError.getErrorBody();
                        }
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError Login: " + anError.getErrorDetail() + " / " + anError.getResponse().networkResponse());
                        Log.d(TAG, "onError: " + anError.getErrorBody() + " / " + anError.getErrorCode());
                    }
                });
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

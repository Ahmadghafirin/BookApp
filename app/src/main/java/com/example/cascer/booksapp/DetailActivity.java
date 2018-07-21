package com.example.cascer.booksapp;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cascer.booksapp.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.rating_bar)
    MaterialRatingBar ratingBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        book = getIntent().getParcelableExtra("book");
        Glide.with(this)
                .load(book.getThumbnail())
                .dontAnimate()
                .error(R.drawable.ic_broken_image_black_24dp)
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .fitCenter()
                .into(ivBackdrop);
        initCollapsingToolbar(book.getTitle());
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        ratingBar.setRating(book.getRating());
        tvDesc.setText(book.getDesc());
    }

    private void initCollapsingToolbar(final String title) {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);


        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

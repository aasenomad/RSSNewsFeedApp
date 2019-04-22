package com.example.rssnewsfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.rssnewsfeed.R;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView pubDateTextView = (TextView) findViewById(R.id.pubDateTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView linkTextView = (TextView) findViewById(R.id.linkTextView);

        Intent intent = getIntent();

        String pubDate = intent.getStringExtra("pubdate");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description").replace('\n', ' ');

        pubDateTextView.setText(pubDate);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        linkTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = getIntent();

        String link = intent.getStringExtra("link");
        Uri viewUri = Uri.parse(link);

        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
    }
}

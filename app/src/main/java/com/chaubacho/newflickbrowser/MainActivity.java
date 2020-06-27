package com.chaubacho.newflickbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chaubacho.Const;
import com.chaubacho.control.PhotoDetailActivity;
import com.chaubacho.control.ProcessJsonData;
import com.chaubacho.control.RecyclerViewAdapter;
import com.chaubacho.control.RecyclerViewOnClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.chaubacho.Const.PHOTO_TRANSFER;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListener.OnRecyclerClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String queryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                fetchPhoto("1");
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, recyclerView, this));
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(), this, swipeRefreshLayout);
        recyclerView.setAdapter(recyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        queryResult = sharedPreferences
                .getString(Const.FLICKR_QUERY, "");

        fetchPhoto("1");

    }

    private void fetchPhoto(String page) {
        if (queryResult.length() > 0) {
            new ProcessJsonData(queryResult, page, recyclerViewAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(this, "Long click to open", Toast.LENGTH_SHORT);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, recyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        fetchPhoto("1");
    }
}

package com.corsair.mylibrary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.onCardClickListener{

    private RecyclerView recyclerView;
    private FloatingActionButton add_btn;
    private ArrayList<String> book_id, book_title, book_author, book_pages;
    private DatabaseHelper databaseHelper;
    private Toolbar toolbar;
    private LibraryModel libraryModel;
    private List<LibraryModel> bookList;

    //constants
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The app will not use night mode even if it is turned on by the user
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        libraryModel = new LibraryModel();
        bookList = new ArrayList<>();

        recyclerView = findViewById(R.id.id_recycler_view);
        add_btn = findViewById(R.id.id_floating_action_button);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });

        displayData();
    }

    private List<LibraryModel> displayData(){
        databaseHelper = new DatabaseHelper(this);
        bookList = new ArrayList<>();
        Cursor cursor = databaseHelper.readData();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String author = cursor.getString(2);
            String publisher = cursor.getString(3);
            int pages = cursor.getInt(4);
            int published_year = cursor.getInt(5);
            String genre = cursor.getString(6);
            String summary = cursor.getString(7);
            byte[] image = cursor.getBlob(8);

            LibraryModel libraryModel = new LibraryModel(
                    id,
                    title,
                    author,
                    publisher,
                    pages,
                    published_year,
                    genre,
                    summary,
                    image);
            bookList.add(libraryModel);

            CustomAdapter customAdapter = new CustomAdapter(this, bookList, this::onCardClick);
            recyclerView.setAdapter(customAdapter);
            //recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        databaseHelper.readData().close();
        return bookList;
    }


    @Override
    public void onCardClick(int position) {
        bookList.get(position);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("row_id", bookList.get(position).getId());
        intent.putExtra(getString(R.string.title), bookList.get(position).getBook_title());
        intent.putExtra(getString(R.string.author), bookList.get(position).getBook_author());
        intent.putExtra(getString(R.string.publisher), bookList.get(position).getBook_publisher());
        intent.putExtra(getString(R.string.pages), bookList.get(position).getBook_pages());
        intent.putExtra(getString(R.string.published_year), bookList.get(position).getBook_published_year());
        intent.putExtra(getString(R.string.genre), bookList.get(position).getBook_category());
        intent.putExtra(getString(R.string.summary_intent), bookList.get(position).getBook_summary());
        intent.putExtra(getString(R.string.byte_image), bookList.get(position).getBook_image());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
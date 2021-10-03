package com.corsair.mylibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class AddActivity extends AppCompatActivity {
    //Variables and Views
    private TextInputEditText title_eb, author_eb, publisher_eb, pages_eb, published_year_eb;
    private EditText summary_eb;
    private String spinner_text = "";
    private Spinner spinner;
    private ImageView back_button_iv, save_info_iv, choose_book_image;

    //constant values
    private static final int REQUEST_CODE = 101;
    private static final String LOG_TAG = "Add Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findAllTheViews();//find all the views by their id's. All methods should be called after this method only

        displaySpinnerItems();
        toolbarClickEvents();
        openCamera();

    }

    //Method to find all the views
    private void findAllTheViews(){
        spinner = findViewById(R.id.id_spinner);
        back_button_iv = findViewById(R.id.id_back_button);
        save_info_iv = findViewById(R.id.id_save_info);
        choose_book_image = findViewById(R.id.id_book_image);
        title_eb = findViewById(R.id.id_book_title);
        author_eb = findViewById(R.id.id_book_author);
        publisher_eb = findViewById(R.id.id_book_publisher);
        published_year_eb = findViewById(R.id.id_book_published_year);
        pages_eb = findViewById(R.id.id_book_pages);
        summary_eb = findViewById(R.id.id_book_summary);
    }

    //method to display spinner items
    private void displaySpinnerItems(){
        String[] items = new String[] {"Select Genre", "Romance", "Historical", "Mystery", "Contemporary", "Horror", "Thriller",
                "Fiction", "Cooking", "Art", "Self Help", "Health", "Comedy", "Lifestyle", "Kids", "Travel", "Biography", "Motivational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_text = items[position];

                if(spinner_text == "Select Genre"){
                    spinner_text = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Method to setup Toolbar click events
    private void toolbarClickEvents(){

        back_button_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_info_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will insert/save data into the database
                insertData();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap book_image = (Bitmap) data.getExtras().get("data");
        choose_book_image.setImageBitmap(book_image);

    }

    private void openCamera(){

        choose_book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(open_camera, 1);
                }
                else{
                    ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA},
                            REQUEST_CODE);

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(AddActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertData(){
        LibraryModel libraryModel;
        Bitmap bitmap = ((BitmapDrawable)choose_book_image.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        try{
            libraryModel = new LibraryModel(-1,
                    title_eb.getText().toString(),
                    author_eb.getText().toString(),
                    publisher_eb.getText().toString(),
                    Integer.parseInt(pages_eb.getText().toString()),
                    Integer.parseInt(published_year_eb.getText().toString()),
                    spinner_text,
                    summary_eb.getText().toString(),
                    byteArray);

            DatabaseHelper databaseHelper = new DatabaseHelper(AddActivity.this);
            databaseHelper.addData(libraryModel);
            Toast.makeText(AddActivity.this, "Saved", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(this, "Numeric values required", Toast.LENGTH_SHORT).show();
        }

    }


}
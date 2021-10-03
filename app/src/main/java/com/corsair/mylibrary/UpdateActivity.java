package com.corsair.mylibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class UpdateActivity extends AppCompatActivity {

    private TextInputEditText title_eb, author_eb, publisher_eb, pages_eb, published_year_eb;
    private EditText summary_eb;
    private String spinner_text = "";
    private Spinner spinner;
    private ImageView back_button_iv, save_info_iv, book_iv;
    private static final String TAG = "UpdateActivity";
    String row_id = "";
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        findAllTheViews();
        getAndSetIntentExtras();
        openCamera();
        displaySpinnerItems();
        toolbarClickEvents();

        row_id = getIntent().getExtras().getString("row_id");
    }

    //Method to find all the views
    private void findAllTheViews(){
        spinner = findViewById(R.id.id_spinner);
        back_button_iv = findViewById(R.id.id_back_button);
        save_info_iv = findViewById(R.id.id_save_info);
        book_iv = findViewById(R.id.id_book_image);
        title_eb = findViewById(R.id.id_book_title);
        author_eb = findViewById(R.id.id_book_author);
        publisher_eb = findViewById(R.id.id_book_publisher);
        published_year_eb = findViewById(R.id.id_book_published_year);
        pages_eb = findViewById(R.id.id_book_pages);
        summary_eb = findViewById(R.id.id_book_summary);
    }

    //method to display spinner items
    private void displaySpinnerItems(){
        String[] items = new String[] {spinner_text, "Romance", "Historical", "Mystery", "Contemporary", "Horror", "Thriller",
                "Fiction", "Cooking", "Art", "Self Help", "Health", "Comedy", "Lifestyle", "Kids", "Travel", "Biography", "Motivational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinner_text = items[position];
                for(int i=0; i<items.length; i++){
                    if(spinner_text == items[i]){
                        items[i] = "Select Genre";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAndSetIntentExtras(){
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            title_eb.setText(extras.getString(getString(R.string.title)));
            author_eb.setText(extras.getString(getString(R.string.author)));
            publisher_eb.setText(extras.getString(getString(R.string.publisher)));
            pages_eb.setText(extras.getString(getString(R.string.pages)));
            published_year_eb.setText(extras.getString(getString(R.string.published_year)));
            spinner_text = extras.getString(getString(R.string.genre));
            summary_eb.setText(extras.getString(getString(R.string.summary_intent)));

            //convert byte image to bitmap
            byte[] byteArray = extras.getByteArray(getString(R.string.byte_image));
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            book_iv.setImageBitmap(bitmap);
        }
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
                modifyBookInformation();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void modifyBookInformation(){
        LibraryModel libraryModel;

        Bitmap bitmap = ((BitmapDrawable) book_iv.getDrawable()).getBitmap();
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

            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.updateData(row_id, libraryModel);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(this, "Numeric values required", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera(){
        book_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(open_camera, 1);
                }else{
                    ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        book_iv.setImageBitmap(bitmap);
    }
}
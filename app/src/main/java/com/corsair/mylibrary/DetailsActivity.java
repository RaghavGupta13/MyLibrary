package com.corsair.mylibrary;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {

    private TextView title_tv, author_tv, publisher_tv, pages_tv, published_year_tv;
    private TextView genre_tv, summary_tv;
    private ImageView book_iv;
    private static final String TAG = "DetailsActivity";
    private String row_id = "";
    private DatabaseHelper databaseHelper;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        findAllViews();
        displayIntentData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.id_update:
                updateFields();
                break;
            case R.id.id_delete:
                databaseHelper = new DatabaseHelper(DetailsActivity.this);
                databaseHelper.deleteData(row_id);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    //this method will handle the update click menu option
    private void updateFields(){
        Intent intent = new Intent(DetailsActivity.this, UpdateActivity.class);
        intent.putExtra(getString(R.string.title), title_tv.getText());
        intent.putExtra(getString(R.string.author), author_tv.getText());
        intent.putExtra(getString(R.string.publisher), publisher_tv.getText());
        intent.putExtra(getString(R.string.pages), pages_tv.getText());
        intent.putExtra(getString(R.string.published_year), published_year_tv.getText());
        intent.putExtra(getString(R.string.genre), genre_tv.getText());
        intent.putExtra(getString(R.string.summary_intent), summary_tv.getText());
        intent.putExtra("row_id", row_id);

        //converting bitmap image to byte again
        Bitmap bitmap = ((BitmapDrawable)book_iv.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        intent.putExtra(getString(R.string.byte_image), byteArray);

        startActivity(intent);
    }

    //this method will all the views in the layout
    private void findAllViews(){
        title_tv = findViewById(R.id.id_title_details);
        author_tv = findViewById(R.id.id_author_details);
        publisher_tv = findViewById(R.id.id_publisher_details);
        pages_tv = findViewById(R.id.id_pages_details);
        published_year_tv = findViewById(R.id.id_published_year_details);
        genre_tv = findViewById(R.id.id_genre_details);
        summary_tv = findViewById(R.id.id_summary_text_details);
        book_iv = findViewById(R.id.id_book_image_card_view);
    }

    //this method will take values from the intent and pass it on to the text views
    private void displayIntentData(){
        Bundle extras = getIntent().getExtras();


        try{
            if(extras != null){

                String pages = String.valueOf(extras.getInt(getString(R.string.pages)));
                String published_year = String.valueOf(extras.getInt(getString(R.string.published_year)));
                row_id = String.valueOf(extras.getInt("row_id"));

                byte[] byte_image = extras.getByteArray(getString(R.string.byte_image));
                bitmap = BitmapFactory.decodeByteArray(byte_image, 0, byte_image.length);

                title_tv.setText(extras.getString(getString(R.string.title)));
                author_tv.setText(extras.getString(getString(R.string.author)));
                publisher_tv.setText(extras.getString(getString(R.string.publisher)));
                pages_tv.setText(pages);
                published_year_tv.setText(published_year);
                genre_tv.setText(extras.getString(getString(R.string.genre)));
                summary_tv.setText(extras.getString(getString(R.string.summary_intent)));
                book_iv.setImageBitmap(bitmap);

            }
        }catch (Exception e){
        }
    }

}
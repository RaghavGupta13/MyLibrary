package com.corsair.mylibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Viewholder> {
    private Context context;
    private List<LibraryModel> list;
    private onCardClickListener listener;
    private Bitmap bitmap;

    public CustomAdapter(Context context, List<LibraryModel> list, onCardClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.custom_card_view_layout, parent, false);

        return new Viewholder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.Viewholder holder, int position) {

        final LibraryModel libraryModel = list.get(position);
        holder.title_tv.setText(libraryModel.getBook_title().trim());
        holder.author_tv.setText(libraryModel.getBook_author().trim());

        byte[] getByteArrayFromModel = libraryModel.getBook_image();
        bitmap = BitmapFactory.decodeByteArray(getByteArrayFromModel, 0, getByteArrayFromModel.length);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title_tv, author_tv, pages_tv;
        ImageView imageView;
        onCardClickListener onCardClickListener;

        public Viewholder(@NonNull View itemView, onCardClickListener onCardClickListener) {
            super(itemView);

            title_tv = itemView.findViewById(R.id.id_card_view_book_title);
            author_tv = itemView.findViewById(R.id.id_card_view_book_author);
            imageView = itemView.findViewById(R.id.id_book_image_card_view);
            itemView.setOnClickListener(this);
            this.onCardClickListener = onCardClickListener;
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition());
        }
    }

    public interface onCardClickListener{
        void onCardClick(int position);
    }
}

package com.example.booglesearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class custom_adapter_books_list_view extends ArrayAdapter<booksListData>
{

    public custom_adapter_books_list_view(Context context,List<booksListData> objects)
    {
        super(context,0,objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        booksListData tmp_data = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout_books_list, parent, false);
        }
        TextView book_title = (TextView)convertView.findViewById(R.id.text_view_book_title);
        TextView authors = (TextView)convertView.findViewById(R.id.text_view_author);
        TextView description = (TextView)convertView.findViewById(R.id.text_view_description);
        ImageView smallThumbnail = (ImageView)convertView.findViewById(R.id.image_view_book_thumbnail);

        String title = "";
        if(tmp_data.getTitle() != null) {
            title = tmp_data.getTitle().split(",")[0];
            title = title.split(";")[0];
            title = title.split(",")[0];
        }
        book_title.setText(tmp_data.getTitle());
        authors.setText("by "+tmp_data.getAuthor());
        description.setText(tmp_data.getDescription());
        //Preparing image to display
        Glide.with(getContext()).load(tmp_data.getSmallThumbnail()).centerCrop().into(smallThumbnail);
        booksList.spinner.setVisibility(View.GONE);
        return convertView;
    }
}

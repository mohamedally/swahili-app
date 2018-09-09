package com.example.mohamedally.swahiliapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResourceId;


    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {

        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Create a list item from the recycled list views
        View listItemView = convertView;

        // If no views to convert then create a new list view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the Word object at this position from the list
        Word currentWord = getItem(position);

        // Find the textview in list_item.xml with id swahili_text_view
        TextView miwokTextView = listItemView.findViewById(R.id.swahili_text_view);

        // Set the txt displayed by this textview to be the swahili word
        miwokTextView.setText(currentWord.getSwahiliTranslation());

        TextView defaultTextView = listItemView.findViewById(R.id.default_text_view);

        defaultTextView.setText(currentWord.getDefaultTranslation());

        ImageView iconView = listItemView.findViewById(R.id.image);

        // Handle words which have or dont have an image icon
        if (currentWord.hasImage())  {
            iconView.setImageResource(currentWord.getImageResourceId());
            iconView.setVisibility(View.VISIBLE);
        }
        else{
            iconView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);

        int color = ContextCompat.getColor(getContext(), mColorResourceId);

        textContainer.setBackgroundColor(color);

        return listItemView;


    }
}

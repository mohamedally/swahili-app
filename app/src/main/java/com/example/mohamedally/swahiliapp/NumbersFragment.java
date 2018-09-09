package com.example.mohamedally.swahiliapp;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaplayer;

    /* Initialize the audiomanager object*/
    private AudioManager mAudioManager;

    /* Deal with audio focus change */
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaplayer.pause();
                        mMediaplayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaplayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }

                }
            };

    /* Free memory held up by media player after sound completely plays */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Inflate the word_list view to create the numbers view */
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        /* Request access to audio services */
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);


        /* Initialize an array list to create word objects */
        final ArrayList<Word> words = new ArrayList<>();

        /* Populate the arraylist */
        words.add(new Word("one", "moja", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "mbili", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tatu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "nne", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "tano", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "sita", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "saba", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "nane", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "tisa",R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "kumi", R.drawable.number_ten, R.raw.number_ten));

        /* Create a word adapter and attach it to the word list view */
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        /* Play the respective word when list item is clicked */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                /* Releases memory held by previous word in case new word is played before previous word finished playing */
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music stream
                        AudioManager.STREAM_MUSIC,
                        // Request short period focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus



                    mMediaplayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaplayer.start();

                    mMediaplayer.setOnCompletionListener(mCompletionListener);

                }

            }
        });

        return rootView;

    }

    /* release memory when the fragment is stopped */
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaplayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaplayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaplayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}

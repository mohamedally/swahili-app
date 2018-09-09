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
public class ColorsFragment extends Fragment {

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

        /* Inflate the word_list view to create the colors view */
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        /* Request access to audio services */
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        /* Initialize an array list to create word objects */
        final ArrayList<Word> words = new ArrayList<>();

        /* Populate the arraylist */
        words.add(new Word("red", "nyekundu", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "kijani", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "khaki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "kijivu", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "nyeusi", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "nyeupe", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("yellow", "njano", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        /* Create a word adapter and attach it to the word list view */
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);

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

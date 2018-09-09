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
public class PhrasesFragment extends Fragment {

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

        /* Inflate the word_list view to create the phrases view */
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        /* Request access to audio services */
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        /* Initialize an array list to create word objects */
        final ArrayList<Word> words = new ArrayList<>();

        /* Populate the arraylist */
        words.add(new Word("Where are you going?", "Unaenda wapi?", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "Unaitwa nani?", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is", "Naitwa", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "Hujambo?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "Sijambo", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "Unakuja?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "Ndio, ninakuja", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "Ninakuja", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "Twende", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "Njoo", R.raw.phrase_come_here));

        /* Create a word adapter and attach it to the word list view */
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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

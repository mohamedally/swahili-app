package com.example.mohamedally.swahiliapp;

public class Word {

    /** Default translation for the word */
    private String mDefaultTranslation;

    /** Miwok Translation for the word */
    private String mSwahiliTranslation;

    /** Image resource id for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /** Audio resource id for the word */
    private int mAudioResourceId;

    private static final int NO_IMAGE_PROVIDED = -1;

    /** Word object declaration for words objects without image attribute as in Phrase Fragment words */
    public Word(String defaultTranslation, String swahiliTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mSwahiliTranslation = swahiliTranslation;
        mAudioResourceId = audioResourceId;
    }

    /** Word object declaration for words objects with image attribute */
    public Word(String defaultTranslation, String swahiliTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mSwahiliTranslation = swahiliTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }


    /** Get default translation of the word */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /** Get swahili translation of the word */
    public String getSwahiliTranslation() {
        return mSwahiliTranslation;
    }

    /** Get Image resource ID for the word */
    public int getImageResourceId() {
        return mImageResourceId;

    }

    /** Get Word resource ID for the word */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }


    /** Check whether or not a list item has an image */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }


}

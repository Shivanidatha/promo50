package com.shiv.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    public static final String ORIGINAL_NOTE_COURSE_ID="com.shiv.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE="com.shiv.notekeeper.ORIGINAL_NOTE_COURSE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT="com.shiv.notekeeper.ORIGINAL_NOTE_COURSE_TEXT";

    public String mOriginalNewCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;

    public boolean mIsNewlyCreated=true;


    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalNewCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalNoteText);

    }

    public void restoreState(Bundle inState){

        mOriginalNewCourseId=inState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle=inState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText=inState.getString(ORIGINAL_NOTE_TEXT);

    }


}

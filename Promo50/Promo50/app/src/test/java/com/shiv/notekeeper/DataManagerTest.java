package com.shiv.notekeeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    @Test
    public void createNewNote() {

        DataManager dm = DataManager.getInstance();
        final CourseInfo course= dm.getCourse("android_async");
        final String noteTitle="Note Title";
        final String noteText="This is the body of my text";

        int noteIndex= dm.createNewNote();

    }
}
package com.shiv.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {


    private final String TAG = getClass().getSimpleName();
    public static final String NOTE_POSITION ="com.shiv.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNoote;
    private Spinner mSpincourse;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNewPosition;
    private boolean mIsCancelling;

    private NoteActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider=new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel=viewModelProvider.get(NoteActivityViewModel.class);

        if(savedInstanceState!=null && mViewModel.mIsNewlyCreated)
            mViewModel.restoreState(savedInstanceState);

        mViewModel.mIsNewlyCreated=false;

        mSpincourse = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adaptercourses= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, courses);
        adaptercourses.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpincourse.setAdapter(adaptercourses);

        readDisplayStateValues();
        saveOriginalVAlues();

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);
        if(!mIsNewNoote)
        displayNote(mSpincourse, mTextNoteTitle, mTextNoteText);


        Log.d(TAG, "onCreate");
    }

    private void saveOriginalVAlues() {
        if(mIsNewNoote)
            return;
        mViewModel.mOriginalNewCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState!=null)
            mViewModel.saveState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            Log.i(TAG,"Cancelling at position"+mNewPosition);
            if(mIsNewNoote) {
                DataManager.getInstance().removeNote(mNewPosition);
            }else{
                storePreviousNoteValues();
            }
        }else {
            saveNote();
        }
        Log.d(TAG,"onPause");
    }

    private void storePreviousNoteValues() {
        CourseInfo course=DataManager.getInstance().getCourse(mViewModel.mOriginalNewCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo)mSpincourse.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void displayNote(Spinner spincourse, EditText textNoteTitle, EditText textNoteText) {

        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        int courseIndex=courses.indexOf(mNote.getCourse());
        spincourse.setSelection(courseIndex);

        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent=getIntent();
        mNewPosition =intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);

        mIsNewNoote = mNewPosition == POSITION_NOT_SET;

        if(mIsNewNoote){
            createNewNote();

        }

        Log.i(TAG,"mNotePosition:"+mNewPosition);
        mNote = DataManager.getInstance().getNotes().get(mNewPosition);


    }

    private void createNewNote() {
        DataManager dm= DataManager.getInstance();
        mNewPosition = dm.createNewNote();
      //  mNote=dm.getNotes().get(mNewPosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }
        else if(id==R.id.action_cancel){
            mIsCancelling = true;
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo course= (CourseInfo) mSpincourse.getSelectedItem();
        String subject= mTextNoteTitle.getText().toString();
        String sss="Hello I'm interested in your voucher. Could you send your account?";
        String  text="Hello I'm interested in your voucher. Could you send your account? ";
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,sss);
       // intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);

    }
}

package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {

    int noteid = -1;
    EditText mNote;
    Button saveNote;

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mNote=findViewById(R.id.mNote);
        saveNote=findViewById(R.id.saveNote);

        sqLiteDatabase = getApplicationContext().openOrCreateDatabase( "notes" , Context.MODE_PRIVATE, null );
        dbHelper=new DBHelper(sqLiteDatabase);

        Intent intent = getIntent();
        noteid = intent.getIntExtra("noteid",-1);
        if(noteid != -1){
            Note note = MainActivity2.notes.get(noteid);
            mNote.setText(note.getContent()+"");
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMethod(v);
                Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void saveMethod(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String title="";
        String content = mNote.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (noteid == -1){
            title = "NOTE_" + (MainActivity2.notes.size()+1);
            dbHelper.saveNotes(username, title, content, date);
        }else{
            title = "NOTE_" +(noteid + 1);
            dbHelper.updateNote(title, date, content, username);
        }
    }
}
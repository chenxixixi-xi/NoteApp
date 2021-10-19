package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    TextView textView;
    public static ArrayList<Note> notes = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView =findViewById(R.id.textView);
        Intent intent = getIntent();
        name = intent.getStringExtra("message");
        textView.setText("Welcome " + name + "!");

        getNotes();
    }
    public void getNotes(){
        sqLiteDatabase = getApplicationContext().openOrCreateDatabase( "notes" , Context.MODE_PRIVATE, null );
        dbHelper=new DBHelper(sqLiteDatabase);
        notes=dbHelper.readNotes(name);
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Note note : notes){
            displayNotes.add(String.format("Title:%s\nDate:%s", note.getTitle(),note.getDate()));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        adapter.notifyDataSetChanged();
        ListView listView = findViewById(R.id.notesListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                intent.putExtra("noteid", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNotes();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Logout) {
            Intent intent = new Intent(this, MainActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(MainActivity.usernameKey).apply();
            startActivity(intent);
            finish();
            return true;
        }else if(item.getItemId()==R.id.AddNote){
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
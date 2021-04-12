package com.example.rememberme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private GridView gridview1;
    SearchView searchView;
    EditText editText;
    GridAdapter adapter;
    DatabaseActivity dbActivity;
    // Create a new Array of type HashMap
    private ArrayList<HashMap<String, String>> maplist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ActivityEditor.class);
                startActivity(i);
            }
        });

        gridview1 = findViewById(R.id.mainGridView1);
        dbActivity = new DatabaseActivity(MainActivity.this);
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;

                return false;
            }
        });


        // Add items to the Map list
        /*{
            HashMap<String, String> item = new HashMap<>();
            item.put("title", "This is details of the some text which is display over here");
            item.put("icon", "05-May-20 09:30");
            maplist.add(item);
        }*/
        Cursor cursor = dbActivity.selectAllNotes();
        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("id", cursor.getString(0).toString());
            item.put("title", cursor.getString(1).toString());
            item.put("icon", cursor.getString(2).toString());
            maplist.add(item);
        }


        final GridAdapter adapter = new GridAdapter(this, maplist);
        gridview1.setAdapter(adapter);

        gridview1.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                //   Toast.makeText(getApplicationContext(), maplist.get(p3).get("title").toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, ActivityEditor.class);
                i.putExtra("id", maplist.get(p3).get("id").toString());
                i.putExtra("title", maplist.get(p3).get("title").toString());
                startActivity(i);
            }
        });
        gridview1.setLongClickable(true);
        gridview1.setOnItemLongClickListener(new GridView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dbActivity.deleteNotes(maplist.get(position).get("id").toString());
                                //  Intent i = new Intent(MainActivity.this,MainActivity.class);
                                //  startActivity(i);
                                adapter.clearAdapter(); // clear old values
                                adapter.addNewValues(maplist);
                                Cursor cursor = dbActivity.selectAllNotes();
                                while (cursor.moveToNext()) {
                                    HashMap<String, String> item = new HashMap<>();
                                    item.put("id", cursor.getString(0).toString());
                                    item.put("title", cursor.getString(1).toString());
                                    item.put("icon", cursor.getString(2).toString());
                                    maplist.add(item);
                                }
                                final GridAdapter adapter = new GridAdapter(MainActivity.this, maplist);
                                gridview1.setAdapter(adapter);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
    }

/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/


    //Twice backed pressed to exist
    private boolean pressTwice;

    @Override
    public void onBackPressed() {

        if (pressTwice == true) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        pressTwice = true;
        Toast.makeText(this, "Press again to exits", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                pressTwice = false;
            }
        }, 3000);
    }


}

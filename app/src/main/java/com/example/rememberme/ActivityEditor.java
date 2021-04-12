package com.example.rememberme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityEditor extends AppCompatActivity {

    EditText edtTxtPara;
    DatabaseActivity dbActivity;
    Date date;
    Intent iin= getIntent();
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        exitApplication();
        edtTxtPara = findViewById(R.id.editValue);
        dbActivity = new DatabaseActivity(ActivityEditor.this);
        date = new Date();
        b = getIntent().getExtras();
        if(b!=null)
        {
            String j =(String) b.get("title");
            edtTxtPara.setText(j);
        }
    }

    private void savepdf() {
        Document doc = new Document();
        String mfile = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mfilepath = Environment.getExternalStorageDirectory() + "/" + mfile + ".pdf";
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.DEFAULTSIZE);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(mfilepath));
            doc.open();
            String mtext = edtTxtPara.getText().toString();
            doc.addAuthor("Remember Me");
            doc.add(new Paragraph(mtext, smallBold));
            doc.close();
            Toast.makeText(this, "" + mfile + ".pdf" + " is saved to " + mfilepath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "This is Error msg : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void shareMe() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, edtTxtPara.getText().toString());
        startActivity(Intent.createChooser(intent, "Share the content with"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.generatePDF) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] parmission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(parmission, 1000);
                } else savepdf();
            } else savepdf();
            return true;
        }
        if (id == R.id.share) {

            shareMe();
            return true;
        }
        if (id == R.id.action_save) {
            String note_id =null;
            if(b==null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm");
                dbActivity.insertNotes(edtTxtPara.getText().toString(), formatter.format(date));

                Intent i = new Intent(ActivityEditor.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }else{
                note_id = (String) b.get("id");
                dbActivity.UpdateNotes(note_id,edtTxtPara.getText().toString());
                Intent i = new Intent(ActivityEditor.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }


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

        String note_id =null;
        if(b==null) {
            if(edtTxtPara.getText().toString()!=null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm");
                dbActivity.insertNotes(edtTxtPara.getText().toString(), formatter.format(date));
                Intent i = new Intent(ActivityEditor.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }
        }else{
            note_id = (String) b.get("id");
            if(edtTxtPara.getText().toString().length()!= 0) {
                dbActivity.UpdateNotes(note_id, edtTxtPara.getText().toString());
                Intent i = new Intent(ActivityEditor.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }else{
                dbActivity.deleteNotes(note_id);
                Intent i = new Intent(ActivityEditor.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                pressTwice = true;
            }
        }, 3000);
    }


private void exitApplication(){
    if (getIntent().getBooleanExtra("EXIT", false)) {
        finish();
    }
}
}

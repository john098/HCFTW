package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Compose extends Activity {
    Button cancel;
    Button send;
    String toString;
    String subjString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        EditText toField = (EditText) findViewById(R.id.toTextBox);
        EditText subjField = (EditText) findViewById(R.id.subjTextBox);
        if(i.hasExtra("Name")) {
            toString = i.getStringExtra("Name");
            toField.setText(toString);
            toField.setSelection(toString.length());
            subjString = i.getStringExtra("Subject");
            subjField.setText("Re: " + subjString);
        }
        cancel = (Button) findViewById(R.id.cancelButton);
        //check for compose click
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        send = (Button) findViewById(R.id.sendButton);
        //check for compose click
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast t = Toast.makeText(getApplicationContext(),"Sent!",Toast.LENGTH_SHORT);
                t.show();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

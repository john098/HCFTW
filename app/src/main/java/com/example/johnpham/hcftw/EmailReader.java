package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.StringTokenizer;


public class EmailReader extends Activity {
    //String email;
    String inbox;
    String toString;
    String fromString;
    String subjString;
    String message;
    String dater;
    String emailAddress;
    TextView to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_email_reader);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        i.getStringArrayListExtra("List");
        dater = i.getStringExtra("Date");
        emailAddress = i.getStringExtra("Email");
        toString = i.getStringExtra("Email");
        fromString = i.getStringExtra("From");
        if(fromString.equals("Unknown")) {
        fromString = emailAddress;
        }
        subjString = i.getStringExtra("Subject");
        message = i.getStringExtra("Body");
        //toString = i.getStringExtra("to");
        //StringTokenizer test = new StringTokenizer(email,"\n");
        //inbox = i.getStringExtra("Inbox");
        to = (TextView) findViewById(R.id.toName);
        TextView inboxView = (TextView) findViewById(R.id.inboxReader);
        TextView date = (TextView) findViewById(R.id.dateText);
        Button reply = (Button) findViewById(R.id.replyButton);
        Button move = (Button) findViewById(R.id.moveButton);
        //editable from email String
        TextView from = (TextView) findViewById(R.id.fromName);
        TextView subj = (TextView) findViewById(R.id.subjText);
        TextView mess = (TextView) findViewById(R.id.messageText);
        //setting the values
        inboxView.setText(inbox);
        to.setText(toString);
        date.setText(dater);
        from.setText(fromString);
        subj.setText(subjString);
        mess.setText(message);
        //listener
        reply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Compose.class);
                i.putExtra("Name",emailAddress);
                i.putExtra("Subject",subjString);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.email_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

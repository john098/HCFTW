package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
    String email;
    String inbox;
    String fromString;
    String subjString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reader);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        email = i.getStringExtra("Email");
        StringTokenizer test = new StringTokenizer(email,"\n");
        inbox = i.getStringExtra("Inbox");
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
        date.setText(new Date().toString());
        fromString = test.nextToken();
        from.setText(fromString);
        subjString = test.nextToken();
        subj.setText(subjString);
        mess.setText(test.nextToken());
        //listener
        /*reply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            //    Intent i = new Intent(getApplicationContext(),Compose.class);
                i.putExtra("Name",fromString);
                i.putExtra("Subject",subjString);
                startActivity(i);
            }
        });*/


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

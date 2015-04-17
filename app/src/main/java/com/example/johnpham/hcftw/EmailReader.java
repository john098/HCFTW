package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.scheme.HostNameResolver;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;


public class EmailReader extends Activity {
    //String email;
    boolean enabled;
    String inbox;
    List<String> toString;
    String fromString;
    String subjString;
    String message;
    String dater;
    String emailAddress;
    //WebView mess;
    WebView mess2;
    ScrollView test;
    TextView to;
    int offset;
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
        toString = i.getStringArrayListExtra("recipients");
        fromString = i.getStringExtra("From");
        if(fromString.equals("Unknown")) {
        fromString = emailAddress;
        }
        subjString = i.getStringExtra("Subject");
        message = i.getStringExtra("Body");
        enabled = i.getBooleanExtra("Html",false);
        to = (TextView) findViewById(R.id.toName);
        TextView inboxView = (TextView) findViewById(R.id.inboxReader);
        TextView date = (TextView) findViewById(R.id.dateText);
        Button reply = (Button) findViewById(R.id.replyButton);
        Button move = (Button) findViewById(R.id.moveButton);
        //editable from email String
        TextView from = (TextView) findViewById(R.id.fromName);
        TextView subj = (TextView) findViewById(R.id.subjText);
        mess2 = (WebView) findViewById(R.id.messageText2);
        mess2.setVisibility(View.VISIBLE);
        mess2.loadData(message,"text/html",null);
        //setting the values

        inboxView.setText(inbox);
        StringBuilder names = new StringBuilder();
        for(int j=0;j<toString.size();j++){
            if(j<toString.size()-1) {
                names.append(toString.get(j) + ",");
            }else {
                names.append(toString.get(j));
            }
        }
        to.setText(names);
        date.setText(dater);
        from.setText(fromString);
        subj.setText(subjString);
        //listener
        reply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Compose.class);
                i.putExtra("Name",emailAddress);
                i.putExtra("Subject",subjString);
                startActivity(i);
            }
        });
         move.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 Toast.makeText(EmailReader.this, "NYI", Toast.LENGTH_SHORT).show();
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

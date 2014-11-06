package com.example.johnpham.hcftw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Email extends Activity {
    private ListView mainListView;
    private TextView text;
    private Button compose, select;
    private ArrayAdapter<String> listAdapter;
    private String inbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        //definitions
        mainListView = (ListView) findViewById(R.id.mainListView);
        text = (TextView) findViewById(R.id.newMess);
        compose = (Button) findViewById(R.id.sendButton);
        select = (Button) findViewById(R.id.selectButton);
        final ArrayList<String> emailList = new ArrayList<String>();

        //populating with fake emails for now
        for(int i=1;i<=9;i++) {
            emailList.add("meneghelloj@yahoo.com\nHello!\nHow are you doing?");
        }
        inbox="Inbox(" +emailList.size()+")";
        text.setText(inbox); //updating the Inbox name
        listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow,emailList);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 /*//Log.d("Success!", "Yeah! Item " + id + " worked!");
                emailList.remove(position);
                listAdapter.notifyDataSetChanged();
                text.setText("Inbox(" +emailList.size()+")");
                if(emailList.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),"No more emails!",Toast.LENGTH_SHORT);
                    toast.show();
                }*/

                Intent i = new Intent(getApplicationContext(),EmailReader.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("Email",emailList.get(position));
                i.putExtra("Inbox",inbox);
                startActivity(i);
            }
        });
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("Success!", "Yeah! Item " + id + " worked!");
                emailList.remove(position);
                Toast toast = Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT);
                toast.show();
                listAdapter.notifyDataSetChanged();
                inbox="Inbox(" +emailList.size()+")";
                text.setText(inbox);
                if(emailList.isEmpty()) {
                    Toast toaster = Toast.makeText(getApplicationContext(),"No more emails!",Toast.LENGTH_SHORT);
                    toaster.show();
                }
                return true;
            }
        });


        //check for compose click
        compose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Compose.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Select things
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
       /* if (id == R.id.logout){
            Intent intent=new Intent(getApplicationContext(),Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}

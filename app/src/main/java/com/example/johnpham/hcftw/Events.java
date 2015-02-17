package com.example.johnpham.hcftw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.microsoft.outlookservices.Calendar;

import java.util.ArrayList;
import java.util.List;


public class Events extends Activity {
private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Button j=(Button)findViewById(R.id.newEvent);
        TextView k=(TextView)findViewById(R.id.event);
        Bundle get=getIntent().getExtras();
        Calendar cal;
        //cal.getCalendarView();

        list = (ListView) findViewById(R.id.lost);

        ArrayList<String> array=new ArrayList<String>();
      //  Calendar v=Calendar.getCalenderView();

        Calendar ca=new Calendar();

        array.add( ca.getId().toString());
        ArrayAdapter<String> myAdapter=new
                ArrayAdapter <String>(
                this,
                android.R.layout.simple_list_item_1,
                array);
        list.setAdapter(myAdapter);
      /*  if(get!=null)
        {
            String date=get.getString("variable");
            k.setText(date);
        }*/
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), NewNotes.class));
            }
         });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

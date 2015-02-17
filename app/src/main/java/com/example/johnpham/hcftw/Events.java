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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Calendar;
import com.microsoft.outlookservices.CalendarGroup;
import com.microsoft.outlookservices.ODataBaseEntity;
import com.microsoft.outlookservices.Item;
import com.microsoft.outlookservices.Event;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;


import java.util.ArrayList;
import java.util.List;


public class Events extends Activity {

private ListView list;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_events);
        Button j=(Button)findViewById(R.id.newEvent);
        TextView k=(TextView)findViewById(R.id.event);
        Bundle get=getIntent().getExtras();
        String dat=get.getString("variable");
        Calendar cal;


        //cal.getCalendarView();
        OutlookClient client= new OutlookClient(ServiceConstants.ENDPOINT_ID,(DefaultDependencyResolver)Controller.getInstance().getDependencyResolver());

        list = (ListView) findViewById(R.id.lost);

        ArrayList<String> array=new ArrayList<String>();
        ArrayList<Event>s=new ArrayList<Event>();
      //  Calendar v=Calendar.getCalenderView();

        ListenableFuture<List<Event>> even=  client.getMe().getCalendar().getEvents().read();

        try{

            List<Event> events=even.get();
            for(Event e: events)
            {
               // client.getMe().getCalendar().getEvents().getById(e.getId());
                //s.add(e);
              //  Log.d("my time", date.toString());
            //    Log.d("office time", e.getStart().getTime().toString());
              //  if((e.getStart().getTime().toString().equals(dat)) {
                String a = e.getSubject() + " " + e.getBodyPreview() + "\n" + e.getStart().getTime().toString()+" \n"+dat;
                array.add(a);
                //  }
            }

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
        }catch(Exception e)
        {

        }


        //even.add((Event)client.getMe().getCalendar().getEvent(events.getId()).getCalendar().getEvents());

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

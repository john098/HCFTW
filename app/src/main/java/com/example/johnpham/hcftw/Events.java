package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import java.util.Date;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Event;
import com.microsoft.outlookservices.odata.EventFetcher;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;
import com.microsoft.outlookservices.BodyType;
import java.util.ArrayList;



public class Events extends Activity {

private ListView list;
    private PopupWindow popUp;
    private List<Event> events=null;
    private ArrayList<Event> arrayEvent=new ArrayList<Event>();
    private ArrayList<String> body=new ArrayList<String>();
    private ListenableFuture<List<Event>> even=null;
    private BodyType typ=BodyType.Text;
private  ArrayAdapter<String> myAdapter;
    private  ArrayList<String> array=new ArrayList<String>();
   private ArrayList<String> id=new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_events);
        Button j=(Button)findViewById(R.id.newEvent);
        TextView k=(TextView)findViewById(R.id.event);
        Bundle get=getIntent().getExtras();
        String dat=get.getString("variable");
        //Calendar cal;
        array=get.getStringArrayList("title");
        body=get.getStringArrayList("body");

        list = (ListView) findViewById(R.id.lost);
        ArrayList<Event>s=new ArrayList<Event>();
      //  Calendar v=Calendar.getCalenderView();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            String date;
            final Singleton singleton=Singleton.getInstance();

            events = singleton.getEvent();
            for(Event e: events)
            {
                e.getBody().setContentType(typ);
                if(!e.getIsAllDay()) {
                    date = sdf.format(e.getStart().getTime());
                    if (date.compareTo(dat) == 0) {
                        String a = e.getSubject() + "\n";
                        array.add(a);
                        body.add(e.getBody().getContent());
                        arrayEvent.add(e);
                        continue;
                    }
                }
                    date = sdf.format(e.getEnd().getTime());
                    if (date.compareTo(dat) == 0) {
                        String a = e.getSubject() + "\n" ;
                        array.add(a);
                        body.add(e.getBody().getContent());
                        arrayEvent.add(e);
                    }
            }

            myAdapter=new
                    ArrayAdapter <String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    array);
            list.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
           list.setFocusable(false);
            list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // if(position==1)
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View layout = inflater.inflate(R.layout.popup,
                            (ViewGroup) findViewById(R.id.PopLayoutID));
                    popUp=new PopupWindow(layout,1000,1200,true);
                    popUp.showAtLocation(layout,Gravity.CENTER,0,0);
                    popUp.setFocusable(true);
                    Button PopCan=(Button)layout.findViewById(R.id.PopupButton);
                    TextView tex=(TextView)layout.findViewById(R.id.popupText);
                    Button Del=(Button)layout.findViewById(R.id.DeleteButton);
                    ListView popList=(ListView)layout.findViewById(R.id.PopupList);
                    ArrayList<String> showThis=new ArrayList<String>();
                    showThis.add("Title:     "+arrayEvent.get(position).getSubject());
                    showThis.add("Time:       "+arrayEvent.get(position).getDateTimeCreated().getTime());
                    showThis.add("Body");
                    ArrayAdapter<String> popUpAdapter=new ArrayAdapter<String> (
                            Events.this,
                            android.R.layout.simple_list_item_1,
                            showThis);
                    popUpAdapter.notifyDataSetChanged();
                    popList.setAdapter(popUpAdapter);
                    tex.setText(arrayEvent.get(position).getBodyPreview());

                    final int w=position;
                    Del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Event> remove=new ArrayList<Event>();
                            String id=arrayEvent.get(w).getId();
                         OutlookClient cl=  singleton.getClient();
                            cl.getMe().getEvents().getById(id).delete();

                            for(Event a: events)
                            {
                              if(arrayEvent.get(w).getId()==a.getId())
                              {
                                  remove.add(a);
                              }
                          }
                            events.removeAll(remove);
                           singleton.setEvent(events);
                            arrayEvent.remove(w);
                            array.remove(w);
                            change();
                            popUp.dismiss();
                        }
                    });
                   // tex.setText(body.get(position));

                    try {
                        PopCan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                popUp.dismiss();
                            }
                        });
                        Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e)
                    {
                        Log.d(Events.class.getSimpleName(),"eerrrrro",e);
                    }

                }


            });
      /*  if(get!=null)
        {
            String date=get.getString("variable");
            k.setText(date);
        }*/
            j.setFocusable(false);
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

    public void change()
    {
        myAdapter=new
                ArrayAdapter <String>(
                this,
                android.R.layout.simple_list_item_1,
                array);
        list.setAdapter(myAdapter);
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

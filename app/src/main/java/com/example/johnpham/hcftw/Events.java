package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import java.text.SimpleDateFormat;
import android.widget.PopupWindow;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Attendee;
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
    private Singleton singleton=Singleton.getInstance();
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_events);
        Calendar cal=Calendar.getInstance();

        bar.setTitle("Events   " + singleton.getTodayDate());
        Button j=(Button)findViewById(R.id.newEvent);
        TextView k=(TextView)findViewById(R.id.event);
        Bundle get=getIntent().getExtras();
//        final String dat=get.getString("variable");
        list = (ListView) findViewById(R.id.lost);
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            String date;
            String dat=singleton.getTodayDate();
            events = singleton.getEvent();
            for(Event e: events)
            {
                e.getBody().setContentType(typ);
                Log.d("Event",events.size()+"");
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

            Display(array);
           list.setFocusable(false);
            list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // if(position==1)
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    SimpleDateFormat happy = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                    View layout = inflater.inflate(R.layout.popup,
                            (ViewGroup) findViewById(R.id.PopLayoutID));
                    Display dis = getWindowManager().getDefaultDisplay();
                    int widthSize = dis.getWidth();
                    int hight=dis.getHeight();
                    popUp=new PopupWindow(layout,1200,1000,true);
                    popUp.showAtLocation(layout,Gravity.CENTER,0,0);
                    popUp.setFocusable(true);
                    Button PopCan=(Button)layout.findViewById(R.id.PopupButton);
                    TextView tex=(TextView)layout.findViewById(R.id.popupText);
                    Button Del=(Button)layout.findViewById(R.id.DeleteButton);
                    ListView popList=(ListView)layout.findViewById(R.id.PopupList);
                    ArrayList<String> showThis=new ArrayList<String>();
                    showThis.add("Title:     "+arrayEvent.get(position).getSubject());
                    showThis.add("Time:       "+happy.format(arrayEvent.get(position).getStart().getTime())
                            +" - "+happy.format(arrayEvent.get(position).getEnd().getTime()));
                    List<Attendee> atten=arrayEvent.get(position).getAttendees();
                    ArrayList<String> tester = new ArrayList<String>();
                    for(int i=0;i<atten.size();i++) {
                        tester.add(atten.get(i).getEmailAddress().getName());

                    }
                    if(atten.size()<0)
                    {
                        tester.add("");
                    }


                    showThis.add("Attendees    " + tester.toString());
                    showThis.add("Location:     "+arrayEvent.get(position).getLocation().getDisplayName());
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
                            Display(array);
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
                      //  Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e)
                    {
                        Log.d(Events.class.getSimpleName(),"eerrrrro",e);
                    }

                }


            });
            j.setFocusable(false);
           j.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(getApplicationContext(),NewNotes.class);
                    i.putExtra("date", singleton.getTodayDate());
                    startActivity(i);
                }
            });
        }catch(Exception e)
        {

        }


        //even.add((Event)client.getMe().getCalendar().getEvent(events.getId()).getCalendar().getEvents());

    }

    public void Display(ArrayList<String> array)
    {
        myAdapter=new
                ArrayAdapter <String>(
                this,
                android.R.layout.simple_list_item_1,
                array);
        list.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
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
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),Calender_.class);
        startActivity(i);
        finish();
    }
}

package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import com.google.common.util.concurrent.ListenableFuture;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Intent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.Event;
import com.microsoft.outlookservices.ItemBody;
import com.microsoft.outlookservices.Location;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.sharepointservices.OfficeClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class NewNotes extends Activity {
    private AutoCompleteTextView text;
    private AutoCompleteTextView enter;
    private TextView b;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private TextView d;
    private EditText note;
    private EditText location;
    private Singleton singleton=Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));


        setContentView(R.layout.activity_new_notes);
        /*Display dis = getWindowManager().getDefaultDisplay();
        int height=dis.getHeight();
        int repect=height/6;
        TextView heightdate=(TextView)findViewById(R.id.dateText);
        heightdate.getLayoutParams().height=repect;
        TextView heightTitle=(TextView)findViewById(R.id.textView3);
        heightTitle.getLayoutParams().height=repect;
        TextView heightLocation=(TextView)findViewById(R.id.Locationtext);
        heightLocation.getLayoutParams().height=repect;
        TextView heightAtten=(TextView)findViewById(R.id.textAtten);
        heightAtten.getLayoutParams().height=repect;
        TextView heightStart=(TextView)findViewById(R.id.textView5);
        heightStart.getLayoutParams().height=repect;
        TextView heightStartTime=(TextView)findViewById(R.id.timed);
        heightStartTime.getLayoutParams().height=repect;
*/      location = (EditText) findViewById(R.id.editLocation);




        text=(AutoCompleteTextView)findViewById(R.id.Date);
        Calendar cal=Calendar.getInstance();
        Intent i = getIntent();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(cal.getTime());

         SimpleDateFormat sp=new SimpleDateFormat(("MM/dd/yyyy"));
        Date dated=new Date();
        String datename = singleton.getTodayDate();
        enter=(AutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView);
        if(i.hasExtra("date")) {

            //text.setKeyListener(null);
            try {
                dated = df.parse(datename);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            text.setText(sp.format(dated));
        }
        final Date useThis = dated;
        Button clear=(Button)findViewById(R.id.Clear);
        note=(EditText)findViewById(R.id.editText);
        b=(TextView)findViewById(R.id.timed);
        d=(TextView)findViewById(R.id.timed1);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setText("");
                b.setText("");
                d.setText("");
                note.setText("");
            }
        });

        final Button t=(Button)findViewById(R.id.time);
        Button time=(Button)findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        final Button t1=(Button)findViewById(R.id.time1);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call1();
            }
        });
        Button create=(Button)findViewById(R.id.Create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;
                if (enter.getText().toString().equals("")) {
                    enter.setError("Please enter title");
                    error=true;
                }
                if (b.getText().toString().equals("")) {
                    t.setError(("Please select the time"));
                    error=true;
                }
                if (d.getText().toString().equals("")) {
                    t1.setError("Please select the time");
                    error=true;
                }
                if(error==true) {
                    return;
                }
                    SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");
                    Date base = new Date();
                try {
                    base = (Date) df.parse(text.getText().toString());
                } catch (ParseException e) {
                     text.setError("please enter correct MM/DD/YYY");
                    e.printStackTrace();
                }
                finish();
                    df.format(base);
                    final Singleton singleton = Singleton.getInstance();
                    Calendar start = Calendar.getInstance();
                    start.setTime(base);
                    Calendar end = df.getCalendar();
                    end.setTime(base);
                    start.add(Calendar.HOUR_OF_DAY,startHour);
                    start.add(Calendar.MINUTE,startMin);
                    end.add(Calendar.HOUR_OF_DAY,endHour);
                    end.add(Calendar.MINUTE,endMin);
                    Event e = new Event();
                    ItemBody itemBody = new ItemBody();
                    itemBody.setContent(note.getText().toString());
                    UUID id = java.util.UUID.randomUUID();
                    e.setId(id.toString());
                    e.setBody(itemBody);
                    Location theLocation = new Location();
                    theLocation.setDisplayName(location.getText().toString());
                    e.setLocation(theLocation);
                    e.setSubject(enter.getText().toString());
                    e.setStart(start);
                    e.setEnd(end);
                    List<Event>n=singleton.getEvent();
             //   n.addAll(e);
                singleton.setEvent(n);
                    ListenableFuture<Event> send = singleton.getClient().getMe().getCalendar().getEvents().add(e);
                    Futures.addCallback(send, new FutureCallback<Event>() {
                        @Override
                        public void onSuccess(Event result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            Intent i = new Intent(getApplicationContext(), Calender_.class);
                            Singleton.getInstance().refresh();
                            startActivity(i);
                            finish();

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Controller.getInstance().handleError(NewNotes.this, t.getMessage());
                        }
                    });

            }
        });
    }
    public void call()
    {
        final Calendar c = Calendar.getInstance();
        int mHour = 0;
        int mMinute =0;
        TimePickerDialog n = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String am_pm = "";
                        startHour=hourOfDay;
                        if(hourOfDay>=12) {
                            hourOfDay = hourOfDay - 12;
                            am_pm="PM";
                        }
                        else
                        {
                            am_pm="AM";
                        }
                        if(hourOfDay==0)
                            hourOfDay=hourOfDay+12;
                        b.setText(hourOfDay + ":" + String.format("%02d",minute) +  "  "+am_pm);

                        startMin=minute;
                    }
                }, mHour, mMinute, false);
        n.show();
    }
    public void call1()
    {
        final  Calendar c = Calendar.getInstance();
        int mHour = 0;
        int mMinute = 0;
        TimePickerDialog n = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String am_pm = "PM";
                        endHour=hourOfDay;

                        if(hourOfDay>=12) {
                            am_pm="PM";
                            hourOfDay = hourOfDay - 12;
                        }
                        else
                        {
                            am_pm="AM";
                        }
                        if(hourOfDay==0)
                            hourOfDay=hourOfDay+12;
                        d.setText(hourOfDay + ":" + String.format("%02d",minute)+  "  "+am_pm);

                        endMin=minute;
                    }
                }, mHour, mMinute, false);
        n.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_notes, menu);
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

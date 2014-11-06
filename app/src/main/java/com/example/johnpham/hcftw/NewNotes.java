package com.example.johnpham.hcftw;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;

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

public class NewNotes extends Activity {
private AutoCompleteTextView text;
    private AutoCompleteTextView enter;
    private TextView b;
    private TextView d;
    private EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);
        text=(AutoCompleteTextView)findViewById(R.id.Date);
        Calendar cal=Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String Date = df.format(cal.getTime());

        SimpleDateFormat sp=new SimpleDateFormat(("EEEE"));
        Date dated=new Date();
        String datename=sp.format(dated);
        enter=(AutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView);
        text.setText(datename+"  " +Date);

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
        Button t1=(Button)findViewById(R.id.time1);
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

                if(enter.getText().toString().equals(""))
                {
                    enter.setError("Please enter title");
                }


            }
        });
    }
public void call()
{
    final Calendar c = Calendar.getInstance();
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);
    TimePickerDialog n = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    String am_pm = "";

                    if(hourOfDay>12) {
                        hourOfDay = hourOfDay - 12;
                        am_pm="PM";
                    }
                    else
                    {
                        am_pm="AM";
                    }
                    if(hourOfDay==0)
                            hourOfDay=hourOfDay+12;
                    b.setText(hourOfDay + ":" + minute+  "  "+am_pm);
                }
            }, mHour, mMinute, false);

    n.show();
}
    public void call1()
    {
       final  Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog n = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String am_pm = "PM";


                        if(hourOfDay>12) {
                            am_pm="PM";
                            hourOfDay = hourOfDay - 12;

                        }
                        else
                        {
                            am_pm="AM";
                        }
                        if(hourOfDay==0)
                            hourOfDay=hourOfDay+12;
                         d.setText(hourOfDay + ":" + minute+  "  "+am_pm);

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
}

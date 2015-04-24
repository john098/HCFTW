package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jake on 4/22/2015.
 */
public class MultiRole_Report extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private int numb=0;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private boolean tutorCheck, internCheck, volCheck;
    private ArrayList<String> years=new ArrayList<String>();
    private String month, year, teachhr, prephr, travel;
    private Spinner monthSpinner, teachSpinner, prepSpinner, travelSpinner,yearSpinner;
    private TextView teachhrOther, prephrOther, travelOther;
    private PopupWindow pop;
    private View layout;
    private EditText acomplishments, phoneNum;
    private Button send, clear;
    private CheckBox internBox, tutorBox, volBox;
    private TextView checkboxText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multirole_report);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        final Report submit = new Report();
        yearSpinner=(Spinner)findViewById(R.id.yearSpinner);
        int theyear= Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0;i<5;i++)
        {
            years.add(Integer.toString(theyear));
            Log.d("this is the year",theyear+"\n");
            theyear--;
        }
        ArrayAdapter<String> yearAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,years);

        yearSpinner.setAdapter(yearAdapter);
        monthSpinner = (Spinner) findViewById(R.id.spinner);
        teachSpinner = (Spinner) findViewById(R.id.spinner2);

        prepSpinner = (Spinner) findViewById(R.id.spinner3);
        travelSpinner = (Spinner) findViewById(R.id.spinner4);
        setSpinerslisteners();
        checkboxText = (TextView) findViewById(R.id.checks);
        acomplishments = (EditText)findViewById(R.id.editText);
        phoneNum = (EditText)findViewById(R.id.editText2);
        internBox = (CheckBox) findViewById(R.id.internBox);
        tutorBox = (CheckBox) findViewById(R.id.tutorBox);
        volBox = (CheckBox) findViewById(R.id.volBox);
        send = (Button) findViewById(R.id.sendData);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submitMonth = month+" "+year;
                String acomp;
                String name;
                name=Singleton.getInstance().getName();
                acomp = acomplishments.getText().toString();

                if(acomp.equals("")||phoneNum.getText().toString().equals("")){

                    /*if(acomp.equals("")){
                        acomplishments.setError("Please leave a comment");
                    }
                    if(phoneNum.getText().toString().equals("")){
                        phoneNum.setError("Please enter number");
                    }*/
                }
                else {

                    long phone = Long.parseLong(phoneNum.getText().toString());
                    submit.setName(name);
                    submit.setMonth(submitMonth);
                    submit.setRole("A1");
                    submit.setPhone(phone);
                    submit.setTeachhr(teachhr);
                    submit.setPrephr(prephr);
                    submit.setTravel(travel);
                    submit.setServhr("A0");
                    submit.setAcomp(acomp);

                    //new SubmitData().execute(submit);
                    yearSpinner.setSelection(0);
                    monthSpinner.setSelection(0);
                    teachSpinner.setSelection(0);
                    prepSpinner.setSelection(0);
                    travelSpinner.setSelection(0);
                    phoneNum.setText("");
                    acomplishments.setText("");
                }
            }
        });
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearSpinner.setSelection(0);
                monthSpinner.setSelection(0);
                teachSpinner.setSelection(0);
                prepSpinner.setSelection(0);
                travelSpinner.setSelection(0);
                phoneNum.setText("");
                acomplishments.setText("");
            }
        });


    }
    public void setSpinerslisteners(){
        //Spinner for years
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year=years.get(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
        //Month Spinner
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                month = getResources().getStringArray(R.array.Month)[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        month = getResources().getStringArray(R.array.Month)[monthSpinner.getSelectedItemPosition()];
        //Spinner for teaching hours
        teachSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                teachhrOther = (TextView) findViewById(R.id.otherView);

                if (position == 41) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    layout = inflater.inflate(R.layout.other, (ViewGroup) findViewById(R.id.otherId));
                    pop = new PopupWindow(layout, 500, 500, true);
                    pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    pop.setFocusable(true);
                    Button ok = (Button) layout.findViewById(R.id.other_ok);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText view = (EditText) layout.findViewById(R.id.other_edittext);
                            teachhr = view.getText().toString();

                            teachhrOther.setText(teachhr);
                            teachhrOther.setVisibility(View.VISIBLE);
                            pop.dismiss();

                        }
                    });
                } else {
                    teachhr = getResources().getStringArray(R.array.Hour_Code)[position];
                    teachhrOther.setVisibility(View.INVISIBLE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        teachhr = getResources().getStringArray(R.array.Hour_Code)[teachSpinner.getSelectedItemPosition()];
        //Preparation Spinner
        prepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                prephrOther = (TextView) findViewById(R.id.otherView2);

                if (position == 41) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    layout = inflater.inflate(R.layout.other, (ViewGroup) findViewById(R.id.otherId));
                    pop = new PopupWindow(layout, 500, 500, true);
                    pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    pop.setFocusable(true);
                    Button ok = (Button) layout.findViewById(R.id.other_ok);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText view = (EditText) layout.findViewById(R.id.other_edittext);
                            prephr = view.getText().toString();

                            prephrOther.setText(prephr);
                            prephrOther.setVisibility(View.VISIBLE);
                            pop.dismiss();

                        }
                    });
                } else {
                    prephr = getResources().getStringArray(R.array.Hour_Code)[position];
                    prephrOther.setVisibility(View.INVISIBLE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        prephr = getResources().getStringArray(R.array.Hour_Code)[prepSpinner.getSelectedItemPosition()];
        //Travel Spinner
        travelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                travelOther = (TextView) findViewById(R.id.otherView3);

                if (position == 41) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    layout = inflater.inflate(R.layout.other, (ViewGroup) findViewById(R.id.otherId));
                    pop = new PopupWindow(layout, 500, 500, true);
                    pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    pop.setFocusable(true);
                    Button ok = (Button) layout.findViewById(R.id.other_ok);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText view = (EditText) layout.findViewById(R.id.other_edittext);
                            travel = view.getText().toString();

                            travelOther.setText(travel);
                            travelOther.setVisibility(View.VISIBLE);
                            pop.dismiss();

                        }
                    });
                } else {
                    travel = getResources().getStringArray(R.array.Hour_Code)[position];
                    travelOther.setVisibility(View.INVISIBLE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        travel = getResources().getStringArray(R.array.Hour_Code)[prepSpinner.getSelectedItemPosition()];
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.tutorBox:
                if (checked) {
                    tutorCheck=true;
                    Report tutor = new Report();
                }
                else{
                    tutorCheck=false;
                }

                break;
            case R.id.internBox:
                if (checked){
                    internCheck=true;
                }

                else{

                }

                break;
            case R.id.volBox:
                if (checked){}

                else{}

                break;

        }
    }
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }
    public void onSectionAttached(int number) {
        // mTitle="Report";
        switch (number) {
            case 1:
                if(numb!=0) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                numb++;
                break;
            case 2:
                //  mTitle = "Email";
                startActivity(new Intent(getApplicationContext(), Email.class));
                break;
            case 3:
                //  mTitle = "Calendar";
                startActivity(new Intent(getApplicationContext(), Calender_.class));
                break;
            case 4:
                //  mTitle="Report";
                startActivity(new Intent(getApplicationContext(), ReportHub.class));
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.mr_report, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout){
            onPause();
            clearApplicationData();

            onDestroy();
            System.exit(0);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    private class SubmitData extends
            AsyncTask<Report, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MultiRole_Report.this);
            dialog.setTitle("Submitting...");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        protected String doInBackground(Report... input) {


            try {
                String path = "http://fortwayne.education/mobileapp.php";

                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
                // Limit
                HttpResponse response;
                JSONObject json = new JSONObject();



                try {
                    HttpPost post = new HttpPost(path);
                    json.put("lang", "en");
                    json.put("sdate", input[0].getStartdate());
                    json.put("dtime", input[0].getDatetime());
                    json.put("ip", input[0].getIp());
                    Log.d("ip = ", input[0].getIp());
                    json.put("name", input[0].getName());
                    json.put("month", input[0].getMonth());
                    json.put("phone", input[0].getPhone());
                    json.put("email", Singleton.getInstance().getEmail());
                    json.put("role",input[0].getRole());
                    json.put("teachhr", input[0].getTeachhr());
                    json.put("prephr", input[0].getPrephr());
                    json.put("travel", input[0].getTravel());
                    json.put("servhr", input[0].getServhr());
                    json.put("acomp", input[0].getAcomp());
                    Log.i("jason Object", json.toString());
                    post.setHeader("json", json.toString());
                    Log.d("message sent", json.toString());
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                            "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);
                    // Checking response
                    if (response != null) {
                        InputStream in = response.getEntity().getContent(); // Get the
                        // data in
                        // the
                        // entity
                        String a = convertStreamToString(in);
                        Log.i("Read from Server", a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {


            }
            return "s";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
               /* Toast.makeText(Tutor_Report.this, "error", Toast.LENGTH_SHORT).show();*/}
            else{

                /*Toast.makeText(Tutor_Report.this, result , Toast.LENGTH_SHORT).show();*/}
            dialog.dismiss();
            //AlertDialog finished = new AlertDialog.Builder(getActivity());

        }

    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_multirole_report, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MultiRole_Report) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
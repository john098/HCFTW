package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Email extends Activity
            implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private int numb=0;
    private ListView mainListView;
    private TextView text;
    private Button compose, select;
    private ArrayAdapter<String> listAdapter;
    private String inbox;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

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

        /*
         * Drawer initialization
         */
        //getting drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /*
         * End drawer initialization
         */

        /*
         * Listeners and functionality
         */
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
        /*
         * End Listeners and functionality
         */
    }
    /*
     * Drawer methods
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle= "Email";
        switch (number) {
            case 1:
                if(numb!=0) {
                    mTitle = "Home";
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                numb++;
                break;
            case 2:
                mTitle = "Email";
               //startActivity(new Intent(getApplicationContext(),Email.class));
                break;
            case 3:
                mTitle = "Calendar";
                startActivity(new Intent(getApplicationContext(), Calender_.class));

                break;
            case 4:
                mTitle = "Report";
                startActivity(new Intent(getApplicationContext(), Tutor_Report.class));
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
            getMenuInflater().inflate(R.menu.email, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    /*
     * End drawer methods
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if (id == R.id.logout){
           // Intent intent=new Intent(getApplicationContext(),Login.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_email, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Email) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

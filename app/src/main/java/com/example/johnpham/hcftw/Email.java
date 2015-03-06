package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.Message;
import com.microsoft.outlookservices.Recipient;
import com.microsoft.outlookservices.odata.MessageFetcher;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


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
    MessagesAdapter adapter;
    int messageCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_email);
        //definitions
        mainListView = (ListView) findViewById(R.id.mainListView);
        text = (TextView) findViewById(R.id.newMess);
        compose = (Button) findViewById(R.id.sendButton);
        select = (Button) findViewById(R.id.selectButton); //actually is update button
        //final ArrayList<String> emailList = new ArrayList<String>();
        MessagesAdapter  adapter = new MessagesAdapter(this, new ArrayList<Message>());
        mainListView.setAdapter(adapter);
        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.adapter = adapter;
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
        /*
        for(int i=1;i<=9;i++) {
            emailList.add("meneghelloj@yahoo.com\nHello!\nHow are you doing?");
        }
        inbox="Inbox(" +emailList.size()+")";
        text.setText(inbox); //updating the Inbox name
        listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow,emailList);
        mainListView.setAdapter(listAdapter);
        */
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message m = (Message) mainListView.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(),EmailReader.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String subject,date,body,from;
                List<Recipient> to;
                int j=0;
                subject = m.getSubject();
                body = m.getBody().getContent();
                SimpleDateFormat sdf =  new SimpleDateFormat("MM/dd/yyyy");
                date = sdf.format(m.getDateTimeSent().getTime());
                to = m.getToRecipients();
                //from = m.getFrom().getEmailAddress().getAddress();

                String name = "Unknown";
                Recipient recipient = m.getFrom();
                if (recipient != null) {
                    EmailAddress address = recipient.getEmailAddress();
                    if (address != null) {
                        name = address.getName();
                    }
                }
                i.putExtra("From",name);
                i.putExtra("Email",recipient.getEmailAddress().getAddress());
                i.putExtra("Date",date);
                i.putExtra("Subject",subject);
                i.putExtra("Body",body);
                for(Recipient r : to) {
                    i.putExtra("To " + j, r.getEmailAddress().getAddress());
                }
                startActivity(i);
            }
        });
        /*
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
        */

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
                Controller.getInstance().postASyncTask(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        return retrieveMails();
                    }
                });
            }
        });
        /*
         * End Listeners and functionality
         */
    }
    /*
    * Email methods
    *
    */
    /**
     * retrieves Inbox folder contents
     * @return nothing
     */
    private Void retrieveMails() {

        //create a client object
        OutlookClient client = new OutlookClient(ServiceConstants.ENDPOINT_ID, (DefaultDependencyResolver)Controller.getInstance().getDependencyResolver());

        // retrieve Inbox folder content asynchronously
        ListenableFuture<List<Message>> messages = client   .getMe()
                .getFolders()
                .getById("Inbox")
                .getMessages()
                .read();

        // handle success and failure cases
        final MessagesAdapter adapter = this.adapter;
        Futures.addCallback(messages, new FutureCallback<List<Message>>() {

            @Override
            public void onSuccess(final List<Message> result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Email.this, String.valueOf(result.size()) + " messages received", Toast.LENGTH_SHORT).show();
                        adapter.clear();
                        adapter.addAll(result);
                    }
                });

            }

            @Override
            public void onFailure(final Throwable t) {
                Controller.getInstance().handleError(Email.this, t.getMessage());
            }
        });

        return null;
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
       // mTitle= "Email";
        switch (number) {
            case 1:
                if(numb!=0) {
                  //  mTitle = "Home";
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                numb++;
                break;
            case 2:
               // mTitle = "Email";
               //startActivity(new Intent(getApplicationContext(),Email.class));
                break;
            case 3:
               // mTitle = "Calendar";
                startActivity(new Intent(getApplicationContext(), Calender_.class));

                break;
            case 4:
               // mTitle = "Report";
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
    /**
     * Adapter for the ListView
     */
    private class MessagesAdapter extends ArrayAdapter<Message> {

        /**
         * Caches one item in the list view
         */
        private class ViewHolder {
            TextView from;
            TextView subject;
            TextView body;
        }

        /**
         *
         * @param context   the activity Context
         */
        public MessagesAdapter(Context context, ArrayList<Message> messages) {
            super(context, R.layout.simplerow, messages);
        }

        /**
         * Overrides the ArrayAdapter getView method to retrieve the view for one item
         * @param position      the position of the item in the underlying collection
         * @param convertView   the view of the item
         * @param parent        the containing widget
         * @return              the View of one item of the containing ListView
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message m = getItem(position);

            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.simplerow, parent, false);
                viewHolder.from = (TextView) convertView.findViewById(R.id.rowTextView);
                viewHolder.subject = (TextView) convertView.findViewById(R.id.subject);
                viewHolder.body = (TextView) convertView.findViewById(R.id.description);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String name = "Unknown";
            Recipient recipient = m.getFrom();
            if (recipient != null) {
                EmailAddress address = recipient.getEmailAddress();
                if (address != null) {
                    name = address.getName();
                }
            }
            viewHolder.from.setText(name);
            viewHolder.subject.setText(m.getSubject());
            viewHolder.body.setText(m.getBodyPreview());
            return convertView;
        }
    }
}

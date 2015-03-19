package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.text.Html;
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
import android.content.DialogInterface;
import android.app.AlertDialog;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.directoryservices.User;
import com.microsoft.outlookservices.BodyType;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.ItemBody;
import com.microsoft.outlookservices.Message;
import com.microsoft.outlookservices.Recipient;
import com.microsoft.outlookservices.odata.MessageFetcher;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import android.view.Display;
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
    private boolean disabled=false;
    private boolean htmlOrText;
    MessagesAdapter adapter;
    private int messageCounter=0;
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
        select = (Button) findViewById(R.id.selectButton);
        adapter = new MessagesAdapter(this, new ArrayList<Message>());
        mainListView.setAdapter(adapter);
        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
         * End drawer initialization
         */
        retrieveMails();
        /*
         * Listeners and functionality
         */
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (disabled == false) {
                    Message m = (Message) mainListView.getItemAtPosition(position);
                    Intent i = new Intent(getApplicationContext(), EmailReader.class);
                    String subject, date, body, from;
                    List<Recipient> to;
                    subject = m.getSubject();
                    ItemBody text = m.getBody();
                    if(text.getContentType()==BodyType.HTML) {
                        htmlOrText=true;
                    }
                    else{
                        htmlOrText=false;
                    }
                    body=text.getContent();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    date = sdf.format(m.getDateTimeSent().getTime());
                    to = m.getToRecipients();
                    //from = m.getFrom().getEmailAddress().getAddress();
                    List<Recipient> cc = m.getCcRecipients();
                    List<Recipient> bcc = m.getBccRecipients();
                    String name = "Unknown";
                    Recipient recipient = m.getFrom();

                    if (recipient != null) {
                        EmailAddress address = recipient.getEmailAddress();
                        if (address != null) {
                            name = address.getName();
                        }
                    }
                    i.putExtra("From", name);
                    i.putExtra("Email", recipient.getEmailAddress().getAddress());
                    i.putExtra("Date", date);
                    i.putExtra("Subject", subject);
                    i.putExtra("Body", body);
                    i.putExtra("Html",htmlOrText);
                    ArrayList<String> recipients = new ArrayList<String>();
                    for (Recipient r : to) {
                        if (r.getEmailAddress().getName() != "Unknown") {
                            recipients.add(r.getEmailAddress().getName());
                        } else {
                            recipients.add(r.getEmailAddress().getAddress());
                        }
                    }
                    for (Recipient r : cc) {
                        if (r.getEmailAddress().getName() != "Unknown") {
                            recipients.add(r.getEmailAddress().getName());
                        } else {
                            recipients.add(r.getEmailAddress().getAddress());
                        }
                    }
                    for (Recipient r : bcc) {
                        if (r.getEmailAddress().getName() != "Unknown") {
                            recipients.add(r.getEmailAddress().getName());
                        } else {
                            recipients.add(r.getEmailAddress().getAddress());
                        }
                    }
                    i.putStringArrayListExtra("recipients", recipients);
                    startActivity(i);
                }
            }
        });
        mainListView.setOnItemLongClickListener(
                new ListView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        disabled=true;
                        new AlertDialog.Builder(Email.this)
                                .setTitle("Delete?")
                                .setMessage("Do you really want to delete this message?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast.makeText(Email.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        Message m = (Message) mainListView.getItemAtPosition(position);
                                        adapter.remove(m);
                                        adapter.notifyDataSetChanged();
                                        Singleton.getInstance().getClient().getMe().getMessage(m.getId()).delete();
                                        disabled=false;
                                    }})
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        disabled=false;
                                    }}).show();
                        return false;
                    }
                }
        );
        compose.setOnClickListener(new View.OnClickListener()

                                  {
                                      public void onClick (View v){
                                          startActivity(new Intent(getApplicationContext(),Compose.class));
                                              }
                                          });

            select.setOnClickListener(new View.OnClickListener()

            {
                public void onClick (View v){
                //Select things
                Controller.getInstance().postASyncTask(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        return retrieveMails();
                    }
                });
            }
            }

            );
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
        OutlookClient client = Singleton.getInstance().getClient();

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
                        if(result.size()!=messageCounter) {
                            Toast.makeText(Email.this, String.valueOf(result.size()) + " messages received", Toast.LENGTH_SHORT).show();
                        }
                        messageCounter=result.size();
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
    @Override
    public void onResume(){
        super.onResume();
        retrieveMails();
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

package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Contact;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.ItemBody;
import com.microsoft.outlookservices.Message;
import com.microsoft.outlookservices.Recipient;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import android.view.Display;

public class Compose extends Activity {
    Button send;
    String toString;
    String ccString;
    String bccString;
    String subjString;
    String messString;
    String theEmail;
    MultiAutoCompleteTextView toField;
    MultiAutoCompleteTextView ccField;
    MultiAutoCompleteTextView bccField;
    EditText subjField;
    EditText messField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Contact> contact;
        ArrayList<String> contactList;
        ArrayList<String> nameList;
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_compose);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        Display dis = getWindowManager().getDefaultDisplay();
        int widthSize = dis.getWidth();
        ListenableFuture<List<Contact>> contactFuture= Singleton.getInstance().getClient().getMe().getContacts().top(9999).read();

        try {
            contact = contactFuture.get();
            toField = (MultiAutoCompleteTextView) findViewById(R.id.toTextBox);
            toField.getLayoutParams().width=widthSize;
            ccField = (MultiAutoCompleteTextView) findViewById(R.id.ccTextBox);
            ccField.getLayoutParams().width=widthSize;
            bccField = (MultiAutoCompleteTextView) findViewById(R.id.bccTextBox);
            bccField.getLayoutParams().width=widthSize;
            subjField = (EditText) findViewById(R.id.subjTextBox);
            subjField.getLayoutParams().width=widthSize;
            // In the onCreate method
            contactList = new ArrayList<String>();
            nameList = new ArrayList<String>();
            for(int j=0;j<contact.size();j++){
                contactList.add(contact.get(j).getEmailAddresses().get(0).getAddress());
                nameList.add(contact.get(j).getEmailAddresses().get(0).getName());
            }
            //change to diff adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Compose.this,android.R.layout.simple_dropdown_item_1line,nameList);
            toField.setAdapter(adapter);
            toField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            ccField.setAdapter(adapter);
            ccField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            bccField.setAdapter(adapter);
            bccField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            messField = (EditText) findViewById(R.id.emailTextBox);
            messField.getLayoutParams().width=widthSize;
            if(i.hasExtra("Name")) {
                toString = i.getStringExtra("Name");
                toField.setText(toString);
                toField.setSelection(toString.length());
                subjString = i.getStringExtra("Subject");
                subjField.setText("Re: " + subjString);
            }
        /*
        removed Cancel button, because of back arrow on actionbar
        cancel = (Button) findViewById(R.id.cancelButton);
        //check for compose click
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });*/
            send = (Button) findViewById(R.id.sendButton);
            //check for compose click
            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Controller.getInstance().postASyncTask(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            return sendEmail();
                        }
                    });
                    finish();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    /**
     * send one email to the account specified in the preferences
     * @return nothing
     */
    private Void sendEmail() {

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //final String email = preferences.getString(SettingsConstants.EMAIL_TARGET_KEY, SettingsConstants.EMAIL_TARGET);
        /*
        if (email.isEmpty()) {
            Controller.getInstance().handleError(Compose.this, "Please set email address in Settings or edit ServiceConstants");
            return null;
        }*/

        try {

            // create a client object
            OutlookClient client = Singleton.getInstance().getClient();

            // craft a message
            //final String messageCounter = String.valueOf(++this.messageCounter);

            //get people email is to
            toString = toField.getText().toString();
            ArrayList<Recipient> recipients=new ArrayList<Recipient>();
            recipients.addAll(parseEmails(toString));
            //people email is cc'd
            ccString = ccField.getText().toString();
            ArrayList<Recipient> ccRecipients = new ArrayList<Recipient>();
            ccRecipients.addAll(parseEmails(ccString));
            //get people email is to
            bccString = bccField.getText().toString();
            ArrayList<Recipient> bccRecipients = new ArrayList<Recipient>();
            bccRecipients.addAll(parseEmails(bccString));

            ItemBody body = new ItemBody();
            messString = messField.getText().toString();
            body.setContent(messString);

            Message m = new Message();
            m.setToRecipients(recipients);
            m.setCcRecipients(ccRecipients);
            m.setBccRecipients(bccRecipients);
            subjString = subjField.getText().toString();
            m.setSubject(subjString);
            m.setBody(body);

            // prepare message for sending, adding the message to the Drafts folder
            // this operation is synchronous
            Message addedMessage = client.getMe().getMessages().add(m).get();

            // send message asynchronously
            ListenableFuture<Integer> sent = client .getMe()
                    .getMessages()
                    .getById(addedMessage.getId())
                    .getOperations().send();
            // handle success and failure cases
            Futures.addCallback(sent, new FutureCallback<Integer>() {
                @Override
                public void onSuccess(final Integer result) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(Compose.this, "Message sent", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onFailure(final Throwable t) {
                    Controller.getInstance().handleError(Compose.this, t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Controller.getInstance().handleError(Compose.this, "eus");
        }

        return null;
    }
    public ArrayList<Recipient> parseEmails(String addressString){
        ArrayList<EmailAddress> addresses = new ArrayList<EmailAddress>();
        String[] split = addressString.split("\\(|\\)|\\,");
        for(int i=0; i<split.length;i++) {
            if(split[i].contains("@")) {
            EmailAddress temp = new EmailAddress();
            temp.setAddress(split[i].trim());
            addresses.add(temp);
            }
        }
        ArrayList<Recipient> recipients = new ArrayList<Recipient>();
        for(int i=0; i<addresses.size(); i++) {
            Recipient toRecipient = new Recipient();
            toRecipient.setEmailAddress(addresses.get(i));
            recipients.add(toRecipient);
        }
        return recipients;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

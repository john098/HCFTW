package com.example.johnpham.hcftw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.ItemBody;
import com.microsoft.outlookservices.Message;
import com.microsoft.outlookservices.Recipient;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;


public class Compose extends Activity {
    Button send;
    String toString;
    String ccString;
    String bccString;
    String subjString;
    String messString;
    EditText toField;
    EditText ccField;
    EditText bccField;
    EditText subjField;
    EditText messField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_compose);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        toField = (EditText) findViewById(R.id.toTextBox);
        ccField = (EditText) findViewById(R.id.ccTextBox);
        bccField = (EditText) findViewById(R.id.bccTextBox);
        subjField = (EditText) findViewById(R.id.subjTextBox);
        messField = (EditText) findViewById(R.id.emailTextBox);
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
            OutlookClient client =
                    new OutlookClient(
                            ServiceConstants.ENDPOINT_ID,
                            (DefaultDependencyResolver) Controller.getInstance().getDependencyResolver()
                    );

            // craft a message
            //final String messageCounter = String.valueOf(++this.messageCounter);

            //get people email is to
            toString = toField.getText().toString();
            ArrayList<EmailAddress> addresses = new ArrayList<EmailAddress>();
            Scanner st = new Scanner(toString);
            st.useDelimiter(" ,");
            while(st.hasNext()){
                EmailAddress temp = new EmailAddress();
                temp.setAddress(st.next());
                addresses.add(temp);
            }
            ArrayList<Recipient> recipients = new ArrayList<Recipient>();
            for(EmailAddress e: addresses) {
                Recipient toRecipient = new Recipient();
                toRecipient.setEmailAddress(e);
                recipients.add(toRecipient);
            }
            //get people email is cc'd
            ccString = ccField.getText().toString();
            ArrayList<EmailAddress> ccAddr = new ArrayList<EmailAddress>();
            Scanner st2 = new Scanner(ccString);
            st.useDelimiter(" ,");
            while(st2.hasNext()){
                EmailAddress temp = new EmailAddress();
                temp.setAddress(st.next());
                ccAddr.add(temp);
            }
            ArrayList<Recipient> ccRecipients = new ArrayList<Recipient>();
            for(EmailAddress e: ccAddr) {
                Recipient ccRecipient = new Recipient();
                ccRecipient.setEmailAddress(e);
                ccRecipients.add(ccRecipient);
            }
            //get people email is to
            bccString = bccField.getText().toString();
            ArrayList<EmailAddress> bccAddresses = new ArrayList<EmailAddress>();
            Scanner st3 = new Scanner(toString);
            st.useDelimiter(" ,");
            while(st3.hasNext()){
                EmailAddress temp = new EmailAddress();
                temp.setAddress(st.next());
                bccAddresses.add(temp);
            }
            ArrayList<Recipient> bccRecipients = new ArrayList<Recipient>();
            for(EmailAddress e: bccAddresses) {
                Recipient bccRecipient = new Recipient();
                bccRecipient.setEmailAddress(e);
                bccRecipients.add(bccRecipient);
            }



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
                            Toast.makeText(Compose.this, "Message sent", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onFailure(final Throwable t) {
                    Controller.getInstance().handleError(Compose.this, t.getMessage());
                }
            });

        } catch (final Throwable t) {
            Controller.getInstance().handleError(Compose.this, t.getMessage());
        }

        return null;
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

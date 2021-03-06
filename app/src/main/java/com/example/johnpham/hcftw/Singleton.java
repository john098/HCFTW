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
import java.util.Iterator;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Contact;
import com.microsoft.outlookservices.EmailAddress;
import com.microsoft.outlookservices.Event;
import com.microsoft.outlookservices.User;
import com.microsoft.outlookservices.odata.EventFetcher;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;
import com.microsoft.outlookservices.BodyType;
import java.util.ArrayList;


import java.util.List;
import java.util.ListIterator;

/**
 * Created by johnpham on 2/26/15.
 */
public class Singleton {

    private static Singleton singleton = null;
    private List<Event> event;
    private List<Contact> contacts;
    ListenableFuture<List<Contact>> contactFuture;
    private OutlookClient client =new OutlookClient(ServiceConstants.ENDPOINT_ID,(DefaultDependencyResolver)Controller.getInstance().getDependencyResolver());
    private ListenableFuture<List<Event>> even;
    private String name;
    private String email;
    private String todayDate;
    private ListenableFuture<User> user=client.getMe().read();
    private ListenableFuture<List<Contact>> contact =client.getMe().getContacts().top(9999).read();

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setFuture(ListenableFuture<List<Event>> even)
    {
        this.even=even;
    }
    public void setEmail(String alias){
        this.email=alias+"@lifetime.education";
    }
    public String getEmail(){
        return email;
    }
    public ListenableFuture<List<Event>> getFuture()
    {

        return even;
    }
    public Singleton() {
        try {
            setName(user.get().getDisplayName());
            setEmail(user.get().getAlias());
        }
        catch (Exception e)
        {

        }
       Runnable runnable=new Runnable() {
            @Override
            public void run() {

                while(true) {
                    try {
                        even=client.getMe().getCalendar().getEvents().top(9999).read();
                        contactFuture=client.getMe().getContacts().top(9999).read();
                        event = even.get();
                        contacts=contactFuture.get();
                        setContacts(contacts);
                        setEvent(event);
                        Thread.sleep(300000);//5min

                    }
                    catch(Exception e)
                    {

                    }

                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
    public  void setContacts(List<Contact> contacts)
    {
        this.contacts=contacts;
    }
   public ArrayList<String> getContact()
    {
        ArrayList<String> contactList=new ArrayList<String>();
        for(int j=0;j<contacts.size();j++){
            contactList.add(contacts.get(j).getEmailAddresses().get(0).getAddress());
            //nameList.add(contact.get(j).getEmailAddresses().get(0).getName());
        }
        return contactList;
    }
    public ArrayList<String> getNameList()
    {
        ArrayList<String> nameList=new ArrayList<String>();
        for(int j=0;j<contacts.size();j++){

            nameList.add(contacts.get(j).getEmailAddresses().get(0).getName());
        }
        return nameList;
    }

    public void refresh()
    {try{
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                    try {
                        even=client.getMe().getCalendar().getEvents().top(9999).read();
                        event = even.get();
                        setEvent(event);
                        contactFuture=client.getMe().getContacts().top(9999).read();
                        contacts=contactFuture.get();
                        setContacts(contacts);

                    }
                    catch(Exception e)
                    {

                    }

            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
    catch(Exception e)
    {

    }


    }
    public OutlookClient getClient()
    {
        return client;
    }
    public void setEvent(List<Event> event) {

         this.event=event;
    }
    public List<Event> getEvent()
    {

        return event;
    }
    public void setTodayDate(String todayDate)
    {
        this.todayDate=todayDate;

    }
    public String getTodayDate()
    {
        return todayDate;
    }
}

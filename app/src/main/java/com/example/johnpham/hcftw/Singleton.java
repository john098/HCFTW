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
    private OutlookClient client =new OutlookClient(ServiceConstants.ENDPOINT_ID,(DefaultDependencyResolver)Controller.getInstance().getDependencyResolver());
    private ListenableFuture<List<Event>> even;
    private String name;
    private String todayDate;
    private ListenableFuture<User> user=client.getMe().read();
    private ListenableFuture<List<Contact>> contacts =client.getMe().getContacts().read();
    private User userName;
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
    public ListenableFuture<List<Event>> getFuture()
    {

        return even;
    }
    public String getAddress(){
        String add;

        //iter = contact;
        add="";
        return add;
    }
    public Singleton() {
        try {
            setName(user.get().getDisplayName());
        }
        catch (Exception e)
        {

        }
       Runnable runnable=new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        even=client.getMe().getCalendar().getEvents().read();
                        event = even.get();
                        setEvent(event);

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

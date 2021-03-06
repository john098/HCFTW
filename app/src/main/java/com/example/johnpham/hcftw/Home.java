package com.example.johnpham.hcftw;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static int count=0;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private  Singleton single;
    private ProgressDialog dial=null;
    private ImageView background;


    private PopupWindow about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean finish = getIntent().getBooleanExtra("finish", false);
        if(finish){
            System.exit(0);
            return;
        }
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        setContentView(R.layout.activity_home);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        if(count==0) {
        SettableFuture<Void> authenticated =
                Authentication.acquireToken(
                        Home.this
                );

            final Context context = this;
            Futures.addCallback(authenticated, new FutureCallback<Void>() {
                @Override
                public void onSuccess(Void result) {

                    Controller.getInstance().postASyncTask(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    dial=ProgressDialog.show(Home.this,"Logging in","Please wait",true);
                                    onPause();
                                    new Thread() {
                                        public void run() {
                                            while(single==null) {
                                                single=Singleton.getInstance();
                                            }
                                            SystemClock.sleep(3000);
                                            onResume();
                                            dial.dismiss();

                                        }
                                    }.start();

                                    background=(ImageView)findViewById(R.id.imageView3);
                                    background.setBackground(getResources().getDrawable(R.drawable.resizehome));




                                    // enable scenarios



                                }
                            });

                            return null;
                        }
                    });
                }
                public void onFailure(final Throwable t) {

                    Controller.getInstance().handleError(Home.this, t.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setIcon(getResources().getDrawable(R.drawable.x));
                    builder.setMessage("Oops Something is wrong\n"+"Please try again later.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    onDestroy();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            count++;

        }
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle="Home";
             //   startActivity(new Intent(getApplicationContext(), Home.class));
                break;
            case 2:
                mTitle = "Email";
                startActivity(new Intent(this,Email.class));
                break;
            case 3:
                mTitle = "Calendar";
                startActivity(new Intent(this, Calender_.class));

                break;
            case 4:
                mTitle = "Report";

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Authentication.context.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
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
       // MenuItem name=(MenuItem)findViewById(R.id.name);
        //name.setTitle(single.getName());
        if (id == R.id.logout) {
            Intent intent = new Intent(this,Home.class);
            intent.putExtra("finish", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
/*            dial.setIcon(getResources().getDrawable(R.drawable.three));
            dial=ProgressDialog.show(Home.this,"Logging Out","Please wait",true);
            new Thread() {
                public void run() {
                    SystemClock.sleep(2000);
                    dial.dismiss();

                }
            }.start();*/
            clearApplicationData();
            onDestroy();
            startActivity(intent);
            finish();
            //System.exit(0);

            return true;
        }
        if(id == R.id.about){
            ArrayList<String>con=new ArrayList<String>();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_login,
                    (ViewGroup) findViewById(R.id.aboutUsPopup));
            Display dis = getWindowManager().getDefaultDisplay();
            Button ok=(Button)layout.findViewById(R.id.Okbutton);

            about=new PopupWindow(layout,1200,1000,true);
            about.showAtLocation(layout, Gravity.CENTER, 0, 0);
            about.setFocusable(true);
            con.add("Detroit Phone");
            con.add("313-789-0100");
            con.add("Fort Wayne Phone");
            con.add("260-557-1230");
            con.add("Email");
            con.add("info@lifetime.education");
            ArrayAdapter<String> adp=new ArrayAdapter<String>(Home.this,android.R.layout.simple_list_item_1,con);
            adp.notifyDataSetChanged();
            ListView contactUs=(ListView)layout.findViewById(R.id.ContactList);
            contactUs.setAdapter(adp);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    about.dismiss();
                }
            });

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
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }
}

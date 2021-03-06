package com.example.johnpham.hcftw;
import android.app.Activity;
import java.io.File;
import java.util.List;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import android.text.format.DateFormat;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Event;
import android.view.Display;
public class Calender_ extends
        Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {
    private ListenableFuture<List<Event>> even;
    private static final String tag = "Main";
    private int numb=0;
    private Button curMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;

    private int month, year;
    private final DateFormat date = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0c2f51")));
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
               R.id.navigation_drawer,
             (DrawerLayout) findViewById(R.id.drawer_layout));
        Display dis = getWindowManager().getDefaultDisplay();
        int widthSize = dis.getWidth();
        int eachSize = widthSize / 7;
        Button Sun = (Button) findViewById(R.id.Sun);
        Button Mon = (Button) findViewById(R.id.Mon);
        Button Tue = (Button) findViewById(R.id.Tue);
        Button Wed = (Button) findViewById(R.id.Wed);
        Button Thu = (Button) findViewById(R.id.Thu);
        Button Fri = (Button) findViewById(R.id.Fri);
        Button Sat = (Button) findViewById(R.id.Sat);

        Sun.getLayoutParams().width = eachSize;
        Mon.getLayoutParams().width = eachSize;
        Tue.getLayoutParams().width = eachSize;
        Wed.getLayoutParams().width = eachSize;
        Thu.getLayoutParams().width = eachSize;
        Fri.getLayoutParams().width = eachSize;
        Sat.getLayoutParams().width = eachSize;
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH);
        year = _calendar.get(Calendar.YEAR);

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        curMonth = (Button) this.findViewById(R.id.curMonth);
        curMonth.setText(date.format(dateTemplate, _calendar.getTime()));
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"freescpt.ttf");
        curMonth.setTypeface(tf);
        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);
        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
        Button add = (Button) findViewById(R.id.AddButton);
        add.getLayoutParams().width=widthSize/2;

        Button cancel=(Button)findViewById(R.id.cancel);
        cancel.getLayoutParams().width=widthSize/2;
        cancel.setText("Update");
        cancel.setOnClickListener(new OnClickListener() {//test this
            @Override
            public void onClick(View v) {
                Singleton.getInstance().refresh();
                //adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
                adapter.notifyDataSetChanged();
                //calendarView.setAdapter(adapter);
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewNotes.class));
            }
        });
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
            getMenuInflater().inflate(R.menu.calender_, menu);
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

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
        curMonth.setText(date.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        if (v == prevMonth) {
            if (month <= 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month >= 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
            setGridCellAdapterToDate(month, year);
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
            View rootView = inflater.inflate(R.layout.fragment_calender_, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Calender_) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public class GridCellAdapter extends BaseAdapter implements OnClickListener,View.OnLongClickListener {
        private ListenableFuture<List<Event>> even;
        private static final String tag = "GridCellAdapter";
        private final Context _context;
        private View first;
        private View Second;
        private final List<String> list;
        private List<Event> events;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final int month, year;
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
        private Singleton singleton = Singleton.getInstance();
        private String date;

        public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
            super();

            this._context = context;
            this.list = new ArrayList<String>();
            this.month = month;
            this.year = year;


            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            // Print Month
            printMonth(month, year);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);
            gridcell.setOnLongClickListener(this);
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];


            int m = Integer.parseInt(themonth);
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            String today=theday + "-" + themonth + "-" + theyear;
            if (m - 1 == month) {   row.setBackgroundResource(R.drawable.calendar_button_selector1);
               gridcell.setBackgroundResource(R.drawable.calendar_button_selector1);
                try {
                    events = singleton.getEvent();
                    SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
                    for (Event e : events) {
                        if (!e.getIsAllDay()) {
                            date = sdf.format(e.getStart().getTime());
                            if (date.compareTo(gridcell.getTag().toString()) == 0) {
                                gridcell.setTextColor(Color.RED);
                               // row.setBackgroundResource(R.drawable.calendar_tile_small4);
                                continue;
                            }
                        }
                        date = sdf.format(e.getEnd().getTime());
                        if (date.compareTo(gridcell.getTag().toString()) == 0) {

                            gridcell.setTextColor(Color.RED);
                        }
                    }

                } catch (Exception e) {
                    Log.d("the error is", e + "\n");
                }
                int todayYear,todaymonth,todayDate;
                Calendar temp=Calendar.getInstance();
                todayYear=temp.get(Calendar.YEAR);
                todaymonth=temp.getInstance().get(Calendar.MONTH)+1;
                todayDate=temp.getInstance().get(Calendar.DATE);



                String line=todayDate + "-" + todaymonth+ "-" + todayYear;
                if(todaymonth==13) {
                    todaymonth = 0;
                }
                if(today.equals(line))
                {
                    gridcell.setBackgroundResource(R.drawable.calendar_button_selector2);//Current day color
                    row.setBackgroundResource(R.drawable.calendar_button_selector2);
                }
            }
            else{
                row.setBackgroundResource(R.drawable.calendar_button_selector);
            }
            // Set the Day GridCell

            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            return row;
        }

        private void printMonth(int mm, int yy) {

            // The number of days to leave blank at
            // the start of this month.
            int trailingSpaces = 0;
            int leadSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int curMonth = mm;
            String currentMonthName = getMonthAsString(mm);
            daysInMonth = getNumberOfDaysOfMonth(mm);
            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, curMonth, 1);//1


            if (curMonth == 11) {
                prevMonth = curMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (curMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;

            } else {
                prevMonth = curMonth - 1;
                nextMonth = curMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);

            }

            // Compute how much to leave before before the first day of the
            // month.
            // getDay() returns 0 for Sunday.
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;


            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + (prevMonth + 1) + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {

                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + (curMonth + 1) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + (curMonth + 1) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {

                list.add(String.valueOf(i + 1) + "-GREY" + "-" + (nextMonth + 1) + "-" + nextYear);
            }
        }

        public int getCount() {

            return list.size();
        }

        public void onClick(View view) {
            Second = view;
            if (first != null) {
                first.setBackground(getResources().getDrawable(R.drawable.calendar_tile_small));
            }
            String date_month_year = (String) view.getTag();

            //Toast.makeText(getApplicationContext(), date_month_year, Toast.LENGTH_SHORT).show();
            first = Second;

            Intent i = new Intent(getApplicationContext(), Events.class);

            singleton.setTodayDate(date_month_year);
            startActivity(i);
            finish();

        }
      public boolean onLongClick(View view)
      {
          Second = view;
          if (first != null) {
              first.setBackground(getResources().getDrawable(R.drawable.calendar_tile_small));
          }
          String date_month_year = (String) view.getTag();

         // Toast.makeText(getApplicationContext(), date_month_year, Toast.LENGTH_SHORT).show();
          first = Second;

          Intent i = new Intent(getApplicationContext(), NewNotes.class);
          singleton.setTodayDate(date_month_year);
          i.putExtra("date", singleton.getTodayDate());
          startActivity(i);
          finish();

          return true;
      }

        public long getItemId(int position) {
            return position;
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {

            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {

            return currentWeekDay;
        }
    }

}



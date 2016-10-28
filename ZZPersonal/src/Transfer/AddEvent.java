package Transfer;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    //FOR DATE PICKER
    static final int DIALOG_ID = 0;

    private int rYear, rMonth, rDay, rWeekday;
    private int sStartYear, sStartMonth, sStartDay;
    private int sEndYear, sEndMonth, sEndDay;


    private int sel_start_year, sel_start_month, sel_start_day;
    private int sel_end_year, sel_end_month, sel_end_day;

    private Date sel_startDate, sel_endDate;
    private int startHR, startMIN; String AMPM;
    private int current_hr, current_min;
    private int year,month,day;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_36dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recieveDate();
        setTodayDateTimeToTextViews();
    }


    public void setTodayDateTimeToTextViews()
    {
        TextView startDate = (TextView) findViewById(R.id.editStartDate);
        TextView endDate = (TextView) findViewById(R.id.editEndDate);
        TextView startTime = (TextView) findViewById(R.id.editStartTime);
        TextView endTime = (TextView) findViewById(R.id.editEndTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");

        SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM");
        Calendar cal = Calendar.getInstance();
        cal.set(rYear,rMonth,rDay);
        String formatted = format1.format(cal.getTime());

        startDate.setText(formatted);
        endDate.setText(formatted);

    }

    /**
     * Recieves date from Main Activity
     */
    public void recieveDate()
    {
        Intent mIntent = getIntent();
        rDay = mIntent.getIntExtra("selected_day",0);
        rMonth = mIntent.getIntExtra("selected_month",0);
        rYear = mIntent.getIntExtra("selected_year",0);
        rWeekday = mIntent.getIntExtra("selected_weekday", 0);

        SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM");
        Calendar cal = Calendar.getInstance();
        cal.set(rYear,rMonth,rDay);
        String formatted = format1.format(cal.getTime());


        TextView tv = (TextView) findViewById(R.id.textViewDateTemp);
        //tv.setText(rDay+"/"+rMonth+"/"+rYear + "/ " + rWeekday);
        tv.setText(formatted +"    " +rDay+"/"+rMonth+"/"+rYear + "/ " + rWeekday);

    }




    public void timePicker(View view)
    {
        final TextView startTimeText = (TextView) findViewById(R.id.editStartTime);
        startTimeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = 0;

                TimePickerDialog mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

//                        setSelectedTime();

                        startTimeText.setText( selectedHour + ":" + selectedMinute);


                    }
                }, hour, minute, false); // true for 24 hour, false for 12 hour

//                String AM_PM ;
//                if(selectedHour < 12) {
//                    AM_PM = "AM";
//                } else {
//                    AM_PM = "PM";
//                }
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    /**
     * When startDate is tapped, this pops up
     * @param view
     */
    public void datePicker(View view)
    {
        final TextView editStartDate = (TextView) findViewById(R.id.editStartDate);
        editStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //final Calendar calendar = Calendar.getInstance();
                int yy = rYear;
                int mm = rMonth;
                int dd = rDay;
                DatePickerDialog datePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sel_startDate = new Date(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM");
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);
                        String formatted = format1.format(cal.getTime());
                        editStartDate.setText(formatted);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });



        final TextView editEndDate = (TextView) findViewById(R.id.editEndDate);
        editEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //final Calendar calendar = Calendar.getInstance();
                int yy = rYear;
                int mm = rMonth;
                int dd = rDay;
                DatePickerDialog datePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sel_endDate = new Date(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM");
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);
                        String formatted = format1.format(cal.getTime());
                        editEndDate.setText(formatted);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
    }

    private void saveEvent()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save_button:
                saveEvent();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return false;
    }

    /*

    protected Dialog onCreateDialog(int id)
    {
        if (id== DIALOG_ID)

            return new DatePickerDialog(this, dpickerListener, year, month, day);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int yearx, int monthOfYear, int dayOfMonth) {
            year = yearx;
            month = monthOfYear;
            day = dayOfMonth;

            Toast.makeText(AddEventActivity.this, year + "/" + (month+1) + "/" + day, Toast.LENGTH_LONG).show();

        }
    };

     */

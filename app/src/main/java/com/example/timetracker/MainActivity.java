package com.example.timetracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // ------------------- REFERENCES ------------------- \\
    //  references buttons and other controls on layout   \\
    ToggleButton tt_toggle;
    CalendarView tt_calendar;
    Switch tt_switch;
    //TextView tt_date;

    // ------------------- CONSTANTS ------------------- \\
    static final int MS_IN_MINUTE = 60000;

    // -------------------- LOG TAGS -------------------- \\
    private static final String TOGGLE   = "ToggleChecker";
    private static final String CALENDAR = "CalendarChecker";
    private static final String SWITCH   = "SwitchChecker";
    private static final String DATE     = "DateChecker";
    private static final String MAIN     = "MainActivity";
    private static final String SHIFT    = "ShiftCalculation";
    private static final String DATABASE = "DatabaseMethod";
    private static final String SHAKE    = "ShakeSensor";

    // ------------------ DATE FORMATS ------------------- \\
    //SimpleDateFormat sdf_tt_date    = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat sdf_log_checks = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    SimpleDateFormat sdf_str_to_int = new SimpleDateFormat("dd/MM/yyyy");

    // ---------------- TIMING VARIABLES ----------------- \\
    public Date start_time_stamp;
    public Date end_time_stamp;

    // --------------- UPDATE CURRENT DATE ---------------- \\
    private TextView tt_date;
    private Calendar calendar;
    private SimpleDateFormat sdf_tt_date;
    private String date;

    // ----------------- SHAKE RESOURCES ------------------ \\
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    public boolean shake_flag;
    private boolean isAccelerometerAvailable;
    private boolean isNotFirstTime = false;
    private float currX, currY, currZ;
    private float lastX, lastY, lastZ;
    private float xDiff, yDiff, zDiff;
    private float shakeThreshold = 5f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tt_toggle = findViewById(R.id.tt_toggle);
        tt_calendar = findViewById(R.id.tt_calendar);
        tt_switch = findViewById(R.id.tt_switch);
        tt_date = findViewById(R.id.tt_date);

        /*  Handle date text view, update as date changes in real time
         *  make use of static variables for instances of the class not class itself
         *  => layout reference: tt_date
         */
        calendar = Calendar.getInstance();
        sdf_tt_date = new SimpleDateFormat("dd MMM yyyy");
        date = sdf_tt_date.format(calendar.getTime());
        Log.i(DATE, date);
        tt_date.setText(date);

        /*  Handle CalendarView when date is selected
         *  will call openDialog function to alert user to shift time table
         *  => layout reference: tt_calendar
         */
        tt_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                //month passed in as array index so month + 1
                Log.i(CALENDAR, String.format("Year: %d, Month: %d, Day: %d",
                                              year, (month + 1), day));

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                TimeTrackingModel timeTrackingModel = new TimeTrackingModel(year, month + 1, day, 0);
                int minutes = dataBaseHelper.clockedMinutesToday(timeTrackingModel);

                Dialog dialog = new Dialog(year, month, day, minutes);
                dialog.show(getSupportFragmentManager(), "Minutes worked alert dialog");
            }
        });

        /*  Handle start/stop toggle to calculate shift time
         *  will add shift data to database after being toggled off
         *  => layout reference: tt_toggle
         */
        tt_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String temp = sdf_log_checks.format(new Date());
                if(isChecked) {
                    start_time_stamp = new Date();
                    Log.i(SHIFT, "Start Time: " + temp);
                }
                else  {
                    end_time_stamp = new Date();
                    Log.i(SHIFT, "Stop Timer: " + temp);

                    long difference = end_time_stamp.getTime() - start_time_stamp.getTime();
                    int shiftTotal = (int) Math.floor(difference / MS_IN_MINUTE);
                    Log.i(SHIFT, "This shift time: " + shiftTotal);

                    String strToInt = sdf_str_to_int.format(new Date());
                    String[] strToIntSplit = strToInt.split("/");

                    int day   = Integer.parseInt(strToIntSplit[0]);
                    int month = Integer.parseInt(strToIntSplit[1]);
                    int year  = Integer.parseInt(strToIntSplit[2]);

                    TimeTrackingModel timeTrackingModel;

                    try {
                        timeTrackingModel = new TimeTrackingModel(year, month, day, shiftTotal);
                    }
                    catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error creating time track model", Toast.LENGTH_SHORT).show();
                        timeTrackingModel = new TimeTrackingModel(0, 0, 0, 0);
                    }

                    //Add shift data to table approaiately
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                    boolean success = dataBaseHelper.updateOnDuplicate(timeTrackingModel);
                    if(success) { Log.i(DATABASE, "Successfully added data to db"); }

                    //Check out today work data for debugging
                    int checkMinutes = dataBaseHelper.clockedMinutesToday(timeTrackingModel);
                    Log.i(DATABASE, "Minutes worked today: " + checkMinutes);
                }
            }
        });

        /*  Allow user to activate shake function
         *  listener will function outside of app as well while toggled to register shake activation
         *  will operate same as start/stop toggle
         *  => layout reference: tt_date
         */
        tt_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.i(SWITCH, "Shake has been activated.");
                    shake_flag = true;
                }
                else {
                    Log.i(SWITCH, "Shake has been deactivated.");
                    shake_flag = false;
                }
           }
        });

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //SHAKE SENSOR
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerAvailable = true;
        } else {
            Log.i(SHAKE, "Accelerometer sensor is not available");
            isAccelerometerAvailable = false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        currX = sensorEvent.values[0];
        currY = sensorEvent.values[1];
        currZ = sensorEvent.values[2];

        if(isNotFirstTime) {
            xDiff = Math.abs(lastX - currX);
            yDiff = Math.abs(lastY - currY);
            zDiff = Math.abs(lastZ - currZ);

            if((xDiff > shakeThreshold && yDiff > shakeThreshold) ||
                    (yDiff > shakeThreshold && zDiff > shakeThreshold) ||
                    (xDiff > shakeThreshold && zDiff > shakeThreshold)) {
                Log.i(SHAKE, "Shake has been detected");
                if(shake_flag) {
                    tt_toggle.performClick();
                    Log.i(SHAKE, "Perform click through shake");
                }
            }
        }

        lastX = currX;
        lastY = currY;
        lastZ = currZ;
        isNotFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isAccelerometerAvailable) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerAvailable) {
            sensorManager.unregisterListener(this);
        }
    }
}
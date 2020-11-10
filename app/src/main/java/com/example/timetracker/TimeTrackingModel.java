package com.example.timetracker;

public class TimeTrackingModel {
    // ------------------- CLASS VARIABLES ------------------- \\
    private int year;
    private int month;
    private int day;
    private int minutes;

    // --------------------- CONSTRUCTORS --------------------- \\

    /*  Parameterized Constructor
     *  @param int id, int year, int month, int day, String date, int minutesParams
     *  @return VOID
     */
    public TimeTrackingModel(int year, int month, int day, int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.minutes = minutes;
    }

    /*  Null Constructor */
    public TimeTrackingModel() {}

    // ------------------------ GETTERS ----------------------- \\

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public int getMinutes() { return minutes; }

    // ------------------------ SETTERS ----------------------- \\

    public void setYear(int year) { this.year = year; }

    public void setMonth(int month) { this.month = month; }

    public void setDay(int day) { this.day = day; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    /*  Method to convert Instance variables to simple string
     *  necessary for printing class contents
     */
    @Override
    public String toString() {
        return String.format("TimeTrackingModel{year= %d, month= %d, minutes= %d, day= %d}",
                             year, month, day, minutes);
    }
}

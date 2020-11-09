package com.example.timetracker;

public class TimeTrackingModel {
    // ------------------- CLASS VARIABLES ------------------- \\
    private int id;
    private String date;
    private int minutes;

    // --------------------- CONSTRUCTORS --------------------- \\

    /*  Parameterized Constructor
     *  @param int id, String date, int minutesParams
     */
    public TimeTrackingModel(int id, String date, int minutes) {
        this.id = id;
        this.date = date;
        this.minutes = minutes;
    }

    /*  Null Constructor
     */
    public TimeTrackingModel() {}

    // ------------------------ GETTERS ----------------------- \\

    public int getId() { return id; }

    public String getDate() { return date; }

    public int getMinutes() { return minutes; }

    // ------------------------ SETTERS ----------------------- \\

    public void setId(int id) { this.id = id; }

    public void setDate(String date) { this.date = date; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    /*  Method to convert Instance variables to simple string
     *  necessary for printing class contents
     */
    @Override
    public String toString() {
        return "TimeTrackingModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", minutes=" + minutes +
                '}';
    }
}

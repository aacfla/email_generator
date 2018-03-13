package main.java.Models;

public class BuildPageRequest {
    private String quarter;
    private int week;

    public void setQuarter(String quarter) { this.quarter = quarter; }
    public String getQuarter() { return quarter; }

    public void setWeek(int week) { this.week = week; }
    public int getWeek() { return week; }
}

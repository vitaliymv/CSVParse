package com.example.uploadingfiles.models;

public class CSVModel {

    private String name;
    private String workHours;
    private String date;

    public CSVModel(Object name, Object workHours, Object date) {
        this.name = (String) name;
        this.workHours = (String) workHours;
        this.date = (String) date;
    }



    @Override
    public String toString() {
        return "CSVModel{" +
                "name='" + name + '\'' +
                ", workHours='" + workHours + '\'' +
                ", date='" + date + '\'' +
                '}';
    }



    public String getName() {
        return name;
    }

    public String getWorkHours() {
        return workHours;
    }

    public String getDate() {
        return date;
    }

}

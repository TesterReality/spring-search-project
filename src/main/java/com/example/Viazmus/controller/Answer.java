package com.example.Viazmus.controller;

public class Answer {
    private String[] data;
    private String dataStr;

    public Answer(String[] data) {
        this.data = data;
    }

    public Answer(String dataStr) {
        this.dataStr = dataStr;
    }

    public Answer() {
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }
}
package org.exampledriven.sleuth.rest;


public class SleepRequest {
    private int sleepInMillis;
    private String message;

    public SleepRequest() {
    }

    public SleepRequest(int sleepInMillis, String message) {
        this.sleepInMillis = sleepInMillis;
        this.message = message;
    }

    public int getSleepInMillis() {
        return sleepInMillis;
    }

    public void setSleepInMillis(int sleepInMillis) {
        this.sleepInMillis = sleepInMillis;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

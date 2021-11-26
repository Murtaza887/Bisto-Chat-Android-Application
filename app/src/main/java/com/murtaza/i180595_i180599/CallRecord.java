package com.murtaza.i180595_i180599;

public class CallRecord {
    private String name, time;
    private int image;

    public CallRecord(int image, String name, String time) {
        this.image = image;
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

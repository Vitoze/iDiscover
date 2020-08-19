package com.example.vitib.idiscoverqrcodescanner;

/**
 * Created by Ana on 05/04/2018.
 */

public class Workshop {

    private String id;
    private String name;

    public Workshop(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }
}

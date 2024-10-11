package com.advance.advancesdkdemo;

public class ADManager {
    private static ADManager instance;

    public boolean hasInit = false;

    public static synchronized ADManager getInstance() {
        if (instance == null) {
            instance = new ADManager();
        }
        return instance;
    }


}

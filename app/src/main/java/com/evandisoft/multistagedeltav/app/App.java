package com.evandisoft.multistagedeltav.app;

import com.evandisoft.saneandroidutils.lib.FileIO;

public class App {
    private static App instance;
    Rocket rocket;

    static App getInstance(){
        if(instance==null){
            instance=new App();
        }

        return instance;
    }

    static public boolean instanciated(){
        return instance!=null;
    }

    private App(){
        rocket=new Rocket();
    }
}
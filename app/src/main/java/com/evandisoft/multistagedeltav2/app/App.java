package com.evandisoft.multistagedeltav2.app;

import java.text.DecimalFormat;

public class App {
    private static App instance;


    static App getInstance(){
        if(instance==null){
            instance=new App();
        }

        return instance;
    }

    static public boolean instanciated(){
        return instance!=null;
    }

    DecimalFormat deltavFormat=new DecimalFormat();
    Rocket rocket;

    private App(){
        rocket=new Rocket();
        deltavFormat.setMaximumFractionDigits(0);
    }
}

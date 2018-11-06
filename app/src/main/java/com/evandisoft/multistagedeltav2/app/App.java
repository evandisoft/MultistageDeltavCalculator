package com.evandisoft.multistagedeltav2.app;

import java.text.DecimalFormat;

class App {
    private static App instance;


    static App getInstance(){
        if(instance==null){
            instance=new App();
        }

        return instance;
    }

    static boolean instantiated(){
        return instance!=null;
    }

    final DecimalFormat deltavFormat=new DecimalFormat();
    final Rocket rocket;

    private App(){
        rocket=new Rocket();
        deltavFormat.setMaximumFractionDigits(0);
    }
}

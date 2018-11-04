package com.evandisoft.multistagedeltav2.app;

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

package com.example.jfxclient.dbupdateobserver;

public interface Subject {

    void register(Observer o);
    void unregister(Observer o);
    void notifyObservers();

}

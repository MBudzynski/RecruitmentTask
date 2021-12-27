package com.example.jfxclient.dto;

public class PhysicianIdHolder {

    private Long physicianId;
    private static PhysicianIdHolder instance = null;

    private PhysicianIdHolder() {
    }

    public static PhysicianIdHolder getInstance(){
        if(instance == null){
            synchronized(PhysicianIdHolder.class){
                if(instance == null){
                    instance = new PhysicianIdHolder();
                }
            }
        }
        return instance;
    }


    public Long getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(Long physicianId) {
        this.physicianId = physicianId;
    }

}

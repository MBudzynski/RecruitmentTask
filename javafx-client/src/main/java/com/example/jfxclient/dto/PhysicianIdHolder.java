package com.example.jfxclient.dto;

public class PhysicianIdHolder {

    private Long physicianId;
    public final static PhysicianIdHolder INSTANCE = new PhysicianIdHolder();

    public PhysicianIdHolder() {
    }

    public Long getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(Long physicianId) {
        this.physicianId = physicianId;
    }

}

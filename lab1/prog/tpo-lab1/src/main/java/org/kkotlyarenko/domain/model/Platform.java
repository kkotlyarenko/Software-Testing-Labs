package org.kkotlyarenko.domain.model;

public class Platform {

    private final Building building;
    private Speaker speaker;

    public Platform(Building building) {
        if (building == null) {
            throw new IllegalArgumentException("Building must not be null");
        }
        this.building = building;
        building.setPlatform(this);
    }

    public Building getBuilding() {
        return building;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public boolean hasSpeaker() {
        return speaker != null;
    }
}

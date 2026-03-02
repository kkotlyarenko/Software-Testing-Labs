package org.kkotlyarenko.domain.model;

import org.kkotlyarenko.domain.state.ArthurState;
import org.kkotlyarenko.domain.state.CrowdState;
import org.kkotlyarenko.domain.state.SpeakerState;

public class Scene {

    private final Arthur arthur;
    private final Crowd crowd;
    private final Building building;
    private final Platform platform;
    private final Speaker speaker;

    public Scene(Arthur arthur, Crowd crowd, Building building, Platform platform, Speaker speaker) {
        this.arthur = arthur;
        this.crowd = crowd;
        this.building = building;
        this.platform = platform;
        this.speaker = speaker;
    }

    public Arthur getArthur() {
        return arthur;
    }

    public Crowd getCrowd() {
        return crowd;
    }

    public Building getBuilding() {
        return building;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void play() {
        speaker.startSpeaking(platform);
        crowd.cheer();
        Window target = building.getWindowsOnFloor(2).get(0);
        arthur.startSliding(target);
    }

    public boolean isScenePlayed() {
        return arthur.getState() == ArthurState.SLIDING
                && crowd.getState() == CrowdState.CHEERING
                && speaker.getState() == SpeakerState.SPEAKING;
    }
}

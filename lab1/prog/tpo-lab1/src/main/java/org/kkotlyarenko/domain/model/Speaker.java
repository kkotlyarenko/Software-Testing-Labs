package org.kkotlyarenko.domain.model;

import org.kkotlyarenko.domain.state.SpeakerState;
public class Speaker {

    private final String name;
    private SpeakerState state;
    private Platform platform;

    public Speaker(String name) {
        this.name = name;
        this.state = SpeakerState.IDLE;
    }

    public String getName() {
        return name;
    }

    public SpeakerState getState() {
        return state;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void startSpeaking(Platform platform) {
        if (platform == null) {
            throw new IllegalArgumentException("Platform must not be null");
        }
        this.platform = platform;
        platform.setSpeaker(this);
        this.state = SpeakerState.SPEAKING;
    }

    public void stopSpeaking() {
        this.state = SpeakerState.IDLE;
    }
}

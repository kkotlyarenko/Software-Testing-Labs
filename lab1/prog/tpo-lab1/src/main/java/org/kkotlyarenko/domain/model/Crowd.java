package org.kkotlyarenko.domain.model;

import org.kkotlyarenko.domain.state.CrowdState;
public class Crowd {

    private CrowdState state;

    public Crowd() {
        this.state = CrowdState.QUIET;
    }

    public CrowdState getState() {
        return state;
    }

    public void cheer() {
        this.state = CrowdState.CHEERING;
    }

    public void quiet() {
        this.state = CrowdState.QUIET;
    }
}

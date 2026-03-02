package org.kkotlyarenko.domain.model;

import org.kkotlyarenko.domain.state.ArthurState;
public class Arthur {

    private ArthurState state;
    private Window targetWindow;

    public Arthur() {
        this.state = ArthurState.STANDING;
    }

    public ArthurState getState() {
        return state;
    }

    public Window getTargetWindow() {
        return targetWindow;
    }

    public void startSliding(Window target) {
        if (state != ArthurState.STANDING) {
            throw new IllegalStateException("Arthur can start sliding only from STANDING state");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target window must not be null");
        }
        this.targetWindow = target;
        this.state = ArthurState.SLIDING;
    }

    public void arrive() {
        if (state != ArthurState.SLIDING) {
            throw new IllegalStateException("Arthur can arrive only from SLIDING state");
        }
        this.state = ArthurState.ARRIVED;
    }

    public void land() {
        this.state = ArthurState.STANDING;
        this.targetWindow = null;
    }
}

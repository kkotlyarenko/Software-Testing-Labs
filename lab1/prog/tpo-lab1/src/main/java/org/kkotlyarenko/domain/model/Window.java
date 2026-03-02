package org.kkotlyarenko.domain.model;

public class Window {

    private final int floor;

    public Window(int floor) {
        if (floor <= 0) {
            throw new IllegalArgumentException("Floor must be positive");
        }
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}

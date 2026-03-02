package org.kkotlyarenko.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Building {

    private final List<Window> windows;
    private Platform platform;

    public Building() {
        this.windows = new ArrayList<>();
    }

    public void addWindow(Window window) {
        windows.add(window);
    }

    public List<Window> getWindows() {
        return Collections.unmodifiableList(windows);
    }

    public List<Window> getWindowsOnFloor(int floor) {
        List<Window> result = new ArrayList<>();
        for (Window w : windows) {
            if (w.getFloor() == floor) {
                result.add(w);
            }
        }
        return result;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}

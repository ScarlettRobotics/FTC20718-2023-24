package org.firstinspires.ftc.teamcode.Core;

import java.util.ArrayList;

/** AUTONOMOUS
 * Stores multiple events through .addEvent().
 * An event contains the time when instructions should happen and if the event happened.
 * If wanting to check when an event has happened, use .eventOccurred(). */
public class EventManager {
    // When an event should happen
    ArrayList<Double> timings;
    // If an event has happened
    ArrayList<Boolean> actionTaken;

    public EventManager() {
        timings = new ArrayList<Double>();
        actionTaken = new ArrayList<Boolean>();
    }

    /** Adds an item to the events list to check for new timings.
     * Sorts the item in ArrayList timings to be able to binary search in the future */
    public void addEvent(double timing) {
        int index = 0;
        // Empty list
        if (timings.isEmpty()) {
            timings.add(index, timing);
            actionTaken.add(index, false);
            return;
        }
        // Requested timing is larger than all else
        if (timing > timings.get(timings.size()-1)) {
            index = timings.size()-1;
            timings.add(index, timing);
            actionTaken.add(index, false);
            return;
        }
        // Finds first timing larger than request
        while (timing < timings.get(index+1)) {
            index++;
        }
        timings.add(index, timing);
        actionTaken.add(index, false);
    }

    /** Checks if an event needs to happen with the given timing.
     * If the event does need to happen,
     *  the actionTaken index item is then set to true so make sure that the requested event doesn't happen more than once. */
    public boolean eventOccurred(double currentTime, int index) {
        // Event has already happened
        if (actionTaken.get(index)) {
            return false;
        }
        // Event has yet to happen
        if (currentTime < timings.get(index)) {
            return false;
        }
        // Event needs to happen
        actionTaken.set(index, true);
        return true;
    }
}

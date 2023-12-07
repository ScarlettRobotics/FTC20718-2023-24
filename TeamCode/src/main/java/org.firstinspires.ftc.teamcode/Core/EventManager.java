package org.firstinspires.ftc.teamcode.Core;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

    /** Adds an item to the events list to check for new timings. */
    public void addEvent(double timing) {
        timings.add(timing);
        actionTaken.add(false);
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

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS", "EventManager.java");
        telemetry.addData("timings", timings);
        telemetry.addData("actionTaken", actionTaken);
    }
}

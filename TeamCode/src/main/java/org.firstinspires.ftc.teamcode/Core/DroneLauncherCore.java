package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Core for managing the launching of the drone.
 * Map a button that's hard to reach (eg. D-pad, trigger buttons) to launch the drone to prevent false launches.
 */
public class DroneLauncherCore {
    private Servo droneLauncher;
    public DroneLauncherCore(HardwareMap hardwareMap) {
        droneLauncher = hardwareMap.get(Servo.class, "droneLauncher");
    }

    /** Moves the servo in a way to release the linked rubber band. */
    protected void launch() {
        droneLauncher.setPosition(1);
    }

    /** Moves the servo by the inputted amount from its current position. */
    public void moveByPosition(double v) {
        droneLauncher.setPosition(droneLauncher.getPosition() + v);
    }

    /** Telemetry */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS", "DroneLauncherCore.java");
        telemetry.addData("Servo pos", droneLauncher.getPosition());
    }
}

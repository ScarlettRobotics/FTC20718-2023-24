package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class SystemsManager extends OpMode {
    DrivetrainCore drivetrainCore;

    @Override
    public void init() {
        drivetrainCore = new DrivetrainCore(hardwareMap);
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */
    private double noDrift(double stick, double drift) {
        if (stick < drift) {
            return 0;
        }
        return stick;
    }
}

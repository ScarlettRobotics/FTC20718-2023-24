package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

/*** Manages the drivetrain of the robot.
 * This drivetrain uses an X-drive for the robot, using an ArrayList to init DcMotors.
 * Requires an expansion hub, as all motors will be taken on the control hub. */
public class DrivetrainCore {
    /* Initialization */
    // Drive motors in ArrayList[0-3]
    // Starts at front right, then goes counter-clockwise from top view.
    // This is done to fit the same style as a unit circle.
    private ArrayList<DcMotor> driveMotors;

    // Map motor variables to driver hub
    public DrivetrainCore(HardwareMap hardwareMap) {
        driveMotors = new ArrayList<DcMotor>();
        // hardwareMap
        for (int i=0; i<4; i++) {
            driveMotors.add(hardwareMap.get(DcMotor.class, "driveMotor"+i));
        }

        // runMode
        for (DcMotor i : driveMotors) {
            i.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

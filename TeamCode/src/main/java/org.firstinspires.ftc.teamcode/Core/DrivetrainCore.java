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

        // direction
        // When setting direction for all motors, make sure positive makes robot move clockwise.

    }

    /** Receives four double[] values from -1 to 1 to set appropriate motor powers.
     * Will error if array length is less than 4. */
    protected void setPowers(double[] powers) {
        for (int i=0; i<4; i++) {
            driveMotors.get(i).setPower(powers[i]);
        }
    }

    /** Receives how forward and strafing the robot wants to move, then returns a double[] of the motors doing so.
     * A double[] is returned to work together with the rotate() method.
     * @param strafe > 0 if intention is to strafe right. */
    protected double[] moveCoord(double forward, double strafe) {
        double[] output = {0, 0, 0, 0};
        output[0] -= forward;
        output[1] += forward;
        output[2] += forward;
        output[3] -= forward;

        output[0] += strafe;
        output[1] += strafe;
        output[2] -= strafe;
        output[3] -= strafe;
        return output;
    }
}

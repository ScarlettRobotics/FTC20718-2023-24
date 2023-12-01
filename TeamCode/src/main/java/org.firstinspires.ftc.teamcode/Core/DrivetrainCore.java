package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

/*** Manages the drivetrain of the robot.
 * This drivetrain uses an X-drive for the robot, using an ArrayList to init DcMotors.
 * Requires an expansion hub, as all motors will be taken on the control hub. */
public class DrivetrainCore {
    /* INITIALIZATION */
    // Drive motors in ArrayList[0-3]
    // Starts at front right, then goes counter-clockwise from top view.
    // This is done to fit the same style as a unit circle.
    private ArrayList<PIDController> driveMotors;

    // Map motor variables to driver hub
    public DrivetrainCore(HardwareMap hardwareMap) {
        driveMotors = new ArrayList<PIDController>();
        // hardwareMap
        for (int i=0; i<4; i++) {
            driveMotors.add(new PIDController(hardwareMap, "driveMotor"+i,
                    0, 0, 0, 1));
        }

        // direction
        // When setting direction for all motors, make sure positive makes robot move counter-clockwise.
    }

    /** Sets a new target position based on the current position, moving by the input. */
    public void moveByEncoder(int[] encoders) {
        for (int i=0; i<driveMotors.size(); i++) {
            driveMotors.get(i).moveByEncoder(encoders[i]);
        }
    }

    /** Returns left targetPosition */
    protected int getTargetPositionLeft(int index) {
        return driveMotors.get(index).getTargetPosition();
    }

    /** Updates the PIDController to move towards the provided goal position. */
    public void updateAuto() {
        for (PIDController i : driveMotors) {
            i.update();
        }
    }

    /* FUNCTIONS */
    /** Receives four double[] values from -1 to 1 to set appropriate motor powers.
     * Will error if array length is less than 4. */
    protected void setPowers(double[] powers) {
        for (int i=0; i<4; i++) {
            // squared so movement is slower if needed
            driveMotors.get(i).overridePower(changePower(powers[i]));
        }
    }

    /**
     * Squares the power in the given direction
     * @param power power you want to change
     * @return - squared power
     */
    private double changePower(double power) {
        if (power > 0) return power*power;
        return -1*power*power; // (-)*(-) = (+), so changed by doing (-)*(-)*(-) = (-)
    }

    /** Receives how the robot should move without rotating, then returns a double[] of the motors doing so.
     * A double[] is returned to work together with the rotate() method.
     * @param forward How much the robot should move forward/backward.
     *                Positive makes robot move forward.
     * @param strafe How much the robot should move left/right.
     *               Positive makes robot move right. */
    protected double[] translate(double forward, double strafe) {
        double[] output = new double[4];
        output[0] += forward;
        output[1] -= forward;
        output[2] -= forward;
        output[3] += forward;

        output[0] += strafe;
        output[1] += strafe;
        output[2] -= strafe;
        output[3] -= strafe;
        return output;
    }

    /** Receives how much the robot should rotate, then returns a double[] of the motors doing so.
     * A double[] is returned to work together with the translate() method.
     * @param rotateAmount How much the motors should rotate the robot.
     *                     If positive, robot will move counter-clockwise. */
    protected double[] rotate(double rotateAmount) {
        double[] output = new double[4];
        for (int i=0; i<3; i++) {
            output[i] = rotateAmount;
        }
        return output;
    }

    /** Telemetry */
    protected void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS", "DrivetrainCore.java");
        telemetry.addData("Format", "power direction runMode");
        for (PIDController i : driveMotors) {
            i.telemetry(telemetry);
        }
    }
}

package org.firstinspires.ftc.teamcode.Core;

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
                    0.01, 0.0001, 0.0002, 0.3));
        }

        // direction
        // When setting direction for all motors, make sure positive makes robot move counter-clockwise.
    }

    /** Sets a new target position based on the current position, moving by the input.
     * Moves the drivetrain forward/backwards by the specified amount. */
    public void forwardByEncoder(int encoder) {
        driveMotors.get(0).moveByEncoder(-encoder);
        driveMotors.get(1).moveByEncoder(encoder);
        driveMotors.get(2).moveByEncoder(encoder);
        driveMotors.get(3).moveByEncoder(-encoder);
    }

    /** Sets a new target position based on the current position, moving by the input.
     * Strafes the drivetrain right/left by the specified amount. */
    public void strafeByEncoder(int encoder) {
        driveMotors.get(0).moveByEncoder(-encoder);
        driveMotors.get(1).moveByEncoder(-encoder);
        driveMotors.get(2).moveByEncoder(encoder);
        driveMotors.get(3).moveByEncoder(encoder);
    }

    /** Sets a new target position based on the current position, moving by the input.
     * Strafes the drivetrain right/left by the specified amount. */
    public void rotateByEncoder(int encoder) {
        driveMotors.get(0).moveByEncoder(-encoder);
        driveMotors.get(1).moveByEncoder(-encoder);
        driveMotors.get(2).moveByEncoder(-encoder);
        driveMotors.get(3).moveByEncoder(-encoder);
    }

    /** Returns left targetPosition */
    protected int getTargetPositionLeft(int index) {
        return driveMotors.get(index).getDeltaTargetPosition();
    }

    /** Updates the PIDController to move towards the provided goal position. */
    public void updateAuto() {
        // Done because not every motor has an encoder linked up
        if (driveMotors.get(0).getDeltaTargetPosition() == driveMotors.get(1).getDeltaTargetPosition()) { // strafing/rotating
            driveMotors.get(0).update(driveMotors.get(0).getEncoder());
            driveMotors.get(1).overridePower(driveMotors.get(0).getPower());

            driveMotors.get(2).update(driveMotors.get(2).getEncoder());
            driveMotors.get(3).overridePower(driveMotors.get(2).getPower());
        } else { // forward
            driveMotors.get(0).update(driveMotors.get(0).getEncoder());
            driveMotors.get(1).overridePower(driveMotors.get(2).getPower());

            driveMotors.get(2).update(driveMotors.get(2).getEncoder());
            driveMotors.get(3).overridePower(driveMotors.get(0).getPower());
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
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS", "DrivetrainCore.java");
        telemetry.addData("Format", "power direction runMode");
        for (PIDController i : driveMotors) {
            i.telemetry(telemetry);
        }
    }
}

package org.firstinspires.ftc.teamcode.Core;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/** Operates the claw of the robot.
 * Use Concepts/ClawEncoderFinder to find the position of where the claw should open and close. */
public class ClawCore {
    /* Initialization */
    /** Initialization is done within ClawCore for ease of access. */
    protected Servo leftClaw;
    protected Servo rightClaw;

    // Maps Servo motor variables to driver hub
    public ClawCore (HardwareMap hardwareMap) {
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
    }

    /** Opens the claw to a pre-set value. */
    public void open() {
        rightClaw.setPosition(0.382);
        leftClaw.setPosition(0.039);
    }

    /** Closes the claw to a pre-set value. */
    public void close() {
        rightClaw.setPosition(0.336);
        leftClaw.setPosition(0.124);
    }

    /** Debug method to move claw position by input amount
     * @param left Amount to move leftClaw by
     * @param right Amount to move rightClaw by */
    public void moveByPosition(double left, double right) {
        rightClaw.setPosition(rightClaw.getPosition() + right);
        leftClaw.setPosition(leftClaw.getPosition() + left);
    }

    /** Telemetry in contained in each class for ease of access. */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCurrent class", "ClawCore.java");
        telemetry.addData("Claw Right POS:", rightClaw.getPosition());
        telemetry.addData("Claw Left POS:", leftClaw.getPosition());
    }
}

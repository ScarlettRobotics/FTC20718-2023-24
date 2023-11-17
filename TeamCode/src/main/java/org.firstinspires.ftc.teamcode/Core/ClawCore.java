package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawCore {
    /* Initialization */
    /** Initialization is done within ClawCore for ease of access. */
    protected Servo leftClaw;
    protected Servo rightClaw;

    // Maps Servo motor variables to driver hub
    public ClawCore (HardwareMap hardwareMap) {
        leftClaw = hardwareMap.get(Servo.class, "left_claw");
        rightClaw = hardwareMap.get(Servo.class, "right_claw");
    }

    /** Opens the claw to a pre-set value. */
    public void open() {
        rightClaw.setPosition(0);
        leftClaw.setPosition(0);
    }

    /** Closes the claw to a pre-set value. */
    public void close() {
        rightClaw.setPosition(0);
        leftClaw.setPosition(0);
    }

    /** Telemetry in contained in each class for ease of access. */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCurrent class", "ClawCore.java");
        telemetry.addData("Claw Right POS:", rightClaw.getPosition());
        telemetry.addData("Claw Left POS:", leftClaw.getPosition());
    }
}

package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawCore {
    private Servo leftClaw, rightClaw;

    public ClawCore(HardwareMap hardwareMap) {
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
    }

    public void moveByPosition(double left, double right) {
        leftClaw.setPosition(leftClaw.getPosition() + left);
        rightClaw.setPosition(rightClaw.getPosition() + right);
    }

    protected void open() {
        leftClaw.setPosition(0);
        rightClaw.setPosition(0);
    }

    protected void close() {
        leftClaw.setPosition(1);
        rightClaw.setPosition(1);
    }

    protected void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS", "ClawCore.java");
        telemetry.addData("leftClaw pos:", leftClaw.getPosition());
        telemetry.addData("rightClaw pos:", rightClaw.getPosition());
    }
}

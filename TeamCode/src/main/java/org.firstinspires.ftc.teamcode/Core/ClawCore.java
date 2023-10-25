package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawCore {
    // Initialize claw variables
    private Servo claw;
    private double clawGoalPos;
    private boolean clawIsOpen;

    // Map claw variables to driver hub
    public ClawCore (HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");

        close();
    }

    public void toggle() {
        if (clawIsOpen) {
            close();
            return;
        }
        open();
    }

    public void open() {
        claw.setPosition(.923);
        clawGoalPos = 0.923;
        clawIsOpen = true;
    }

    public void close() {
        claw.setPosition(.721);
        clawGoalPos = 0.721;
        clawIsOpen = false;
    }

    public void move(double strength) {
        clawGoalPos += strength;

        if (clawGoalPos < claw.MIN_POSITION) {
            clawGoalPos = claw.MIN_POSITION;
        }
        if (clawGoalPos > claw.MAX_POSITION) {
            clawGoalPos = claw.MAX_POSITION;
        }
    }

    public void update() {
        claw.setPosition(clawGoalPos);
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCurrent class", "ClawCore.java");
        telemetry.addData("claw position", claw.getPosition());
        telemetry.addData("clawGoalPos", clawGoalPos);
    }
}

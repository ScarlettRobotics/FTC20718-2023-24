package org.firstinspires.ftc.teamcode.Concept;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Core.ClawCore;

public class ClawEncoderFinder extends OpMode {
    public ClawCore clawCore;
    double left, right;

    @Override
    public void init() {
        clawCore = new ClawCore(hardwareMap);
    }

    @Override
    public void loop() {
        left = gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;
        clawCore.moveByPosition(left, right);
    }
}
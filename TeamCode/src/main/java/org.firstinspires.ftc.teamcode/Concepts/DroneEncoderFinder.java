package org.firstinspires.ftc.teamcode.Concepts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DroneLauncherCore;

/** DroneEncoderFinder
 * Serves to find the position that the drone launcher opens at.
 * Uses gamepad1 left joystick to set servo position.
 * Telemetry can be viewed to see good values. */
@TeleOp(name = "DroneEncoderFinder", group = "concepts-encoder")
public class DroneEncoderFinder extends OpMode {
    public DroneLauncherCore droneLauncherCore;
    double left, right;

    @Override
    public void init() {
        droneLauncherCore = new DroneLauncherCore(hardwareMap);
    }

    @Override
    public void loop() {
        double left;
        left = gamepad1.left_stick_y;
        droneLauncherCore.moveByPosition(left/1000);
        droneLauncherCore.telemetry(telemetry);
    }
}
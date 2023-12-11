package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DroneLauncherCore;

/** DroneEncoderFinder
 * Serves to find the position that the drone launcher opens at.
 * Uses gamepad1 left joystick to set servo position.
 * Telemetry can be viewed to see good values. */
@TeleOp(name = "DroneEncoderFinder", group = "concepts-encoder")
public class DroneEncoderFinder extends OpMode {
    // FTC Dashboard
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    // drone
    public DroneLauncherCore droneLauncherCore;
    double left, right;

    @Override
    public void init() {
        // Init FTC Dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // init drone
        droneLauncherCore = new DroneLauncherCore(hardwareMap);
    }

    @Override
    public void loop() {
        double left;
        left = gamepad1.left_stick_y;
        droneLauncherCore.moveByPosition(left/1000);
        // Telemetry
        droneLauncherCore.telemetry(telemetry);
        droneLauncherCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
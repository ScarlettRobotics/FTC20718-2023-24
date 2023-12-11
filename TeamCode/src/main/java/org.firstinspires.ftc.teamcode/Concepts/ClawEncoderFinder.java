package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ClawCore;

/** ClawEncoderFinder
 * Serves to find the positions that leftClaw and rightClaw open and close at.
 * Uses gamepad1 joysticks to set servo positions.
 * Telemetry can be viewed to see good values. */
@TeleOp(name = "ClawEncoderFinder", group = "concepts-encoder")
public class ClawEncoderFinder extends OpMode {
    // FTC Dashboard
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    // claw
    public ClawCore clawCore;
    double left, right;

    @Override
    public void init() {
        // Init FTC Dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // init claw
        clawCore = new ClawCore(hardwareMap);
    }

    @Override
    public void loop() {
        double left, right;
        left = gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;
        clawCore.moveByPosition(left/1000, right/1000);
        // Telemetry
        clawCore.telemetry(telemetry);
        clawCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;

/** Use D-pad to increment the arm by small amounts.
 * Left/right is by small amount, and up/down is by larger amounts.
 * Use FTC dashboard to see how PID variables need to be tweaked. */
@TeleOp(name = "ArmPrecisePIDTest", group = "concepts-pid")
public class ArmPrecisePIDTest extends OpMode {
    // FTC Dashboard
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    // arm
    ArmCore armCore;
    boolean pDpadRight, pDpadUp, pDpadLeft, pDpadDown;
    @Override
    public void init() {
        // Initialize FTC Dashboard variables
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // init arm
        armCore = new ArmCore(hardwareMap);
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "20718");
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_right && !pDpadRight) armCore.moveByEncoder(10);
        if (gamepad1.dpad_left && !pDpadLeft) armCore.moveByEncoder(-10);
        if (gamepad1.dpad_up && !pDpadUp) armCore.moveByEncoder(50);
        if (gamepad1.dpad_down && !pDpadDown) armCore.moveByEncoder(-50);
        armCore.updateAuto();
        // Update prev vars
        pDpadRight = gamepad1.dpad_right;
        pDpadUp = gamepad1.dpad_up;
        pDpadLeft = gamepad1.dpad_left;
        pDpadDown = gamepad1.dpad_down;
        // Telemetry
        armCore.telemetry(telemetry);
        armCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}

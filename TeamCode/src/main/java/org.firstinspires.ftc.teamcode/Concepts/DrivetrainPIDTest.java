package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.DrivetrainCore;

/** Moves the arm to preset locations.
 * Use FTC dashboard to see how PID variables need to be tweaked. */
@TeleOp(name = "DrivetrainPIDTest", group = "concepts-pid")
public class DrivetrainPIDTest extends OpMode {
    // FTC Dashboard
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    // drivetrain
    DrivetrainCore drivetrainCore;
    boolean pDpadUp, pDpadDown, pDpadLeft, pDpadRight;
    @Override
    public void init() {
        // Initialize FTC Dashboard variables
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // init drivetrain
        drivetrainCore = new DrivetrainCore(hardwareMap);
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "22531");
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up && !pDpadUp) drivetrainCore.forwardByEncoder(300);
        if (gamepad1.dpad_down && !pDpadDown) drivetrainCore.forwardByEncoder(-300);
        if (gamepad1.dpad_left && !pDpadLeft) drivetrainCore.strafeByEncoder(300);
        if (gamepad1.dpad_right && !pDpadRight) drivetrainCore.strafeByEncoder(-300);
        drivetrainCore.updateAuto();
        // Telemetry
        drivetrainCore.telemetry(telemetry);
        drivetrainCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
        // update prev vars
        pDpadUp = gamepad1.dpad_up;
        pDpadDown = gamepad1.dpad_down;
        pDpadLeft = gamepad1.dpad_left;
        pDpadRight = gamepad1.dpad_right;
    }
}
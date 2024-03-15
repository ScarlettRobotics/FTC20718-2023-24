package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;

/** Moves the arm to preset locations.
 * Use FTC dashboard to see how PID variables need to be tweaked. */
@TeleOp(name = "ArmPIDTest", group = "concepts-pid")
public class ArmPIDTest extends OpMode {
    // FTC Dashboard
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    // arm
    ArmCore armCore;
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
        if (gamepad1.dpad_left) armCore.setTargetAngle(180);
        if (gamepad1.dpad_up) armCore.setTargetAngle(150);
        if (gamepad1.dpad_right) armCore.setTargetAngle(45);
        if (gamepad1.dpad_down) armCore.setTargetAngle(0);
        armCore.updateAuto();
        // Telemetry
        armCore.telemetry(telemetry);
        armCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}

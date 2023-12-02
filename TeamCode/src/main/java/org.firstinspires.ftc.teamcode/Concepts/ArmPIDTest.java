package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;

/** Moves the arm to preset locations.
 * Use FTC dashboard to see how PID variables need to be tweaked. */
@TeleOp(name = "ArmPIDTest", group = "concepts")
public class ArmPIDTest extends OpMode {
    ArmCore armCore;
    FtcDashboard dashboard;
    Telemetry dashboardTelemetry;
    @Override
    public void init() {
        armCore = new ArmCore(hardwareMap);
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "20718");
        // Initialize FTC Dashboard variables
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) armCore.setTargetPosition(300);
        if (gamepad1.dpad_right) armCore.setTargetPosition(100);
        if (gamepad1.dpad_down) armCore.setTargetPosition(0);
        armCore.updateAuto();
        // Telemetry
        armCore.telemetry(telemetry);
        armCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}

package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DrivetrainCore;
import org.firstinspires.ftc.teamcode.Core.EventManager;

/** Class containing basics of an autonomous class.
 * Don't edit this class directly. Instead, make a copy, then edit the new class.
 * When editing, change the next two lines to an appropriate name for your new autonomous file. */
@Autonomous(name = "TODO", group = "TODO") //TODO
@Disabled //TODO DELETE THIS
public class AutoBoilerplate extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    /* TODO NEEDED CORE CLASSES */

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (eventManager.eventOccurred(timer.time(), 0)) {
                /* TODO INSTRUCTION */
            }

            if (eventManager.eventOccurred(timer.time(), 1)) {
                /* TODO INSTRUCTION */
            }

            if (eventManager.eventOccurred(timer.time(), 2)) {
                /* TODO INSTRUCTION */
            }

            /* TODO etc. */

            addTelemetry(telemetry);
        }
    }

    private void initialize() {
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(2);
        //TODO eventManager.addEvent(3.5);
        //TODO eventManager.addEvent(5);
        /* TODO etc. */
        // Init core classes
        /* TODO INIT CORE CLASSES */
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Close claw to grip pixels
        //clawCore.close(); TODO if using
    }

    private void updateAuto() {
        //drivetrainCore.updateAuto(); TODO
        //armCore.updateAuto(); TODO
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry); //reuse ".telemetry(telemetry)"
        /* TODO CORE TELEMETRY */
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        eventManager.telemetry(dashboardTelemetry); //reuse ".telemetry(dashboardTelemetry)"
        /* TODO CORE TELEMETRY */
        dashboardTelemetry.update();
    }
}
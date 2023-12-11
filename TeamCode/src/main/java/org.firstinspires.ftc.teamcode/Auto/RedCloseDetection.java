package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.*;

/** Used if on the close side of movement. Place where wheels touch right teeth.
 * Places yellow pixel based on prop position, then places purple pixel on backdrop based on prop position. */
@Autonomous(name = "RedCloseDetection", group = "red")
public class RedCloseDetection extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    DrivetrainCore drivetrainCore;
    ArmCore armCore;
    ClawCore clawCore;
    TensorFlowCore tensorFlowCore;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (eventManager.eventOccurred(timer.time(), 0)) {
                /* TODO INSTRUCTION */
            } // look for prop
            if (eventManager.eventOccurred(timer.time(), 1)) {
                /* TODO INSTRUCTION */
            } // find prop, move towards position based on prop, move arm to safe
            if (eventManager.eventOccurred(timer.time(), 2)) {
                /* TODO INSTRUCTION */
            } // rotate based on prop, move arm to ground

            if (eventManager.eventOccurred(timer.time(), 3)) {
                /* TODO INSTRUCTION */
            } // open claw
            if (eventManager.eventOccurred(timer.time(), 4)) {
                /* TODO INSTRUCTION */
            } // slightly move arm up
            if (eventManager.eventOccurred(timer.time(), 5)) {
                /* TODO INSTRUCTION */
            } // close claw

            if (eventManager.eventOccurred(timer.time(), 6)) {
                /* TODO INSTRUCTION */
            } // rotate back, move claw up
            if (eventManager.eventOccurred(timer.time(), 7)) {
                /* TODO INSTRUCTION */
            } // strafe based on prop

            if (eventManager.eventOccurred(timer.time(), 8)) {
                /* TODO INSTRUCTION */
            } // move forward to backdrop
            if (eventManager.eventOccurred(timer.time(), 9)) {
                /* TODO INSTRUCTION */
            } // drop pixel on backdrop
            if (eventManager.eventOccurred(timer.time(), 10)) {
                /* TODO INSTRUCTION */
            } // move backwards, reset arm

            if (eventManager.eventOccurred(timer.time(), 11)) {
                /* TODO INSTRUCTION */
            } // strafe to left square based on prop
            if (eventManager.eventOccurred(timer.time(), 12)) {
                /* TODO INSTRUCTION */
            } // move forwards into park

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
        //TODO eventManager.addEvent(1); // look for prop
        //TODO eventManager.addEvent(1.5); // find prop, move towards position based on prop, move arm to safe
        //TODO eventManager.addEvent(2.5); // rotate based on prop, move arm to ground

        //TODO eventManager.addEvent(3.5); // open claw
        //TODO eventManager.addEvent(3.8); // slightly move arm up
        //TODO eventManager.addEvent(4.1); // close claw

        //TODO eventManager.addEvent(4.4); // rotate back, move claw up
        //TODO eventManager.addEvent(5.5); // strafe based on prop

        //TODO eventManager.addEvent(6.5); // move forward to backdrop
        //TODO eventManager.addEvent(8); // drop pixel on backdrop
        //TODO eventManager.addEvent(8.5); // move backwards, reset arm

        //TODO eventManager.addEvent(9); // strafe to left square based on prop
        //TODO eventManager.addEvent(10.5); // move forwards into park
        // Init core classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        tensorFlowCore = new TensorFlowCore(hardwareMap);
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Close claw to grip pixels
        clawCore.close();
    }

    private void updateAuto() {
        //drivetrainCore.updateAuto(); TODO
        //armCore.updateAuto(); TODO
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry); //reuse ".telemetry(telemetry)"
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        tensorFlowCore.telemetry(telemetry);
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        eventManager.telemetry(dashboardTelemetry); //reuse ".telemetry(dashboardTelemetry)"
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        tensorFlowCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
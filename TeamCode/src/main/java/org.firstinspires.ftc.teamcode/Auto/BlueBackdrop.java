package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DrivetrainCore;
import org.firstinspires.ftc.teamcode.Core.EventManager;

@Autonomous(name = "BlueBackdrop", group = "blue")
public class BlueBackdrop extends LinearOpMode {
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    protected DrivetrainCore drivetrainCore;
    protected ArmCore armCore;
    protected ClawCore clawCore;
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (eventManager.eventOccurred(timer.time(), 0)) {
                drivetrainCore.forwardByEncoder(1000);
            }

            if (eventManager.eventOccurred(timer.time(), 1)) {
                drivetrainCore.rotateByEncoder(650);
            }

            if (eventManager.eventOccurred(timer.time(), 2)) {
                drivetrainCore.forwardByEncoder(1150);
                armCore.setTargetPosition(-1700);
            }

            if (eventManager.eventOccurred(timer.time(), 3)) {
                clawCore.open();
            }

            if (eventManager.eventOccurred(timer.time(), 4)) {
                drivetrainCore.forwardByEncoder(-100);
            }

            if (eventManager.eventOccurred(timer.time(), 5)) {
                drivetrainCore.forwardByEncoder(-100);
            }

            if (eventManager.eventOccurred(timer.time(), 6)) {
                drivetrainCore.strafeByEncoder(-750);
                armCore.setTargetPosition(-500);
            }

            if (eventManager.eventOccurred(timer.time(), 7)) {
                drivetrainCore.forwardByEncoder(300);
            }

            addTelemetry(telemetry);
        }
    }

    private void initialize() {
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(2);
        eventManager.addEvent(3.5);
        eventManager.addEvent(5);
        eventManager.addEvent(7.5);
        eventManager.addEvent(8);
        eventManager.addEvent(8.5);
        eventManager.addEvent(10);
        eventManager.addEvent(11.5);
        // Init core classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Close claw to grip pixels
        clawCore.close();
    }

    private void updateAuto() {
        drivetrainCore.updateAuto();
        armCore.updateAuto();
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry);
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        eventManager.telemetry(dashboardTelemetry);
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.EventManager;
import org.firstinspires.ftc.teamcode.RRdrive.SampleMecanumDrive;

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
    // Core classes
    /* TODO NEEDED CORE CLASSES */

    private void initialize() {
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Close claw to grip pixels
        //clawCore.close(); TODO if using
    }

    /** See https://learnroadrunner.com/trajectories.html for information on how to build a trajectory */
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // The robot's starting position
        Pose2d startPose = new Pose2d(10, -8, Math.toRadians(90));

        drive.setPoseEstimate(startPose); // prevent PID from trying to self correct

        // Trajectories TODO DELETE
        Trajectory myTrajectory = drive.trajectoryBuilder(startPose)
                .strafeRight(10)
                .forward(5)
                .build();

        // Initialization
        initialize(); // telemetry setup

        waitForStart();

        // Auto movement
        if(isStopRequested()) return;

        drive.followTrajectory(myTrajectory);
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        /* TODO CORE TELEMETRY */
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        /* TODO CORE TELEMETRY */
        dashboardTelemetry.update();
    }
}
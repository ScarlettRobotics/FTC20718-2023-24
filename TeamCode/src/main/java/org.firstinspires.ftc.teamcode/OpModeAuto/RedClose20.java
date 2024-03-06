package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.TensorFlowCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.teamcode.RRdrive.SampleMecanumDrive;

import java.util.ArrayList;

/** Uses RoadRunner to score 20 autonomous points (20 from purple).
 * This class only works on RedClose position */
@Autonomous(name = "TODO", group = "TODO") //TODO
public class RedClose20 extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    // Vision
    private VisionPortalCore visionPortalCore;
    private TensorFlowCore tensorFlowCore;
    private AprilTagCore aprilTagCore;
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

    @Override
    public void runOpMode() {
        // vision init
        visionPortalCore = new VisionPortalCore(hardwareMap);
        tensorFlowCore = new TensorFlowCore(hardwareMap, visionPortalCore.builder);
        visionPortalCore.build();
        // drivetrain init
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // The robot's starting position
        Pose2d startPose = new Pose2d(9, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose); // prevent PID from trying to self correct

        // Trajectories
        ArrayList<Trajectory> propTrajectory = new ArrayList<>(); // based on propLocation, place on tape
        propTrajectory.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(16, -52), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(7, -40, Math.toRadians(135)), Math.toRadians(150))
                .build()); // place on left tape
        propTrajectory.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(12, -32), Math.toRadians(90))
                .build()); // place on middle tape
        propTrajectory.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(12, -45), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(17, -40, Math.toRadians(45)), Math.toRadians(0))
                .build()); // place on right tape

        ArrayList<Trajectory> propResetTrajectory = new ArrayList<>(); // reset in front of backdrop
        propResetTrajectory.add(drive.trajectoryBuilder(propTrajectory.get(0).end())
                .strafeTo(new Vector2d(8, -42))
                .splineToConstantHeading(new Vector2d(14, -45), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(45))
                .build());
        propResetTrajectory.add(drive.trajectoryBuilder(propTrajectory.get(1).end())
                .strafeTo(new Vector2d(12, -35))
                .splineToConstantHeading(new Vector2d(24, -38), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(15))
                .build());
        propResetTrajectory.add(drive.trajectoryBuilder(propTrajectory.get(2).end())
                .splineToConstantHeading(new Vector2d(14, -43), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(36, -40), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(90))
                .build());

        // Initialization
        initialize(); // telemetry setup

        int propLocation = 0;
        while (opModeInInit()) { // detect prop while in initialization
            if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                propLocation = 2;
            } else if (tensorFlowCore.getX() > (double) 640/2) { // to right of camera
                propLocation = 1;
            } else { // to right of camera
                propLocation = 0;
            }
        }

        waitForStart();
        visionPortalCore.stopStreaming(); // close portal to save cpu/memory

        if(isStopRequested()) return;

        // Auto movement
        // set purple then move in front of backdrop
        drive.followTrajectory(propTrajectory.get(propLocation));
        drive.followTrajectory(propResetTrajectory.get(propLocation));
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
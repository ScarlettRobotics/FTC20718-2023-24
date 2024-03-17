package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.util.ArrayList;

/** Uses RoadRunner to score 20 autonomous points (20 from purple).
 * This class only works on RedClose position */
@Disabled
@Autonomous(name = "RedClose20", group = "red-close")
public class RedClose20 extends RoadRunnerStarter {
    // Roadrunner variables
    int propLocation;
    Pose2d startPose;
    ArrayList<Trajectory> placePurpleTrajectories;
    ArrayList<Trajectory> purpleToBackdropTrajectories;

    protected void initialize() {
        super.initialize();

        /* TRAJECTORIES */
        // Detected game prop location
        propLocation = 0;

        // The robot's starting position
        startPose = new Pose2d(8.0925, -63.3825, Math.toRadians(90));

        drive.setPoseEstimate(startPose); // prevent PID from trying to self correct

        // Trajectories
        placePurpleTrajectories = new ArrayList<>(); // based on propLocation, place on tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .forward(1)
                .splineToConstantHeading(new Vector2d(16, -52), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(0, -30, Math.toRadians(135)), Math.toRadians(150))
                .build()); // place on left tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(10, -60))
                .splineToConstantHeading(new Vector2d(12, -31), Math.toRadians(90))
                .build()); // place on middle tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(10, -60))
                .splineToConstantHeading(new Vector2d(24, -36), Math.toRadians(90))
                .build()); // place on right tape

        purpleToBackdropTrajectories = new ArrayList<>(); // reset in front of backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(0).end())
                .strafeTo(new Vector2d(3, -33))
                .splineToConstantHeading(new Vector2d(14, -35), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(43, -27, Math.toRadians(0)), Math.toRadians(0))
                .build()); // left purple placement to left backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(1).end())
                .strafeTo(new Vector2d(12, -33))
                .splineToConstantHeading(new Vector2d(24, -42), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(45, -36, Math.toRadians(0)), Math.toRadians(45))
                .build()); // middle purple placement to middle backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(2).end())
                .strafeTo(new Vector2d(24, -38))
                .splineToSplineHeading(new Pose2d(48, -46, Math.toRadians(0)), Math.toRadians(0))
                .build()); // right purple placement to right backdrop
    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();
    }

    protected void detectPropInInit() {
        // Detect prop while in initialization phase
        while (opModeInInit()) {
            if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                propLocation = 2;
            } else if (tensorFlowCore.getX() > (double) 640/2) { // to right of camera
                propLocation = 1;
            } else { // to right of camera
                propLocation = 0;
            }
            tensorFlowCore.telemetry(telemetry);
            telemetry.addData("propLocation", propLocation);
            telemetry.update();
            tensorFlowCore.telemetry(dashboardTelemetry);
            dashboardTelemetry.addData("propLocation", propLocation);
            dashboardTelemetry.update();
        }

        waitForStart();
        visionPortalCore.stopStreaming(); // close portal to save cpu/memory
    }

    protected void placePurple() {
        // Auto movement
        // set purple then move in front of backdrop
        drive.followTrajectory(placePurpleTrajectories.get(propLocation));
        drive.followTrajectory(purpleToBackdropTrajectories.get(propLocation));
    }

}
package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on RedClose position */
@Autonomous(name = "RedClose45", group = "red-close")
public class RedClose45 extends RedClose20 {
    // Saved positions of AprilTags on backdrop
    final protected ArrayList<Vector2d> aprilTagCoords = new ArrayList<>();
    final protected ArrayList<Trajectory> backdropToParkTrajectories = new ArrayList<>();
    protected Pose2d aprilTagPose;

    protected void initialize() {
        super.initialize();

        // blue backdrop
        aprilTagCoords.add(new Vector2d(60.75, 42));
        aprilTagCoords.add(new Vector2d(60.75, 36));
        aprilTagCoords.add(new Vector2d(60.75, 30));
        // red backdrop
        aprilTagCoords.add(new Vector2d(60.75, -30));
        aprilTagCoords.add(new Vector2d(60.75, -36));
        aprilTagCoords.add(new Vector2d(60.75, -42));
        // red pixels
        aprilTagCoords.add(new Vector2d(-72, -36));
        aprilTagCoords.add(new Vector2d(-72, -41.5));
        // blue pixels
        aprilTagCoords.add(new Vector2d(-72, 36));
        aprilTagCoords.add(new Vector2d(-72, 41.5));

        // TODO
        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(0).end())
                .build()); // from left to park
        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(1).end())
                .build()); // from center to park
        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(2).end())
                .build()); // from right to park
    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();

        placeYellow();
    }

    protected void placeYellow() {
        /* Move in front of AprilTag depending on propLocation */
        // Move arm to appropriate position before dropping
        armCore.setTargetAngle(150);
        while (!armCore.atTarget(30)) {
            armCore.updateAuto();
            sleep(10);
        }

        clawCore.open();
        sleep(1000);
        // Move arm to safe position
        armCore.setTargetAngle(45);
        while (!armCore.atTarget(30)) {
            armCore.updateAuto();
            sleep(10);
        }

        // Move to parking
        drive.followTrajectory(backdropToParkTrajectories.get(propLocation));
        //TODO
        // angle 162deg for yellow pixel placement
        // angle 150deg with scuffed arm
        // X coord ~43" when placing
    }

}
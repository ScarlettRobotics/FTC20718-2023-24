package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on BlueClose position */
@Autonomous(name = "BlueClose40", group = "blue-close")
public class BlueClose40 extends BlueClose20 {
    // Saved positions of AprilTags on backdrop
    final ArrayList<Vector2d> aprilTagCoords = new ArrayList<>();
    Trajectory placeOnBackdropTrajectory;
    Pose2d aprilTagPose;

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
    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();
    }

    protected void placeYellow() {
        /* Move in front of AprilTag depending on propLocation */
        // Build trajectory to move to right position
        placeOnBackdropTrajectory = drive.trajectoryBuilder(aprilTagPose)
                .build();
        // Move arm to appropriate position before placing
        armCore.setTargetAngle(142);
        while (!armCore.atTarget(5)) {
            armCore.updateAuto();
            sleep(10);
        }
        //TODO
        // Move to appropriate position
        //TODO
        // angle 162deg for yellow pixel placement
        // angle 142deg with scuffed arm
        // X coord ~43" when placing
    }

}
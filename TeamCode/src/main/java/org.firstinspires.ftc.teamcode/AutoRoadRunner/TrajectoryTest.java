package org.firstinspires.ftc.teamcode.AutoRoadRunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RRdrive.SampleMecanumDrive;

// Sample of how we can build a trajectory for a robot to follow

public class TrajectoryTest extends LinearOpMode {
    @Override
    public void runOpMode(){
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        // TODO: Tune the setPoseEstimate for custom pose and tune the values for the trajectories
        drive.setPoseEstimate(new Pose2d());

        // Main Trajectory Building Part
        // Turn the second parameter in 'trajectoryBuilder' to 'true' if we want the path to be followed in reverse
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d()) // Pose2d() Assumes position at the origin (0,0) and facing 0 degrees
                /*
                * here we can add more methods to add to the trajectories,
                * but they need to be spline paths to retain continuity or
                * add a second trajectory object whenever the robot stops or reverses in direction
                */
                .splineTo(new Vector2d(10, 40), Math.toRadians(90))
                .build(); // return the Trajectory object

        // Example of adding a second trajectory
        Trajectory traj2 = drive.trajectoryBuilder(traj1.end()) // Pose2d() Assumes position at the origin (0,0) and facing 0 degrees
                .splineTo(new Vector2d(-10, -15), Math.toRadians(270))
                .build(); // return the Trajectory object

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
    }
}

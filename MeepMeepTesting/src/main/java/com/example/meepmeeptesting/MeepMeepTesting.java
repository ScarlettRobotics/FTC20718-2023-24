package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot dimensions: width, height
                .setDimensions(16.5,17.75)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(56, 60, Math.toRadians(172.25564966005075), Math.toRadians(0), 10.4)
                .followTrajectorySequence(drive ->

                        drive.trajectorySequenceBuilder(new Pose2d(-39.9075, -63.3825, Math.toRadians(90)))

                                .splineTo(new Vector2d(0, -57), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(10, -54, Math.toRadians(30)), Math.toRadians(30))
                                .splineTo(new Vector2d(42, -34), Math.toRadians(0))
                                .build()

                                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)

                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}

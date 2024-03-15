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
                .setDimensions(14.75,14.75)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(56, 60, Math.toRadians(172.25564966005075), Math.toRadians(0), 10.4)
                .followTrajectorySequence(drive ->

                        //drive.trajectorySequenceBuilder(new Pose2d(-35, -34, Math.toRadians(90)))
                        drive.trajectorySequenceBuilder(new Pose2d(8.0925, -63.3825, Math.toRadians(90)))
                                .strafeTo(new Vector2d(10, -60))
                                .splineToConstantHeading(new Vector2d(23, -40), Math.toRadians(90))
                                .build() // place on right tape

                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)

                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}

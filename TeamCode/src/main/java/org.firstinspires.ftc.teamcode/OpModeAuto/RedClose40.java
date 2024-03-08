package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on RedClose position */
@Autonomous(name = "RedClose40", group = "red-close")
public class RedClose40 extends RedClose20 {

    protected void initialize() {
        super.initialize();
    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();
    }

}
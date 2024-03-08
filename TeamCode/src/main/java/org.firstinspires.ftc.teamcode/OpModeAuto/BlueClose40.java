package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on BlueClose position */
@Autonomous(name = "BlueClose40", group = "blue-close")
public class BlueClose40 extends BlueClose20 {

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
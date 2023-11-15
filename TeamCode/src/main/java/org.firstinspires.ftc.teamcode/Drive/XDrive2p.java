package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Core.SystemsManager;


/**
 * XDrive 2 Player
 * Designed for driving to be on controller 1, upper systems on controller 2.
 * X drive. Left stick controls coordinate movement, right stick controls rotation.
 * See ClawCore.java and SlideCore.java for information regarding the claw and slide.
 */
@TeleOp(name = "XDrive2P", group = "x")
public class XDrive2p extends SystemsManager {
    @Override
    public void loop() {
        telemetry.addData("STATUS: ", "Running");
        updateDrivetrain(1);
        updateArm(1);
        telemetry(telemetry);
    }
}

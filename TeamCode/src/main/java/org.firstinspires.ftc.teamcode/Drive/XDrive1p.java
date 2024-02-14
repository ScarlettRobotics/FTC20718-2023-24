package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Core.SystemsManager;


/**
 * XDrive 1 Player
 * Designed for driving and upper systems to be on controller 1.
 * X drive. Left stick controls coordinate movement, right stick controls rotation.
 * See ClawCore.java and SlideCore.java for information regarding the claw and slide.
 */
@TeleOp(name = "XDrive1P", group = " x")
public class XDrive1p extends SystemsManager {
    @Override
    public void loop() {
        telemetry.addData("STATUS: ", "Running");
        updateIMU();
        updateDrivetrain(1);
        updateDrivetrainAligner(gamepad1.y);
        updateArm(gamepad1.left_trigger, gamepad1.right_trigger);
        updateClaw(gamepad1.a, gamepad1.b);
        checkForDroneLaunch(gamepad1.left_bumper);
        telemetry(telemetry);
    }
}

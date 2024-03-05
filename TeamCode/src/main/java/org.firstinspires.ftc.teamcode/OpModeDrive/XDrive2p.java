package org.firstinspires.ftc.teamcode.OpModeDrive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Core.SystemsManager;


/**
 * XDrive 2 Player
 * Designed for driving to be on controller 1, upper systems on controller 2.
 * X drive. Left stick controls coordinate movement, right stick controls rotation.
 * See ClawCore.java and SlideCore.java for information regarding the claw and slide.
 */
@TeleOp(name = "XDrive2P", group = " x")
public class XDrive2p extends SystemsManager {
    @Override
    public void loop() {
        telemetry.addData("STATUS: ", "Running");
        updateIMU();
        updateDrivetrainAligner(gamepad1.y);
        updateDrivetrain(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,
                gamepad1.dpad_up, gamepad1.dpad_down,
                gamepad1.dpad_left, gamepad1.dpad_right);
        updateArm(gamepad2.left_trigger, gamepad2.right_trigger);
        updateClaw(gamepad2.a, gamepad2.b);
        checkForDroneLaunch(gamepad1.left_bumper);
        telemetry(telemetry);
    }
}

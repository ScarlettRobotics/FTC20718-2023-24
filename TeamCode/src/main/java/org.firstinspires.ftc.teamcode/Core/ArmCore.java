package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Operates the arm of the robot.
 * Current, only setPower() and telemetry() are useful.
 * The current mode is RUN_USING_ENCODER, making multiple methods unusable.
 * Methods that don't work: getTargetPosition(), goToEncoder(), moveByEncoder()
 * */
public class ArmCore {
    private DcMotor armMotor;

    ArmCore(HardwareMap hardwareMap) {
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /** Returns the target position of the arm. */
    protected int getTargetPosition() {
        return armMotor.getTargetPosition();
    }

    /** Sets the arm to move to the inputted encoder position. */
    protected void goToEncoder(int encoder) {
        armMotor.setTargetPosition(encoder);
    }

    /** Sets the arm to change the target encoder position by the input. */
    protected void moveByEncoder(int encoder) {
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(encoder);
        armMotor.setPower(0.5);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /** Moves the arm motor as a */
    protected void setPower(double power){
        armMotor.setPower(power);

    }
    //protected void update() {
        //armMotor.setPower(0.5);
    //}


    protected void telemetry(Telemetry telemetry) {
        telemetry.addData("CURRENT CLASS", "ArmCore.java");
        telemetry.addData("runMode", armMotor.getMode());
        if (armMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            telemetry.addData("targetPosition", armMotor.getTargetPosition());
            telemetry.addData("currentPosition", armMotor.getCurrentPosition());
        }
        telemetry.addData("power", armMotor.getPower());
    }
}

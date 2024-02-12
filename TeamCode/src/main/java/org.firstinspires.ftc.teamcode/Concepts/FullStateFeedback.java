package org.firstinspires.ftc.teamcode.Concepts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

// Full state feedback control allows us to control each relevant state of our system.
// Combine with feedforward control for robust control, get target pos, vel, and acc, using motion profiling
public class FullStateFeedback {
    DcMotorEx motor;
    /**
     * Calculate state feedback for position and velocity of our system.
     */
    public double calculateStateFeedback(double targetPosition, double targetVelocity, double robotPosition, double robotVelocity) {

        double positionError = targetPosition - robotPosition; // motorName.getCurrentPosition();
        double velocityError = targetVelocity - robotVelocity;// motorName.getVelocity();
        double u = (positionError * k1) + (velocityError * k2);//
        return u;
    }

    public double calculateFeedforward(double targetVelocity, double targetAcceleration){
        return (targetVelocity +Kv) + (targetAcceleration + Ka);
    }


    // Full state feedback + feed forward = ULTIMATE CONTROL
    public void commandMotor(double targetPosition, double targetVelocity, double targetAcceleration){
        double feedbackCommand = calculateStateFeedback(targetPosition, targetVelocity, motor.getCurrentPosition(), motor.getPower());
        double feedforwardCommand = calculateFeedforward(targetVelocity, targetAcceleration);
        motor.setPower(feedforwardCommand + feedforwardCommand);
    }

    // to get target position, velocity and acceleration we use motion profiling

    // example of using motion profile with fsf&feedforward
    /*
    while (TrajectoryIsNotDone) {
        double x = motion_profile_position(max_acceleration, max_velocity, distance, elapsed_time);
        double v = motion_profile_velo(max_acceleration, max_velocity, distance, elapsed_time);
        double a = motion_profile_accel(max_acceleration, max_velocity, distance, elapsed_time);

        commandMotor(x, v, a);
    }
     */
}

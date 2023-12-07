package org.firstinspires.ftc.teamcode.Concepts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Core.WebcamCore;

@TeleOp(name = "CameraStreamTest", group = "concepts")
public class CameraStreamTest extends OpMode {
    WebcamCore webcamCore;
    @Override
    public void init() {
        webcamCore = new WebcamCore(hardwareMap);
    }

    @Override
    public void loop() {}
}

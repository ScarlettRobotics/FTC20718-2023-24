package org.firstinspires.ftc.teamcode.Concepts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;

@TeleOp(name = "CameraStreamTest", group = "concepts-vision")
public class CameraStreamTest extends OpMode {
    VisionPortalCore visionPortalCore;
    @Override
    public void init() {
        visionPortalCore = new VisionPortalCore(hardwareMap);
        visionPortalCore.build();
    }

    @Override
    public void loop() {}
}

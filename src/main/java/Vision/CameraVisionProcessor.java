/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vision;

import Modelo.NodeType;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
/**
 *
 * @author JuanA
 */
public class CameraVisionProcessor implements VisionProcessor {

    private VideoCapture camera;
    private boolean active;

    @Override
    public boolean initialize() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        camera = new VideoCapture(0);
        active = camera.isOpened();

        if (active) {
            System.out.println("Cámara iniciada correctamente");
        } else {
            System.out.println("No se pudo iniciar la cámara");
        }

        return active;
    }

    @Override
    public DetectionResult processFrame() {

        if (!active) {
            return DetectionResult.empty();
        }

        Mat frame = new Mat();
        camera.read(frame);

        if (frame.empty()) {
            return DetectionResult.empty();
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

        double brightness = Core.mean(gray).val[0];

        if (brightness < 50) {
            return new DetectionResult(
                    NodeType.OBSTACLE,
                    0.80,
                    frame.width() / 2,
                    frame.height() / 2,
                    "Posible obstáculo detectado"
            );
        }

        return DetectionResult.empty();
    }

    @Override
    public void release() {

        if (camera != null && camera.isOpened()) {
            camera.release();
        }

        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}

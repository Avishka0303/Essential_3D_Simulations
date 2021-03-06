package chapter2;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Camera3D extends Application {

    private static final int RADIUS = 100;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //create a sphere
        Sphere sphere=new Sphere(RADIUS);

        //set a camera.
        Camera camera=new PerspectiveCamera(true); //to get the camera to 0,0,0

        Group group=new Group();

        //add to the group.
        group.getChildren().add(sphere);

        //translate the x and y properties.
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-1000); //to get the camera out of the sphere.

        //set the range of the camera.
        camera.setNearClip(1);
        camera.setFarClip(10000);

        //set for key code...
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,event->{
            switch (event.getCode()){
                case W:camera.translateZProperty().set(camera.getTranslateZ()+10);
                        break;
                case S:camera.translateZProperty().set(camera.getTranslateZ()-10);
            }
        });



        Scene scene=new Scene(group,WIDTH, HEIGHT);
        //set a camera to the scene
        scene.setCamera(camera);

        primaryStage.setScene(scene);
        primaryStage.setTitle("First 3D sphere");
        primaryStage.show();
    }
}

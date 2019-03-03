package chapter3;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Rotation3D extends Application {

    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;

    //to get the currently clicked x and y value.
    private double anchorX,anchorY;

    //to get the angles.
    private double anchorAngleX=0;
    private double anchorAngleY=0;

    //for binding the angles.
    private final DoubleProperty angleX=new SimpleDoubleProperty(0);
    private final DoubleProperty angleY=new SimpleDoubleProperty(0);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //create a box
        Box box=prepareBox();//new Box(100,20,50);
        SmartGroup group=new SmartGroup();
        group.getChildren().add(box);

        //set a camera.
        Camera camera=new PerspectiveCamera(); //to get the camera to 0,0,0

        //set scene.
        Scene scene=new Scene(group,WIDTH, HEIGHT);

        //translate the x and y properties.
        group.translateXProperty().set(WIDTH/2);
        group.translateYProperty().set(HEIGHT/2);
        group.translateZProperty().set(-1200); //to get the camera out of the sphere.

        //initializing mouse control
        initMouseControl(group,scene,primaryStage);

        //transform
        Transform transform=new Rotate(65,new Point3D(1,0,0));
        box.getTransforms().add(transform);

        //set for key code...
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,event->{
            switch (event.getCode()){
                case W:box.translateZProperty().set(box.getTranslateZ()+100);
                        break;
                case S:box.translateZProperty().set(box.getTranslateZ()-100);
                        break;
                case UP:group.rotateByY(10);
                        break;
                case DOWN:group.rotateByY(-10);
                        break;
                case LEFT:group.rotateByX(10);
                    break;
                case RIGHT:group.rotateByX(-10);
                    break;
                case Z:group.rotateByZ(10);
                    break;
                case X:group.rotateByZ(-10);
                    break;
            }
        });


        //set a camera to the scene
        scene.setCamera(camera);
        scene.setFill(Color.SILVER);
        primaryStage.setScene(scene);
        primaryStage.setTitle("First 3D sphere");
        primaryStage.show();
    }

    private Box prepareBox() {
        PhongMaterial material=new PhongMaterial();
        //material.setDiffuseColor(Color.GREENYELLOW);
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/Resources/dark.jpeg")));
        Box box=new Box(100,20,50);
        box.setMaterial(material);
        return box;
    }

    private void initMouseControl(SmartGroup group, Scene scene,Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate=new Rotate(0,Rotate.X_AXIS),
                yRotate=new Rotate(0,Rotate.Y_AXIS)
        );

        //bind with the angle property.
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            //get the current x and y values.
            anchorX=event.getSceneX();
            anchorY=event.getSceneY();
            //store the current angles.
            anchorAngleX=angleX.get();
            anchorAngleY=angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX-(anchorY-event.getSceneY()));
            angleY.set(anchorAngleY+(anchorX-event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL,event -> {
            double movement=event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ()+movement);
        });
    }

    class SmartGroup extends Group{
        Rotate r;
        Transform t=new Rotate();

        void rotateByX(int ang){
            r=new Rotate(ang,Rotate.X_AXIS);
            t=t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang){
            r=new Rotate(ang,Rotate.Y_AXIS);
            t=t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByZ(int ang){
            r=new Rotate(ang,Rotate.Z_AXIS);
            t=t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}

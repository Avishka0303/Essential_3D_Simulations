package chapter3;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
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
        group.getChildren().add(prepareBox2());
        //add a new point light.//but the system get darker.
        //group.getChildren().add(new PointLight());
        group.getChildren().addAll(prepareLightSource());
        //group.getChildren().add(new AmbientLight());
       // group.getChildren().add(new PointLight());



        //set a camera.
        Camera camera=new PerspectiveCamera(); //to get the camera to 0,0,0
        //camera.setNearClip(2);
        //camera.setFarClip(1000);
        //camera.translateZProperty().set(-200);

        //set scene.
        Scene scene=new Scene(group,WIDTH, HEIGHT,true);

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

        AnimationTimer animationTimer=new AnimationTimer() {
            @Override
            public void handle(long now) {
                pointLight.setRotate(pointLight.getRotate()+1);
            }
        };
        animationTimer.start();
    }

    private Node prepareBox2() {
        PhongMaterial material=new PhongMaterial();
        //material.setDiffuseColor(Color.GREENYELLOW);
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/Resources/dark.jpeg")));

        //reflection map.
        //material.setSpecularMap(new Image(getClass().getResourceAsStream("/Resources/bump3.jpg")));

        //add self illuminations
        //material.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/Resources/bump2.jpg")));

        //reflect when white .
        //material.setSpecularColor(Color.WHITE);

        //set bump
        material.setBumpMap(new Image(getClass().getResourceAsStream("/Resources/bump4.jpg")));

        Box box=new Box(20,100,50);
        box.setMaterial(material);
        return box;
    }

    private final PointLight pointLight=new PointLight();

    private Node[] prepareLightSource() {
/*      AmbientLight ambientLight=new AmbientLight();
        ambientLight.setColor(Color.AQUA);
        return ambientLight;*/
        //PointLight pointLight=new PointLight();
        pointLight.setColor(Color.RED);
        pointLight.getTransforms().add(new Translate(0,50,100));
        pointLight.setRotationAxis(Rotate.X_AXIS);

        //add a sphere into the place of point light.
        Sphere sphere=new Sphere(2);
        sphere.getTransforms().setAll(pointLight.getTransforms());
        sphere.rotateProperty().bind(pointLight.rotateProperty());
        sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());
        return new Node[]{pointLight,sphere};
    }

    private Box prepareBox() {
        PhongMaterial material=new PhongMaterial();
        //material.setDiffuseColor(Color.GREENYELLOW);
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/Resources/dark.jpeg")));

        //reflection map.
        //material.setSpecularMap(new Image(getClass().getResourceAsStream("/Resources/bump3.jpg")));

        //add self illuminations
        //material.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/Resources/bump2.jpg")));

        //reflect when white .
        //material.setSpecularColor(Color.WHITE);

        //set bump
        material.setBumpMap(new Image(getClass().getResourceAsStream("/Resources/bump4.jpg")));

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

//ambiant light  is for all directions
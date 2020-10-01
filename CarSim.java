import java.io.FileInputStream;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class CarSim extends Application {

    private static final double W = 1000, H = 1000;

    private static final String CAR_IMAGE =
            "https://img.icons8.com/color/2x/f1-race-car-top-veiw.png";
    
    private static final String BAD_CAR_IMAGE = "https://images-ext-1.discordapp.net/external/8m41qiXkILIWLUg495Ysx9OnVkKWDHdMcsnGnm1Urmc/https/img.icons8.com/officel/2x/truck-top-view.png";
    
    private static final String BACK_IMAGE = "https://images-ext-1.discordapp.net/external/TPR7IrckC7Tc9gPIgofWzXsYJpm-VlRzChpaMJMffYU/https/www.ajaybadgujar.com/wp-content/uploads/2014/06/road.png?widt";
    
    private Image carImage;
    private Node  gCar;
    
    private Image backImage;
    private Node back;
    
    private Image badCar;
    private Node bad;

    boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) throws Exception {
        carImage = new Image(CAR_IMAGE);
        gCar = new ImageView(carImage);
        gCar.setRotate(270);
        
        backImage = new Image(BACK_IMAGE);
        back = new ImageView(backImage);
      
        badCar = new Image(BAD_CAR_IMAGE);
        bad = new ImageView(badCar);
        bad.setRotate(90);
        
        Group dungeon = new Group(gCar, bad);

        moveGCarTo(W / 2, H / 2);
        moveBCarTo(W/2 ,H-(3*H/4));
        
        Scene scene = new Scene(dungeon, W, H, Color.FORESTGREEN);
        ImagePattern pattern = new ImagePattern(backImage);
        scene.setFill(pattern);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                    case SHIFT: running = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;
                    case SHIFT: running = false; break;
                }
            }
        });

        stage.setScene(scene);
       
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int dx = 0, dy = 0;

                if (goNorth) dy -= 1;
                if (goSouth) dy += 1;
                if (goEast)  dx += 1;
                if (goWest)  dx -= 1;
                if (running) { dx *= 3; dy *= 3; }

                moveGCarBy(dx, dy);
                moveBCarBy(0, 1);
            }
        };
        timer.start();
    }

    private void moveGCarBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = gCar.getBoundsInLocal().getWidth()  / 2;
        final double cy = gCar.getBoundsInLocal().getHeight() / 2;

        double x = cx + gCar.getLayoutX() + dx;
        double y = cy + gCar.getLayoutY() + dy;

        moveGCarTo(x, y);
    }
    
    private void moveBCarBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = bad.getBoundsInLocal().getWidth()  / 2;
        final double cy = bad.getBoundsInLocal().getHeight() / 2;

        double x = cx + bad.getLayoutX() + dx;
        double y = cy + bad.getLayoutY() + dy;

        moveBCarTo(x, y);
    }

    private void moveGCarTo(double x, double y) {
        final double cx = gCar.getBoundsInLocal().getWidth()  / 2;
        final double cy = gCar.getBoundsInLocal().getHeight() / 2;

        if (x - cx >= 0 &&
            x + cx <= W &&
            y - cy >= 0 &&
            y + cy <= H) {
        	gCar.relocate(x - cx, y - cy);
        }
    }
    
    private void moveBCarTo(double x, double y) {
        final double cx = bad.getBoundsInLocal().getWidth()  / 2;
        final double cy = bad.getBoundsInLocal().getHeight() / 2;

        if (x - cx >= 0 &&
            x + cx <= W &&
            y - cy >= 0 &&
            y + cy <= H) {
            bad.relocate(x - cx, y - cy);
        }
    }

    public static void main(String[] args) { launch(args); }
} 
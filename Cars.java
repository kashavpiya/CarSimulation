import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class Cars extends Application {

    private static final double W = 512, H = 512;

    private static final String CAR_IMAGE =
            "https://img.icons8.com/color/2x/f1-race-car-top-veiw.png";
    
   
    private static final String BACK_IMAGE = "https://images-ext-1.discordapp.net/external/TPR7IrckC7Tc9gPIgofWzXsYJpm-VlRzChpaMJMffYU/https/www.ajaybadgujar.com/wp-content/uploads/2014/06/road.png?widt";
    
    private static final String BACK_IMAGE2 = "https://images-ext-1.discordapp.net/external/TPR7IrckC7Tc9gPIgofWzXsYJpm-VlRzChpaMJMffYU/https/www.ajaybadgujar.com/wp-content/uploads/2014/06/road.png?widt";
    
    private static final String GAME_OVER = "https://www.flaticon.com/premium-icon/icons/svg/2066/2066796.svg";
    
  //  private static final String GAME_OVER = "http://pngimg.com/uploads/game_over/game_over_PNG51.png";
    private boolean crashed = false;
    private Image carImage;
    private Node  gCar;
    
    private Image backImage;
    private Node back;
    
    private Image backImage2;
    private Node back2;
    
    private Image gameOver;
    private Node over;
    
    private int count = 0;
    
    boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) throws Exception {
    	
    	
    	carImage = new Image(CAR_IMAGE);
        gCar = new ImageView(carImage);
        gCar.setRotate(270);
        
        backImage = new Image(BACK_IMAGE);
        back = new ImageView(backImage);
        
        backImage2 = new Image(BACK_IMAGE);
        back2 = new ImageView(backImage);
      
        gameOver = new Image(GAME_OVER);
        over = new ImageView(gameOver);
       
        Group root = new Group(back, back2, gCar, over);
        
        moveGCarTo(W/2, 3 * H / 4);
        moveBgTo(0 , H/2);
        moveBg2To(0, -H/2);
        moveOverTo(W, H);
     
  
        
        Scene scene = new Scene(root, W, H, Color.FORESTGREEN);
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

                if (goNorth) dy -= 3;
                if (goSouth) dy += 3;
                if (goEast)  dx += 3;
                if (goWest)  dx -= 3;
                if (running) { dx *= 3; dy *= 3; }

                moveGCarBy(dx, dy);
                moveBgBy(dx,-dy);
                moveBg2By(dx, -dy);
          //      moveBCarBy(0, 1);
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

        moveGCarTo(x, 3 * H/4);
    }
    
    private void moveBgBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;
        
        if (dy < 0) return;
        
        final double cy = back.getBoundsInLocal().getHeight() / 2;

        double x = back.getLayoutX() + dx;
        double y = cy + back.getLayoutY() + dy;

        moveBgTo(0, y);
    }
    
    private void moveBg2By(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        if (dy < 0) return;
        
        final double cy = back2.getBoundsInLocal().getHeight() / 2;

        double x = back2.getLayoutX() + dx;
        double y = cy + back2.getLayoutY() + dy;

        moveBg2To(0, y);
    }
    

    private void moveGCarTo(double x, double y) {
        final double cx = gCar.getBoundsInLocal().getWidth()  / 2;
        final double cy = gCar.getBoundsInLocal().getHeight() / 2;
        
        boolean accident = isOutOfBounds(x);
        if (accident == true) {
        	try {
				theEnd();
				
		//		System.exit(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if (x - cx >= 0 &&
            x + cx <= W &&
            y - cy >= 0 &&
            y + cy <= H) {
        	gCar.relocate(x - cx, y - cy);
        } 
    }
    
    private void moveBgTo(double x, double y) {
       // final double cx = back.getBoundsInLocal().getWidth()  / 2;
        final double cy = back.getBoundsInLocal().getHeight() / 2;

     //   if (y - cy >= 0 &&
     //       y + cy <= H) {
        if (y -cy >= H) {
        	back.relocate(0, 0);
        } else {
        	back.relocate(0 , y - cy);
        	
        }
    }
    
    private void moveBg2To(double x, double y) {
        // final double cx = back.getBoundsInLocal().getWidth()  / 2;
         final double cy = back2.getBoundsInLocal().getHeight() / 2;

      //   if (y - cy >= 0 &&
      //       y + cy <= H) {
         if (y -cy >= 0) {
         	back2.relocate(0, -H);
         } else {
         	back2.relocate(0 , y - cy);
         	
         }
     }
    
    private void moveOverTo(double x, double y) {
        over.relocate(x,y);
      }
    
    private boolean isOutOfBounds(double x) {
    	if (x > 384 || x < 128) {
    		crashed = true;
    		return true;
    	}else {
    		return false;
    	}
    }
    
    private void theEnd() throws InterruptedException{
    	crashed = true;
    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("GAME OVER!");
    	alert.setHeaderText("You lost the game! Try again.");
    	alert.setContentText("You just went off the road!");
    	
    	alert.show();
    	

    	count += 1;
    	
    	if (count == 100) {
    		System.exit(0);
    	}
    	
    }
  
  
    public static void main(String[] args) { launch(args); }
} 

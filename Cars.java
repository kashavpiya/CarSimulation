//import java.io.FileInputStream;
import java.util.Random;
//import java.util.concurrent.TimeUnit;

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

    private static final double W = 512, H = 1024;

    private static final String CAR_IMAGE = "https://img.icons8.com/color/2x/f1-race-car-top-veiw.png";
    
    private static final String BACK_IMAGE = "https://images-ext-1.discordapp.net/external/TPR7IrckC7Tc9gPIgofWzXsYJpm-VlRzChpaMJMffYU/https/www.ajaybadgujar.com/wp-content/uploads/2014/06/road.png?widt";
    
    private static final String BAD_CAR = "https://images-ext-1.discordapp.net/external/8m41qiXkILIWLUg495Ysx9OnVkKWDHdMcsnGnm1Urmc/https/img.icons8.com/officel/2x/truck-top-view.png";
    
    private static final String HACK = "https://icons.iconarchive.com/icons/iron-devil/ids-game-world/32/Danger-zone-icon.png";
    
    
    private Image hackImage;
    private Node hacker;   
    
    private Image carImage;
    private Node  gCar;
    
    private Image badCar;
    private Node bCar;
    
    private Image backImage;
    private Node back;
    private Node back2;
    private Node back3;
    private Node back4;
    
    private int count = 0;
    private int count2 = 0;
    
    private int score = 0;
    
    private boolean hacked = false;
    private int hackCount = 0;
    
    private double gCarX;
    private double gCarY;
    private double bCarX;
    private double bCarY;
    private double hackerX;
    private double hackerY;
    
    private boolean crashed = false;
    
    boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) throws Exception {
    	
    	instructions();
    	
    	carImage = new Image(CAR_IMAGE);
        gCar = new ImageView(carImage);
        gCar.setRotate(270);
        
        backImage = new Image(BACK_IMAGE);
        back = new ImageView(backImage);

        back2 = new ImageView(backImage);

        back3 = new ImageView(backImage);

        back4 = new ImageView(backImage);

        badCar = new Image(BAD_CAR);
        bCar = new ImageView(badCar);
        bCar.setRotate(90);
        
        hackImage = new Image(HACK);
        hacker = new ImageView(hackImage);
        
        Group root = new Group(back, back2, back3, hacker, gCar, bCar);

        //moving all the objects to its default positions
        moveGCarTo(W/2, 3 * H / 4);
        moveBgTo(0 , -H / 4);
        moveBg2To(0, H / 4);
        moveBg3To(0, 3* H / 4);
        moveBg4To(0, 5 * H/ 4);
        
        //the bad car and its hacker component moved to random starting position
        Random rand = new Random();
        moveBCarTo(rand.nextInt(245) + 120,-100);
        moveHackerTo(rand.nextInt(245)+120, 0);
  
        
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
                
                if (hacked) { 
                	dx *= 3; dy *= 3; 
                }   
             
                moveGCarBy(dx, dy); 
                moveBgBy(dx,-dy);
                moveBg2By(dx, -dy);      
                moveBg3By(dx, -dy);   
                moveBg4By(dx, -dy);  
                moveBCarBy(0, -dy * 2);               
                moveHackerBy(0, -dy);
                             
                if (crashed == true) {
                	stop();
                }
                
            }
        };
        
        		timer.start();
  
    }
    

    private void moveGCarBy(int dx, int dy) {
    	//calls the function which changes the location of the driving car
    	
        if (dx == 0 && dy == 0) return;

        final double cx = gCar.getBoundsInLocal().getWidth()  / 2;
        final double cy = gCar.getBoundsInLocal().getHeight() / 2;

        double x = cx + gCar.getLayoutX() + dx;
        double y = cy + gCar.getLayoutY() + dy;
        
        this.gCarX = x;
        this.gCarY = y;
        

        moveGCarTo(x, 3 * H/4);
    }
    
    private void moveBgBy(int dx, int dy) {
    	//calls the function which moves the background layer 1
    	
        if (dx == 0 && dy == 0) return;
        
        if (dy < 0) return;
        
        final double cy = back.getBoundsInLocal().getHeight() / 2;

        double x = back.getLayoutX() + dx;
        double y = cy + back.getLayoutY() + dy;

        moveBgTo(0, y);
    }
    
    private void moveBg2By(int dx, int dy) {
    	//calls the function which moves the background layer 2
    	
        if (dx == 0 && dy == 0) return;

        if (dy < 0) return;
        
        final double cy = back2.getBoundsInLocal().getHeight() / 2;

        double x = back2.getLayoutX() + dx;
        double y = cy + back2.getLayoutY() + dy;

        moveBg2To(0, y);
    }
    
    private void moveBg3By(int dx, int dy) {
    	//calls the function which moves the background layer 3
    	
        if (dx == 0 && dy == 0) return;

        if (dy < 0) return;
        
        final double cy = back3.getBoundsInLocal().getHeight() / 2;

        double x = back3.getLayoutX() + dx;
        double y = cy + back3.getLayoutY() + dy;

        moveBg3To(0, y);
    }
    
    private void moveBg4By(int dx, int dy) {
    	//calls the function which moves the background layer 4
    	
        if (dx == 0 && dy == 0) return;

        if (dy < 0) return;
        
        final double cy = back4.getBoundsInLocal().getHeight() / 2;

        double x = back4.getLayoutX() + dx;
        double y = cy + back4.getLayoutY() + dy;

        moveBg4To(0, y);
    }
    
    
    private void moveBCarBy(int dx, int dy) {
    	//calls the function which moves the bad car in the opposite direction
    	
        if (dx == 0 && dy == 0) return;
        
        if (dy < 0) return;
        
        final double cy = bCar.getBoundsInLocal().getHeight() / 2;

        double x = bCar.getLayoutX() + dx;
        double y = cy + bCar.getLayoutY() + dy;
        
        this.bCarX = x + 40;
        this.bCarY = y;

        moveBCarTo(x, y);
    }
    
    private void moveHackerBy(int dx, int dy) {
    	//calls the function which moves the hacker component of the bad car
    	if (dx == 0 && dy == 0) return;
        
        if (dy < 0) return;
        
        final double cy = hacker.getBoundsInLocal().getHeight() / 2;

        double x = hacker.getLayoutX() + dx;
        double y = cy + hacker.getLayoutY() + dy;
       
        this.hackerX = x;
        this.hackerY = y;

        moveHackerTo(x, y);
    }
    
    
    private void moveGCarTo(double x, double y) {
    	//moves the good car to the next location
    	//the out of bounds accident event is checked here
    	
    	
        final double cx = gCar.getBoundsInLocal().getWidth()  / 2;
        final double cy = gCar.getBoundsInLocal().getHeight() / 2;
        
        	boolean accident = isOutOfBounds(x);
        	if (accident == true) {
        		
        		try {
        			offTheRoad();
        		} catch (InterruptedException e) {
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
       // moves the background layer 1 to its next position
    	
        final double cy = back.getBoundsInLocal().getHeight() / 2;
        if (y -cy >= 0) {
        	back.relocate(0, -H / 2);
        } else {
        	back.relocate(0 , y - cy);
        	
        }
    }
    
    private void moveBg2To(double x, double y) {
        // moves the background layer 2 to its next position
    	
         final double cy = back2.getBoundsInLocal().getHeight() / 2;
         if (y -cy >= H/2 ) {
         	back2.relocate(0, 0);
         } else {
         	back2.relocate(0 , y - cy);
         	
         }
     }
    
    private void moveBg3To(double x, double y) {
    	 // moves the background layer 3 to its next position
         final double cy = back3.getBoundsInLocal().getHeight() / 2;

         if (y -cy >= H ) {
         	back3.relocate(0, H /2);
         } else {
         	back3.relocate(0 , y - cy);
         	
         }
     }
    
    private void moveBg4To(double x, double y) {
    	// moves the background layer 4 to its next position
        final double cy = back4.getBoundsInLocal().getHeight() / 2;

        if (y -cy >= 3*H/2) {
        	back4.relocate(0, H);
        } else {
        	back4.relocate(0 , y - cy);
        	
        }
    }
    
    private void moveBCarTo(double x, double y)  {        
    	//moves the bad car to its next position
    	//the crashed event is checked here to see if the cars have collided
    	final double cy = bCar.getBoundsInLocal().getHeight() / 2;
    	count2 += 1;
        
        if (count2 > 1) {
        	boolean accident = isCrashed();
    		if (accident == true) {
    			try {
					crashed();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
        }
    	
    	
    	if (y -cy >= H) {
    		Random rand = new Random();
        	bCar.relocate(rand.nextInt(245) + 120, -100);
        	score += 1;
        	
        	
        } else {
        	bCar.relocate(x , y - cy);  
        }
     }

    
    private void moveHackerTo(double x, double y) {
    	//the hacker component of the bad car is being moved here to its next position
    	//also checking if the car has been hacked or not
    	
    	
    	final double cy = hacker.getBoundsInLocal().getHeight() / 2;
    	
    	if (hackCount == 0) {
    		isHacked();
    	}else if(hackCount < 50) {
    		this.hacked = true;
    	} else {
    		hackCount = 0;
    		this.hacked = false;
    	}	
    	
    	if (y -cy >= H) {
    		Random rand = new Random();
        	hacker.relocate(gCarX, -100);
        	if(this.hacked == true) {
        		hackCount += 1;
        	}
        } else {
        	hacker.relocate(x , y - cy);  
        }
    }
    
    private boolean isOutOfBounds(double x) {
    	//checks conditions if the car is off the road
    	
    	if (x > 384 || x < 128) {
    		this.crashed = true;
    		return true;
    	}else {
    		return false;
    	}
    }

    
    private void isHacked() {
    	//checks conditions if the car is hacked
    	
    	double xDiff = gCarX - hackerX;
    	double yDiff = gCarY - hackerY;
    	if (xDiff > -20 && xDiff < 50) {
    		if (yDiff > -60 && yDiff < 60) {
    			this.hacked =  true;
    		}
    	}else {
    		this.hacked = false;
    	}
	
    }
    
    
    private boolean isCrashed() {
    	//checks conditions if the cars have collided
    	
    	double xDiff = this.gCarX - this.bCarX;
    	double yDiff = this.gCarY - this.bCarY;
    	
   /*	System.out.println(this.gCarX);
    	System.out.println(this.bCarX);
    	System.out.println(gCarY);
    	System.out.println(bCarY); */
    	
    	if (xDiff > -36 && xDiff < 110) {
    		if (yDiff > -120 && yDiff < 112) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    
    private void crashed() throws InterruptedException{
    	//display an alert if the car is crashed
    	
    	this.crashed = true;
    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("GAME OVER!");
    	alert.setHeaderText("You lost the game! Try again.");
    	alert.setContentText("You just crashed! Your score was " + score + "!");
    	
    	alert.show();
    	
    	count += 1;
    	
    	if (count==2) {
    		System.exit(0);
    	}
    	
    }
    
    private void offTheRoad() throws InterruptedException{
    	//display an alert if the car is off the road
    	
    	this.crashed = true;
    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("GAME OVER!");
    	alert.setHeaderText("You lost the game! Try again.");
    	alert.setContentText("You just went off the road! Your score was " + score + "!");
    	
    	alert.show();
    	

    	count += 1;
    	
    	if (count==2) {
    		System.exit(0);
    	}
    	
    }
  
    private void instructions() {
    	//display instructions at the start of the game
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("INSTRUCTIONS");
    	alert.setHeaderText("Avoid going off the road or hitting the on coming car!");
    	alert.setContentText("Beware: Hitting the Yellow object will get your car hacked!");
    	
    	alert.showAndWait();
    }
  
    public static void main(String[] args) { launch(args); }
} 

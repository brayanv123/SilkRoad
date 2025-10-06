import Shapes.*;
import java.util.*;
/**
 * The {@code Robot} class represents a graphical robot with a head, body,
 * arms, and legs. The robot is placed at a specific position on the canvas,
 * has a fixed size, and can store a certain amount of "tenges" (a fictional currency).
 * 
 * The robot can be moved to different positions, made visible or invisible,
 * and reset to its initial state.
 * 
 * @author Brayan Valdes-Yan Guerra
 * @version 07/09/2025
 */
public class Robot
{
    private Circle head;
    private Rectangle body;
    private Rectangle arms;
    private Rectangle legs;
    private int actualTenges;
    private int xPosition;
    private int yPosition;
    private int actualX;
    private int actualY;
    private int initialLocation;
    private int size;
    
    
    /**
     * Creates a {@code Robot} object at the specified position with a given
     * initial amount of tenges.
     *
     * @param x      the initial x-coordinate of the robot.
     * @param y      the initial y-coordinate of the robot.
     * @param tenges the initial amount of tenges the robot has.
     */
    public Robot(int x, int y, int initialLocation){
        size = 5;
        xPosition = x;
        yPosition = y;
        actualX = x;
        actualY = y;
        createRobot();
        actualTenges = 0;
        this.initialLocation = initialLocation;
    }
    
    /**
     * Creates the robot's graphical components (head, body, arms, and legs)
     * based on its position and size.
     */
    public void createRobot(){
        
        int armWidth = percentage(size, 0.6);
        int armHeight = size + armWidth;
        int armX = (int) (xPosition - (size * 0.3));
        int armY = yPosition-size;

        int legHeight = percentage(size, 0.6);
        int legX = xPosition + percentage(size, 0.2);
        int legY = yPosition;
        
        body = new Rectangle(size, size,"black", xPosition, yPosition-size);
        head = new Circle(size,xPosition, yPosition-(2*size),"black");
        arms = new Rectangle(armWidth, armHeight, "green", armX, armY);
        legs = new Rectangle(size, legHeight, "green", legX, legY);
    }
    
    /**
     * Moves the robot to a new position (x, y) and updates the positions
     * of all its graphical components accordingly.
     *
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     */
    public void moveTo(int x, int y){
        actualX = x;
        actualY = y;
        
        int armX = x - percentage(size, 0.3);
        int armY = y - size;

        int legX = x + percentage(size, 0.2);
        int legY = y;
        
        arms.moveTo(armX, armY);
        legs.moveTo(legX, legY);
        head.moveTo(x, (y - (2 * size)));
        body.moveTo(x, (y - size));
    }
    
    /**
     * Returns a percentage of a given number.
     *
     * @param number     the base number.
     * @param percentage the percentage to calculate (e.g., 0.6 for 60%).
     * @return the integer result of the percentage calculation.
     */
    private int percentage(int number, Double percentage){
        
        return (int) (number * percentage);
        
    }
    
    /**
     * Makes the robot visible on the canvas by displaying all its parts.
     */
    public void makeVisible(){
        
        head.makeVisible();
        arms.makeVisible();
        legs.makeVisible();
        body.makeVisible();
        
    }
    
    /**
     * Makes the robot invisible on the canvas by hiding all its parts.
     */
    public void makeInvisible(){
        
        head.makeInvisible();
        arms.makeInvisible();
        body.makeInvisible();
        legs.makeInvisible();
        
    }
    
    /**
     * Returns the robot's current x-coordinate.
     *
     * @return the current x-coordinate.
     */
    public int getX(){
        return actualX;
    }
    
    /**
     * Returns the robot's current y-coordinate.
     *
     * @return the current y-coordinate.
     */
    public int getY(){
        return actualY;
    }
    
    /**
     * Returns the current amount of tenges the robot has.
     *
     * @return the current amount of tenges.
     */
    public int getTenges(){
        return actualTenges;
    }
    
    /**
     * Updates the robot's tenges by adding the specified value.
     *
     * @param tenges the amount to add to the current tenges.
     */
    public void setTenges(int tenges){
        actualTenges = tenges;
    }
    
    /**
     * Resets the robot to its initial position on the canvas.
     */
    public void rebootRobot(){
        moveTo(xPosition, yPosition);
    }
    
    public int getInitialLocation(){
        return initialLocation;
    }
    
}
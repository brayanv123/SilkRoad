package SilkRoadPackage;
import java.util.*;
import Shapes.*;

/**
 * Represents a special type of store called an AutonomousStore.
 * <p>
 * An {@code AutonomousStore} chooses its own random position along the Silk Road
 * instead of being placed in the location assigned by the system. Each time it is
 * created, it randomly selects a new position from the available rectangles.
 * <p>
 * This class extends {@link Store} and changes its appearance to a cyan fill with
 * a gray border to distinguish it from other store types.
 *
 * @author Brayan Valdes - Yan Guerra
 * @version 28/10/2025
 */
public class AutonomousStore extends Store
{
    private Rectangle[] rectangles;
    private static int rndPosition;
    
    /**
     * Constructs an AutonomousStore that chooses its position automatically.
     * <p>
     * Upon creation, the store selects a random rectangle from the given array
     * and updates its coordinates accordingly. Its color is set to cyan and gray
     * for easy visual identification.
     *
     * @param rectangle the initial graphical representation of the store.
     * @param tenges the amount of tenges contained in the store.
     * @param rectangles the array of all available rectangles representing road positions.
     */
    public AutonomousStore(Rectangle rectangle, int tenges, Rectangle[] rectangles){
        super(rectangle, tenges);
        this.rectangles = rectangles;
        rndPosition = randomPosition();
        changePosition(xPos(), yPos());
        changeColor("cyan", "gray");
    }
    
    /**
     * Generates a random valid position index within the available rectangles.
     *
     * @return a random integer between 0 (inclusive) and the total number of rectangles (exclusive).
     */
    public int randomPosition(){
        int maxPosition = rectangles.length;
        Random random = new Random();
        return random.nextInt(maxPosition);
    }
    
    /**
     * Returns the X coordinate of the randomly selected position.
     *
     * @return the X coordinate corresponding to the store's new location.
     */
    public int xPos(){
        return rectangles[rndPosition].getXPosition();
    }
    
    /**
     * Returns the Y coordinate of the randomly selected position.
     *
     * @return the Y coordinate corresponding to the store's new location.
     */
    public int yPos(){
        return rectangles[rndPosition].getYPosition();
    }
}
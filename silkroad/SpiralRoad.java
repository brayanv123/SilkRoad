import Shapes.*;
import java.util.*;
/**
 * The {@code SpiralRoad} class represents a spiral-shaped road 
 * built from small rectangular segments. 
 * 
 * The road is created starting from a fixed starting point 
 * and expands outward in a spiral pattern (right, down, left, up).
 * 
 * The spiral length is determined by the number of rectangles specified. 
 * Each rectangle has a fixed size of 10x10.
 * 
 * The road can be made visible or invisible on the canvas, 
 * and all its rectangles can be retrieved as an array.
 * 
 * @author Brayan Valdes - Yan Guerra
 * @version 07/09/2025
 */
public class SpiralRoad
{
    private int length;
    private int numberRectangles;
    private Rectangle[] rectangles;
    private static final int xStart = 500;
    private static final int yStart = 300;
    
    /**
     * Creates a {@code SpiralRoad} with the specified length.
     * The road is automatically generated in a spiral pattern.
     *
     * @param length the number of rectangles in the spiral.
     */
    public SpiralRoad(int length){
        this.length = length;
        numberRectangles = 0;
        rectangles = new Rectangle[length];
        createSpiral();
    }
    
    /**
     * Builds the spiral moving to the right and adding rectangles.
     *
     * @param x   the starting x-coordinate.
     * @param y   the y-coordinate.
     * @param end the distance to move in this direction.
     */
    private void right(int x,int y, int end){
        for (int i = x; i < (x+end); i+=10){
            addRectangle(i,y);
            numberRectangles += 1;
        }
    }
    
    /**
     * Builds the spiral moving downward and adding rectangles.
     *
     * @param x   the x-coordinate.
     * @param y   the starting y-coordinate.
     * @param end the distance to move in this direction.
     */
    private void down(int x, int y, int end){
        for (int j = y; j < (y+end); j+=10){
            addRectangle(x,j);
            numberRectangles += 1;
        }
    }
    
    /**
     * Builds the spiral moving to the left and adding rectangles.
     *
     * @param x   the starting x-coordinate.
     * @param y   the y-coordinate.
     * @param end the distance to move in this direction.
     */
    private void left(int x, int y, int end){
        for (int i = x; i >= (x - end); i-=10){
            addRectangle(i,y);
            numberRectangles += 1;
        }
    }
    
    /**
     * Builds the spiral moving upward and adding rectangles.
     *
     * @param x   the x-coordinate.
     * @param y   the starting y-coordinate.
     * @param end the distance to move in this direction.
     */
    private void up(int x, int y, int end){
        for (int j = y; j >= (y - end); j -=10){
            addRectangle(x,j);
            numberRectangles += 1;
        }
    }
    
    /**
     * Adds a rectangle to the spiral at the given coordinates,
     * if the maximum length has not been reached.
     *
     * @param x the x-coordinate of the rectangle.
     * @param y the y-coordinate of the rectangle.
     */
    private void addRectangle(int x, int y){
        if (numberRectangles < length){
            Rectangle rectangle = new Rectangle(10,10,"yellow", x, y);
            rectangles[numberRectangles] = rectangle;
        }
    }
    
    /**
     * Creates the full spiral by repeatedly moving right, down,
     * left, and up, expanding the distance after each cycle.
     */
    public void createSpiral(){
        int newX = xStart;
        int newY = yStart;
        int end = 30;
        while (numberRectangles < length){
            right(newX, newY, end);
            newX += (end-10);
            newY +=10;        
            down(newX, newY, end);
            newY += (end-10);
            end += 10;
            newX -=10;
            left(newX, newY, end);
            newX = newX - end;
            newY -= 10; 
            up(newX, newY, end);
            newY = newY-end;
            newX+=10;
            end+=30;
        }
    }
    
    /**
     * Returns the array of rectangles that make up the spiral road.
     *
     * @return an array of {@code Rectangle} objects.
     */
    public Rectangle[] getArrRectangles(){
        return rectangles;
    }
    
    /**
     * Makes the spiral road visible on the canvas by displaying all rectangles.
     */
    public void makeVisible(){
        for (int i = 0; i < length; i++){
            rectangles[i].makeVisible();
        }
    }
    
    /**
     * Makes the spiral road invisible on the canvas by hiding all rectangles.
     */
    public void makeInvisible(){
        for (int i = 0; i < length; i ++){
            rectangles[i].makeInvisible();
        }
    }
    
}
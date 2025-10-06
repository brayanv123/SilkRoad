import Shapes.*;
/**
 * The {@code Store} class represents a store in the graphical world.
 * A store has a wall (rectangle), a roof (triangle),
 * a position on the canvas (x, y), a fixed size, and a certain
 * amount of "tenges" (a fictional currency).
 * 
 * The store can be made visible or invisible on the canvas,
 * allow querying or removing its tenges, and can be reset
 * to its initial amount of tenges.
 * 
 * @author Brayan Valdes-Yan Guerra
 * @version 07/09/2025
 */
public class Store
{
    private Triangle roof;
    private Rectangle wall;
    private int xPoint;
    private int yPoint;
    private int size;
    private int initialTenges;
    private int actualTenges;
    private int times;

    /**
     * Creates a {@code Store} object at the specified position.
     *
     * @param x      the x-coordinate of the store.
     * @param y      the y-coordinate of the store.
     * @param tenges the initial amount of tenges stored.
     */
    public Store(int x, int y, int tenges){
        size = 10;
        xPoint = x;
        yPoint = y;
        initialTenges = tenges;
        actualTenges = tenges;
        
        int roofY = (y-size);
        int roofX = (int)(x+(0.5*size));
        wall = new Rectangle(size, size, "blue",x, y);
        roof = new Triangle(roofX, roofY, "green", size, size);
        
    }
    
    /**
     * Makes the store visible on the canvas by displaying
     * its wall and roof.
     */
    public void makeVisible(){
        
        roof.makeVisible();
        wall.makeVisible();
        
    }
    
    /**
     * Makes the store invisible on the canvas by hiding
     * its wall and roof.
     */
    public void makeInvisible(){
        
        roof.makeInvisible();
        wall.makeInvisible();
        
    }
    
    /**
     * Returns the current amount of tenges stored.
     *
     * @return the current amount of tenges.
     */
    public int getTenges(){
        return actualTenges;
    }
    
    /**
     * Removes all tenges from the store,
     * setting the current amount to 0.
     */
    public void removeTenges(){
        actualTenges = 0;
        roof.changeColor("red");
        wall.changeColor("orange");
    }
    
    /**
     * Returns the x-coordinate of the store.
     *
     * @return the x-coordinate.
     */
    public int getX(){
        return xPoint;
    }
    
    /**
     * Returns the y-coordinate of the store.
     *
     * @return the y-coordinate.
     */
    public int getY(){
        return yPoint;
    }
    
    public void increaseTimes(){
        times += 1;
    }
    
    public int getTimes(){
        return times;
    }
    
    /**
     * Restores the store's tenges to the initial
     * amount it was created with.
     */
    public void rebootStore(){
        actualTenges = initialTenges;
        wall.changeColor("blue");
        roof.changeColor("green");
    }
}
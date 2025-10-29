package SilkRoadPackage;
import Shapes.*;

/**
 * Represents a special type of store called a FighterStore.
 * <p>
 * A FighterStore can only be looted by robots that have more tenges than the store itself.
 * This class extends {@link Store} and customizes its visual appearance and interaction rules.
 *
 * @author Brayan Valdes - Yan Guerra
 * @version 28/10/2025
 */
public class FighterStore extends Store
{
    /**
     * Constructs a FighterStore with the given shape and amount of tenges.
     * <p>
     * The store is visually distinguished by having an orange fill and a purple border.
     *
     * @param rectangle the graphical representation (position and shape) of the store.
     * @param tenges the initial amount of tenges contained in the store.
     */
    public FighterStore(Rectangle rectangle, int tenges)
    {
        super(rectangle, tenges);
        changeColor("orange", "purple");
    }
    
    /**
     * Removes tenges from this store if the given robot is allowed to take them.
     * <p>
     * A robot can only remove tenges from this store if it satisfies the
     * {@link #isRemovable(Robot)} condition.
     *
     * @param robot the robot attempting to collect tenges from this store.
     */
    public void removeTenges(Robot robot){
        if (isRemovable(robot)){
            removeTenges();
        }
    }

    /**
     * Determines whether the given robot is allowed to remove tenges from this store.
     * <p>
     * A robot can only loot this store if it has strictly more tenges than the store itself.
     *
     * @param robot the robot attempting to access the store.
     * @return {@code true} if the robot has more tenges than the store; {@code false} otherwise.
     */
    public boolean isRemovable(Robot robot){
        return robot.getTenges() > getTenges();
    }
}
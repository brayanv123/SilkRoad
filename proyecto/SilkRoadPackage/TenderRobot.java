package SilkRoadPackage;
import Shapes.*;

/**
 * Represents a special type of robot called a TenderRobot.
 * <p>
 * A {@code TenderRobot} is a robot that only takes half of the tenges
 * available in each store it visits, instead of taking all the money.
 * <p>
 * This class extends {@link Robot} and changes its visual appearance
 * to red in order to distinguish it from other robot types.
 *
 * @author Brayan Valdes - Yan Guerra
 * @version 28/10/2025
 */
public class TenderRobot extends Robot
{
    /**
     * Constructs a TenderRobot at the specified initial location.
     * <p>
     * The robot is displayed in red to visually indicate its type.
     *
     * @param rectangle the graphical representation (position and shape) of the robot.
     * @param initialLocation the initial location index of the robot on the Silk Road.
     */
    public TenderRobot(Rectangle rectangle, int initialLocation)
    {
        super(rectangle, initialLocation);
        this.body.changeColor("red");
    }
    
}
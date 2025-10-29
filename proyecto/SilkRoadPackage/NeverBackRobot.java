package SilkRoadPackage;
import Shapes.*;
import javax.swing.JOptionPane;

/**
 * Represents a special type of robot called a NeverBackRobot.
 * <p>
 * A {@code NeverBackRobot} is a robot that cannot move backward — it can only
 * move forward along the Silk Road. If an attempt is made to move it to a
 * previous position, the action is rejected and a warning message is shown.
 * <p>
 * This class extends {@link Robot} and modifies its behavior and appearance.
 *
 * @author Brayan Valdes - Yan Guerra
 * @version 28/10/2025
 */
public class NeverBackRobot extends Robot
{
    private int position;
    /**
     * Constructs a NeverBackRobot at the specified initial location.
     * <p>
     * The robot is visually represented in blue to distinguish it from other robot types.
     *
     * @param rectangle the graphical representation (position and shape) of the robot.
     * @param initLocation the initial location index of the robot.
     */
    public NeverBackRobot(Rectangle rectangle, int initLocation)
    {
        super(rectangle, initLocation);
        this.body.changeColor("blue");
    }
    
    /**
     * Attempts to move the robot to a new position along the Silk Road.
     * <p>
     * The robot can only move forward (to a position greater than its current one).
     * If the destination position is less than or equal to the current position,
     * a dialog message is displayed and the robot remains in place.
     *
     * @param newPos the new rectangle representing the target position.
     * @param to the current position index of the robot.
     * @param in the intended destination position index.
     */
    public void moveTo(Rectangle newPos ,int to, int in){
        if (to <= in){
            JOptionPane.showMessageDialog(null, "No se puede mover la posicion");
        } else {
            position = to;
            moveTo(newPos);
        }
    } 
    
    /**
     * Returns the current position index of this robot on the Silk Road.
     *
     * @return the current position of the robot.
     */
    public int actualPosition(){
        return position;
    }
}
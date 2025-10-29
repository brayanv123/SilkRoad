package SilkRoadPackage;

/**
 * Represents a custom exception used within the Silk Road simulator.
 * <p>
 * The {@code SilkRoadException} class defines specific error messages related to
 * invalid operations or data in the Silk Road system, such as invalid numbers,
 * invalid day lists, or invalid positions.
 * <p>
 * This exception is thrown to signal exceptional situations that occur during
 * the simulation setup or robot/store operations.
 *
 * @author Brayan Valdes - Yan Guerra
 * @version 28/10/2025
 */

public class SilkRoadException extends Exception{

    public static final String NUMBER_ERROR = "The number is invalid, it must be a number greater than zero.";
    public static final String DAYS_ERROR = "The list days is invalid";
    public static final String ROAD_IS_FINISH = "The road is finished";
    public static final String POSITION_GREATHER_TAN_LENGHT = "The position is greater than the length";
    
    /**
     * Creates a new {@code SilkRoadException} with the specified detail message.
     *
     * @param message the detail message describing the cause of the exception.
     */
    public SilkRoadException(String message){
        super(message);
    }    
}

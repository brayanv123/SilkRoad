import java.util.*;
/**
 * The {@code SilkRoadContest} class simulates a competition scenario 
 * where robots and stores are placed along a one-dimensional Silk Road.
 * Robots move and collect tenges (coins) from stores based on a sequence 
 * of daily events provided as input.
 * <p>
 * This class allows both a fast execution of the simulation (instant results)
 * and a slow mode using a timer for visualization.
 * </p>
 * 
 * @author Brayan Valdes - Yan Guerra
 * @version 05/10/2025
 */
public class SilkRoadContest
{
    private SilkRoad road;
    private static int iterator = 0;
    private ArrayList<Integer> profit;
    private int [][] days;
 
    /**
     * Constructs a new {@code SilkRoadContest} instance with 
     * default initialization.
     * <p>
     * Initializes an empty {@code profit} list and a default 1x1 
     * {@code days} matrix to avoid null references.
     * </p>
     */
    public SilkRoadContest()
    {
        days = new int[1][1];
        profit = new ArrayList<>();
    }
    
    /**
     * Solves the Silk Road contest by processing a sequence of events 
     * represented by a 2D array of integers.
     * <p>
     * Each row in {@code days} corresponds to an event:
     * <ul>
     *   <li>{@code elem[0]} → type (1 for robot, 2 for store)</li>
     *   <li>{@code elem[1]} → position on the Silk Road</li>
     *   <li>{@code elem[2]} → tenges (only if it's a store)</li>
     * </ul>
     * The method places robots and stores, moves robots, collects profits, 
     * and resets the road after each step.
     * </p>
     * 
     * @param days a 2D integer array representing daily actions on the Silk Road.
     * @return an {@code ArrayList<Integer>} containing the total profit after each step.
     */
    public ArrayList<Integer> solve(int[][] days){
        this.days = days;
        int length = days[0][1];
        for (int[] elem: days){
            if (elem[1]>length) length = elem[1];
        }
        road = new SilkRoad(length);
        for (int[] elem: days){
            int isRobotOrStore = elem[0];
            int position = elem[1];
            if (isRobotOrStore == 2){
                int tenges = elem[2];
                road.pleaseStore(position, tenges);
            } else road.pleaseRobot(position);
            
            road.moveRobots();
            profit.add(road.profit());
            road.reboot();
        }
        System.out.println(profit);  
        return profit;
    }
    
    /**
     * Simulates the Silk Road contest visually, with an option for 
     * slow-motion playback using a {@link Timer}.
     * <p>
     * This method executes the same logic as {@link #solve(int[][])}, 
     * but when {@code slow} is {@code true}, it uses a {@code TimerTask} 
     * to perform the actions gradually every 10 seconds.
     * </p>
     *
     * @param days a 2D integer array representing daily actions (robots or stores).
     * @param slow if {@code true}, runs the simulation step-by-step with a timer;
     *             if {@code false}, runs it instantly.
     */
    public void simulate(int[][] days, boolean slow){
        this.days = days;
        int length = days[0][1];
        for (int[] elem: days){
            if (elem[1]>length) length = elem[1];
        }
        road = new SilkRoad(length);
        road.makeVisible();
        if (slow){
            
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    road.makeVisible();
                    int isRobotOrStore = days[iterator][0];
                    int position = days[iterator][1];
                    if (isRobotOrStore == 2){
                        int tenges = days[iterator][2];
                        road.pleaseStore(position, tenges);
                    } else road.pleaseRobot(position);
                    road.moveRobots();
                    profit.add(road.profit());
                    road.reboot();
                    road.makeVisible();
                    iterator += 1;
                    if (iterator >= days.length){
                        timer.cancel();
                    }
                }
            };
            timer.schedule(task, 0, 10000);
            iterator = 0;
        } else {
            for (int[] elem: days){
                road.makeVisible();
                int isRobotOrStore = elem[0];
                int position = elem[1];
                if (isRobotOrStore == 2){
                    int tenges = elem[2];
                    road.pleaseStore(position, tenges);
                } else road.pleaseRobot(position);
                road.moveRobots();
                profit.add(road.profit());
                road.makeVisible();
                road.reboot();
            }
        }
    }
}
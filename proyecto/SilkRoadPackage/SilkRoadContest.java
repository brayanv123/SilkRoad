package SilkRoadPackage;
import java.util.*;
import Shapes.*;
import javax.swing.JOptionPane;

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
        try{
            road = new SilkRoad(length);
        }catch (SilkRoadException e){
            JOptionPane.showMessageDialog(null, "Error create road"+e.getMessage());
        }
        for (int[] elem: days){
            int isRobotOrStore = elem[0];
            int position = elem[1];
            if (isRobotOrStore == 2){
                int tenges = elem[2];
                try{
                    road.pleaseStore("normal", position, tenges);
                } catch(SilkRoadException e){
                    JOptionPane.showMessageDialog(null, "Error create store" + e.getMessage());
                }
            } else {
                try{
                    road.pleaseRobot("normal", position);
                } catch (SilkRoadException e) {
                    JOptionPane.showMessageDialog(null,"Error create robot" + e.getMessage());
                }   
            }
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
        try{
            road = new SilkRoad(length);
        } catch (SilkRoadException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        road.makeVisible();
        if (slow){
            
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    runDay(iterator);
                    iterator += 1;
                    if (iterator >= days.length){
                        road.makeInvisible();
                        timer.cancel();
                    }
                }
            };
            timer.schedule(task, 0, 10000);
            iterator = 0;

        } else {
            for (int i = 0; i < days.length; i++){
                runDay(i);
            }
            road.makeInvisible();
        }
    }

    /**
     * Executes the simulation for a single day based on the data stored in {@code days[iterator]}.
     * <p>
     * Each day may involve adding either a new robot or a new store on the Silk Road.
     * After the addition, all robots move to collect tenges from the stores, and the
     * daily profit is calculated and stored.
     * <p>
     * The sequence of operations for each day is as follows:
     * <ol>
     *   <li>Make the road visible.</li>
     *   <li>Add a new store or robot according to the current entry.</li>
     *   <li>Move all robots to collect tenges from available stores.</li>
     *   <li>Record the profit for that day.</li>
     *   <li>Reboot the road to its initial state (reset robots and stores).</li>
     *   <li>Make the road visible again to display the updated state.</li>
     * </ol>
     *
     * @param iterator the index of the current day in the {@code days} matrix.
     */

    private void runDay(int iterator){
        road.makeVisible();
        int isRobotOrStore = days[iterator][0];
        int position = days[iterator][1];
        if (isRobotOrStore == 2){
            int tenges = days[iterator][2];
            try{
                road.pleaseStore("normal",position, tenges);
            }catch (SilkRoadException e){
                JOptionPane.showMessageDialog(null, "Error create robot" + e.getMessage());
            }
        } else {
            try{
                road.pleaseRobot("normal",position);
            } catch (SilkRoadException e){
                JOptionPane.showMessageDialog(null, "Error create robot" + e.getMessage());
            }
        }
        road.moveRobots();
        profit.add(road.profit());
        road.reboot();
        road.makeVisible();
    }
}
package SilkRoadPackage;
import java.util.*;
import Shapes.*;
import javax.swing.JOptionPane;
/**
 * The {@code SilkRoad} class represents a system that manages
 * a spiral-shaped road with {@link Robot} and {@link Store} objects
 * placed along its path.
 * 
 * <p>It allows adding and removing robots and stores at specific
 * locations, moving robots along the spiral, resupplying stores,
 * rebooting robots, and calculating the total profit in terms of tenges.</p>
 * 
 * <p>The road is internally represented using a {@link SpiralRoad} object,
 * and robots/stores are tracked in both lists and {@link TreeMap} structures
 * for efficient access by location.</p> 
 * 
 * <p>Once the system is marked as finished, no further modifications
 * to robots or stores can be made.</p>
 * 
 * @author Brayan Valdes - Yan Guerra
 * @version 17/09/2025
 */
public class SilkRoad
{
    private static int length;
    private static boolean isFinished;
    public static Rectangle[] positions;
    private SpiralRoad spiral;
    private ArrayList<Object> robots;
    private ArrayList<Object> stores;
    private TreeMap<Integer, Object> mapRobots;
    private TreeMap<Integer, Object> mapStores;
    private TreeMap<Integer, int[]> profitPerMove;
    
    /**
     * Creates a {@code SilkRoad} of the specified length.
     * 
     * @param length the number of positions (rectangles) in the spiral road.
     */
    public SilkRoad(int length) throws SilkRoadException{
        if (length <= 0){
            throw new SilkRoadException(SilkRoadException.NUMBER_ERROR);
        }
        this.length = length;
        isFinished = false;
        spiral = new SpiralRoad(length);
        positions = spiral.getArrRectangles();
        robots = new ArrayList<>();
        stores = new ArrayList<>();
        mapRobots = new TreeMap<>();
        mapStores = new TreeMap<>();
        profitPerMove = new TreeMap<>();
    }
    
    /**
     * Constructs a new instance of the Silk Road simulator using the given matrix of daily actions.
     * <p>
     * Each subarray in the {@code days} matrix represents an event that occurs on a specific day:
     * <ul>
     *   <li>{@code [1, position]} → adds a robot at the given position.</li>
     *   <li>{@code [2, position, tenges]} → adds a store at the given position with the specified amount of tenges.</li>
     * </ul>
     * The constructor initializes the spiral-shaped road, determines its total length based on
     * the farthest position found in the input, and creates all necessary data structures to manage
     * robots and stores on the road.
     *
     * @param days a two-dimensional array containing the daily setup of robots and stores.
     *             It must not be {@code null} or empty.
     * @throws SilkRoadException if the matrix is null, empty, or contains invalid data.
     */
    public SilkRoad(int[][] days)throws SilkRoadException{
        if(days == null || days.length == 0){
            throw new SilkRoadException(SilkRoadException.DAYS_ERROR);
        }
        length = days[0][1];
        for (int[] elem: days){
            if (elem[1]>length) length = elem[1];
        }
        isFinished = false;
        spiral = new SpiralRoad(length);
        positions = spiral.getArrRectangles();
        robots = new ArrayList<>();
        stores = new ArrayList<>();
        mapRobots = new TreeMap<>();
        mapStores = new TreeMap<>();
        for (int[] elem: days){
            if (elem == null || elem.length == 0){
                throw new SilkRoadException(SilkRoadException.DAYS_ERROR);
            }
            int isRobotOrStore = elem[0];
            int position = elem[1];
            if (isRobotOrStore == 2){
                int tenges = elem[2];
                pleaseStore("normal",position, tenges);
            } else pleaseRobot("normal", position);
        }
    }
    
    /**
     * Places a store at the specified location with an initial amount of tenges.
     *
     * @param location the location index on the road.
     * @param tenges   the initial tenges for the store.
     */
    public void pleaseStore(String type,int location, int tenges)throws SilkRoadException{
        if (isFinished){
            throw new SilkRoadException(SilkRoadException.ROAD_IS_FINISH);
        } else if (location > length){
            throw new SilkRoadException(SilkRoadException.POSITION_GREATHER_TAN_LENGHT);
        }
        if (location >= 1){
            int position = location - 1;
            Rectangle rectangle = positions[position];
            Object store = null;
            if (type.equals("normal")){
                store = new Store(rectangle, tenges);
            } else if (type.equals("autonomous")) {
                store = new AutonomousStore(rectangle, tenges, positions);
            } else if (type.equals("fighter")){
                store = new FighterStore(rectangle, tenges);
            }
            
            mapStores.put(location, store);
            stores.add(store);
        
        } else System.out.println("error please store");
    }
    
    /**
     * Removes the store located at the specified position.
     *
     * @param location the location index on the road.
     */
    public void removeStore(int location) throws SilkRoadException{
        if (isFinished){
            throw new SilkRoadException(SilkRoadException.ROAD_IS_FINISH);
        } else if (location > length){
            throw new SilkRoadException(SilkRoadException.POSITION_GREATHER_TAN_LENGHT);
        }
        int xPosition = positions[location-1].getXPosition();
        int yPosition = positions[location-1].getYPosition();
        for (int i = 0; i < stores.size(); i++){
            Store store = (Store) stores.get(i);
            if (xPosition == store.getX() && yPosition == store.getY()){
                store.makeInvisible();
                stores.remove(i);
            }
        }
        mapStores.remove(location);
    }
    
    /**
     * Places a robot at the specified location with an initial amount of tenges.
     *
     * @param location the location index on the road.
     * @param tenges   the initial tenges for the robot.
     */
    public void pleaseRobot(String type,int location) throws SilkRoadException{
        if (isFinished){
            throw new SilkRoadException(SilkRoadException.ROAD_IS_FINISH);
        } else if (location > length){
            throw new SilkRoadException(SilkRoadException.POSITION_GREATHER_TAN_LENGHT);
        }
        int position = location - 1;
        if (location >= 1){           
            Rectangle rectangle = positions[position];
            Object robot = null;
            if (type.equals("normal")){
                robot = new Robot(rectangle, location);
            } else if (type.equals("neverback")){
                robot = new NeverBackRobot(rectangle, location);
            } else if (type.equals("tender")){
                robot = new TenderRobot(rectangle, location);
            }
            
            mapRobots.put(location, robot);
            robots.add(robot);
        }
    }
    
    /**
     * Removes the robot located at the specified position.
     *
     * @param location the location index on the road.
     */
    public void removeRobot(int location) throws SilkRoadException{
        if (isFinished){
            throw new SilkRoadException(SilkRoadException.ROAD_IS_FINISH);
        } else if (location > length){
            throw new SilkRoadException(SilkRoadException.POSITION_GREATHER_TAN_LENGHT);
        }
        int xPosition = positions[location-1].getXPosition();
        int yPosition = positions[location-1].getYPosition();
        for (int i = 0; i < robots.size(); i++){
            Robot robot = (Robot) robots.get(i);
            if (xPosition == robot.getX() && yPosition == robot.getY()){
                robot.makeInvisible();
                robots.remove(i);
            }
        }
        mapRobots.remove(location);
    }
    
    /**
     * Moves a robot from its current location to a new location,
     * consuming tenges equal to the distance traveled.
     * If the new location has a store, the robot collects its tenges.
     *
     * @param location the current location of the robot.
     * @param meters   the number of positions to move forward.
     */
    public void moveRobot(int location, int meters)throws SilkRoadException{      
        int newLocation = location + meters;
        if (isFinished){
            throw new SilkRoadException(SilkRoadException.ROAD_IS_FINISH);
        } else if (newLocation > length){
            throw new SilkRoadException(SilkRoadException.POSITION_GREATHER_TAN_LENGHT);
        }
        if(mapRobots.containsKey(location)){
            Object objectRobot = mapRobots.get(location);
            Rectangle rectangle = positions[newLocation - 1];
            Robot robot = null;
            if (objectRobot instanceof NeverBackRobot){
                robot = (NeverBackRobot) objectRobot;
                ((NeverBackRobot)robot).moveTo(rectangle,newLocation ,location);
                newLocation = ((NeverBackRobot)robot).actualPosition();
            } else {robot = (Robot)objectRobot;}

            robot.setTenges(robot.getTenges() - Math.abs(meters));
            mapRobots.put(newLocation, robot);
            mapRobots.remove(location); 
            if (mapStores.containsKey(newLocation)){
                Store newStore = (Store) mapStores.get(newLocation);
                Object objectStore = mapStores.get(newLocation);
                int newTenges; 
                if (objectStore instanceof FighterStore ){
                    ((FighterStore)objectStore).removeTenges(robot);
                    if(((FighterStore)objectStore).isRemovable(robot)){
                        newTenges = robot.getTenges() + newStore.getTenges();
                    } else {newTenges = robot.getTenges();}
                } else {newTenges = robot.getTenges() + newStore.getTenges();}
                profitPerMove.put(newLocation, new int[]{robot.getTenges(), newTenges});
                robot.setTenges(newTenges);
                newStore.removeTenges();
                newStore.increaseTimes();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error move robot");
        }
    }
    
    /**
     * Moves all robots along the Silk Road according to the profit they can obtain from nearby stores.
     * <p>
     * For each robot in {@code mapRobots}, the method evaluates every store in {@code mapStores} 
     * to determine whether moving to that store is profitable. 
     * The profit is calculated as:
     * </p>
     * 
     * <pre>
     * profit = storeTenges - |robotPosition - storePosition|
     * </pre>
     * 
     * <p>
     * If the profit is positive, the robot moves toward that store by the required distance.
     * The movement is performed by invoking {@code moveRobot(currentPosition, distance)}.
     * </p>
     * 
     * <p><b>Logic summary:</b></p>
     * <ul>
     *   <li>Iterates through all robots stored in {@code mapRobots}.</li>
     *   <li>For each robot, checks every store in {@code mapStores}.</li>
     *   <li>Computes the movement cost and net profit.</li>
     *   <li>If the profit is positive, moves the robot to the store's position.</li>
     *   <li>Updates the robot’s new position after the movement.</li>
     * </ul>
     *
     * <p><b>Note:</b> The method assumes that {@code moveRobot()} handles the actual 
     * position update, store collection, and internal state consistency of both 
     * robots and stores.</p>
     *
     * @see #moveRobot(int, int)
     */
    public void moveRobots() { 
        List<Integer> robotKeys = new ArrayList<>(mapRobots.keySet());
        for (int keyRobot : robotKeys) {
            int newPosition = keyRobot;
            for (int key : mapStores.keySet()) {
                Store store = (Store) mapStores.get(key);
                int cost = Math.abs(newPosition - key);
                int storeTenges = store.getTenges();
                int profit = storeTenges - cost;
                if (profit > 0) {
                    int distance = key - newPosition;
                    try{
                        moveRobot(newPosition, distance);
                        newPosition += distance;
                    } catch (SilkRoadException e){
                        JOptionPane.showMessageDialog(null, "Error moving robot" + e.getMessage());
                    }   
                }
            }
        }
    }
    
    /**
     * Returns a 2D array representing the profit collected by each robot per move.
     * <p>
     * Each row in the returned array corresponds to a single robot and contains:
     * <ul>
     *   <li><b>index 0:</b> the robot’s current position (key in {@code profitPerMove})</li>
     *   <li><b>index 1:</b> the profit gained in the first move</li>
     *   <li><b>index 2:</b> the profit gained in the second move</li>
     * </ul>
     * The method iterates through the {@code profitPerMove} map, which stores 
     * each robot’s profits across movements, and converts it into a two-dimensional array.
     * </p>
     *
     * @return a 2D integer array where each row represents a robot and its profits per move.
     */
    public int[][] profitPerMove(){
        int[][] profitList = new int[profitPerMove.size()][3];
        int i = 0;
        for (int key: profitPerMove.keySet()){
            int[] tengesList = profitPerMove.get(key);
            profitList[i] = new int[]{key,tengesList[0],tengesList[1]};
            i++;
        }
        return profitList;
    }

    /**
     * Restores all stores to their initial amount of tenges.
     */
    public void resuplyStores(){
        for (int i = 0; i < stores.size(); i++){
            Store store = (Store) stores.get(i);
            store.rebootStore();
        }
    }
    
    /**
     * Returns all robots to their initial positions.
     */
    public void returnRobots(){
        TreeMap<Integer, Object> restorePos = new TreeMap<>();
        for(int key: mapRobots.keySet()){
            Robot robot = (Robot) mapRobots.get(key);
            robot.rebootRobot();
            robot.setTenges(0);
            restorePos.put(robot.getInitialLocation(), robot);
        }
        
        mapRobots.clear();
        mapRobots = restorePos;
    }
    
    /**
     * Reboots the entire road, resupplying stores and returning robots.
     */
    public void reboot(){
        resuplyStores();
        returnRobots();
    }
    
    /**
     * Calculates the total profit by summing the tenges
     * of all robots currently on the road.
     *
     * @return the total tenges held by all robots.
     */
    public int profit(){
        int sumTenges = 0;
        for(Object object: robots){
            Robot robot = (Robot) object;
            sumTenges += robot.getTenges();
        }
        return sumTenges;
    }
    
    /**
     * Returns a list of stores with their positions and tenges.
     *
     * @return an {@code ArrayList} of int arrays {location, tenges}.
     */
    public ArrayList<int[]> stores(){
        ArrayList<int[]> storTenges = new ArrayList<>();
        for (Integer key: mapStores.keySet()){
            Store store=(Store)mapStores.get(key);
            storTenges.add(new int[]{key, store.getTenges()});
        }
        return storTenges;
    }
    
    /**
     * Returns a list of all stores that have been emptied at least once.
     * <p>
     * Each element in the returned list is an {@code int[]} array containing:
     * <ul>
     *   <li><b>index 0:</b> the store’s position (key in {@code mapStores})</li>
     *   <li><b>index 1:</b> the number of times the store has been emptied</li>
     * </ul>
     * Only stores whose {@code getTimes()} value is greater than zero 
     * (meaning they were visited and emptied by a robot) are included.
     * </p>
     *
     * @return an {@code ArrayList<int[]>} where each element represents 
     *         a store position and the number of times it was emptied.
     */
    public ArrayList<int[]> emptiedStores(){
        ArrayList<int[]> emptiedStores = new ArrayList<>();
        for (Integer key: mapStores.keySet()){
            Store store = (Store)mapStores.get(key);
            if (store.getTimes() > 0){
                emptiedStores.add(new int[]{key, store.getTimes()});
            }
        }
        return emptiedStores;
    }
    
    /**
     * Returns a list of robots with their positions and tenges.
     *
     * @return an {@code ArrayList} of int arrays {location, tenges}.
     */
    public ArrayList<int[]> robots(){
        ArrayList<int[]> robotTenges = new ArrayList<>();
        for (Integer key: mapRobots.keySet()){
            Robot robot = (Robot) mapRobots.get(key);
            robotTenges.add(new int[]{key, robot.getTenges()});
        }
        return robotTenges;
    }
    
    /**
     * Makes the entire road visible, including the spiral,
     * stores, and robots.
     */
    public void makeVisible(){
        spiral.makeVisible();
        for (Object object: stores){
            Store store = (Store) object;
            store.makeVisible();
        }
        for (Object object: robots){
            Robot robot = (Robot) object;
            robot.makeVisible();
        }
    }
    
    /**
     * Makes the entire road invisible, including the spiral,
     * stores, and robots.
     */
    public void makeInvisible(){
        spiral.makeInvisible();
        for (Object object: stores){
            Store store = (Store) object;
            store.makeInvisible();
        }
        for (Object object: robots){
            Robot robot = (Robot) object;
            robot.makeInvisible();
        }
    }
    
    /**
     * Finalizes the SilkRoad system by clearing all robots and stores,
     * resetting the spiral, and marking the road as finished.
     */
    public void finis(){
        this.length = 0;
        isFinished = true;
        spiral = null;
        positions = new Rectangle[0];
        robots = new ArrayList<>();
        stores = new ArrayList<>();
        mapRobots = new TreeMap<>();
        mapStores = new TreeMap<>();
    }
    
    /**
     * Checks if all stores are empty (no tenges left).
     *
     * @return {@code true} if all stores are empty,
     *         {@code false} otherwise.
     */
    public boolean ok(){
        int sumStores = 0;
        for (Object object: stores){
            Store store = (Store) object;
            sumStores += store.getTenges();
        }
        if (sumStores > 0){
            return false;
        } else return true;
    }
}
import java.util.*;
import Shapes.*;
import Utilities.*;
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
    private Rectangle[] positions;
    private SpiralRoad spiral;
    private ArrayList<Robot> robots;
    private ArrayList<Store> stores;
    private TreeMap<Integer, Robot> mapRobots;
    private TreeMap<Integer, Store> mapStores;
    private TreeMap<Integer, int[]> profitPerMove;
    
    /**
     * Creates a {@code SilkRoad} of the specified length.
     * 
     * @param length the number of positions (rectangles) in the spiral road.
     */
    public SilkRoad(int length){
        if (length >= 0){
            this.length = length;
            isFinished = false;
            spiral = new SpiralRoad(length);
            positions = spiral.getArrRectangles();
            robots = new ArrayList<>();
            stores = new ArrayList<>();
            mapRobots = new TreeMap<>();
            mapStores = new TreeMap<>();
            profitPerMove = new TreeMap<>();
        } else isFinished = true;
    }
    
    public SilkRoad(int[][] days){
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
            int isRobotOrStore = elem[0];
            int position = elem[1];
            if (isRobotOrStore == 2){
                int tenges = elem[2];
                pleaseStore(position, tenges);
            } else pleaseRobot(position);
        }
    }
    
    /**
     * Places a store at the specified location with an initial amount of tenges.
     *
     * @param location the location index on the road.
     * @param tenges   the initial tenges for the store.
     */
    public void pleaseStore(int location, int tenges){
        int position = location - 1;
        if (location <= length && location > 1 && isFinished == false){
            Rectangle rectangle = positions[position];
            int xPosition = rectangle.getXPosition();
            int yPosition = rectangle.getYPosition();
            Store store = new Store(xPosition, yPosition, tenges);
            mapStores.put(location, store);
            stores.add(store);
        } else System.out.println("error please store");
    }
    
    /**
     * Removes the store located at the specified position.
     *
     * @param location the location index on the road.
     */
    public void removeStore(int location){
        if(location > length || isFinished){
            System.out.println("error remove store");
            return;
        }
        int xPosition = positions[location-1].getXPosition();
        int yPosition = positions[location-1].getYPosition();
        for (int i = 0; i < stores.size(); i++){
            Store store = stores.get(i);
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
    public void pleaseRobot(int location){
        int position = location - 1;
        if (location <= length && location >= 1 && isFinished == false){
            Rectangle rectangle = positions[position];
            int xPosition = rectangle.getXPosition();
            int yPosition = rectangle.getYPosition();
            Robot robot = new Robot(xPosition, yPosition, location);
            mapRobots.put(location, robot);
            robots.add(robot);
        }
    }
    
    /**
     * Removes the robot located at the specified position.
     *
     * @param location the location index on the road.
     */
    public void removeRobot(int location){
        if(location > length || isFinished){
            System.out.println("error remove robot");
            return;
        }
        int xPosition = positions[location-1].getXPosition();
        int yPosition = positions[location-1].getYPosition();
        for (int i = 0; i < robots.size(); i++){
            Robot robot = robots.get(i);
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
    public void moveRobot(int location, int meters){
        int newLocation = location + meters;
        if(mapRobots.containsKey(location) && newLocation <= length && isFinished == false){
            Robot robot = mapRobots.get(location);
            Rectangle rectangle = positions[newLocation - 1];
            int x = rectangle.getXPosition();
            int y = rectangle.getYPosition();
            robot.moveTo(x,y);
            System.out.println(robot.getTenges());
            robot.setTenges(robot.getTenges() - Math.abs(meters));
            mapRobots.put(newLocation, robot);
            mapRobots.remove(location); 
            if (mapStores.containsKey(newLocation)){
                int newTenges = robot.getTenges() + mapStores.get(newLocation).getTenges();
                profitPerMove.put(newLocation, new int[]{robot.getTenges(), newTenges});
                robot.setTenges(newTenges);
                mapStores.get(newLocation).removeTenges();
                mapStores.get(newLocation).increaseTimes();
            }
        } else {
            System.out.println("Error move robot");
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
            Robot robot = mapRobots.get(keyRobot); 
            int newPosition = keyRobot;
            for (int key : mapStores.keySet()) {
                Store store = mapStores.get(key);
                int cost = Math.abs(newPosition - key);
                int storeTenges = store.getTenges();
                int profit = storeTenges - cost;
                if (profit > 0) {
                    int distance = key - newPosition;
                    moveRobot(newPosition, distance);
                    newPosition += distance;
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
            stores.get(i).rebootStore();
        }
    }
    
    /**
     * Returns all robots to their initial positions.
     */
    public void returnRobots(){
        TreeMap<Integer, Robot> restorePos = new TreeMap<>();
        for(int key: mapRobots.keySet()){
            Robot robot = mapRobots.get(key);
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
        for(Robot robot: robots){
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
            storTenges.add(new int[]{key, mapStores.get(key).getTenges()});
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
            if (mapStores.get(key).getTimes() > 0){
                emptiedStores.add(new int[]{key, mapStores.get(key).getTimes()});
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
            robotTenges.add(new int[]{key, mapRobots.get(key).getTenges()});
        }
        return robotTenges;
    }
    
    /**
     * Makes the entire road visible, including the spiral,
     * stores, and robots.
     */
    public void makeVisible(){
        spiral.makeVisible();
        for (Store store: stores){
            store.makeVisible();
        }
        for (Robot robot: robots){
            robot.makeVisible();
        }
    }
    
    /**
     * Makes the entire road invisible, including the spiral,
     * stores, and robots.
     */
    public void makeInvisible(){
        spiral.makeInvisible();
        for (Store store: stores){
            store.makeInvisible();
        }
        for (Robot robot: robots){
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
        for (Store store: stores){
            sumStores += store.getTenges();
        }
        if (sumStores > 0){
            return false;
        } else return true;
    }
}
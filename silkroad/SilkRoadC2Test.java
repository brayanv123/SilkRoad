import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class SilkRoadC2Test {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(10);
    }

    @Test
    public void accordingVGShouldAddAndRemoveStores() {
        road.pleaseStore(5, 20);
        ArrayList<int[]> stores = road.stores();
        assertEquals(1, stores.size());
        assertEquals(20, stores.get(0)[1]);
        road.removeStore(5);
        stores = road.stores();
        assertEquals(0, stores.size());
    }

    @Test
    public void accordingVGShouldAddAndRemoveRobots() {
        road.pleaseRobot(3);
        ArrayList<int[]> robots = road.robots();
        assertEquals(1, robots.size());

        road.removeRobot(3);
        robots = road.robots();
        assertEquals(0, robots.size());
    }

    @Test
    public void accordingVGShouldMoveRobotAndCollectTenges() {
        road.pleaseRobot(2);
        road.pleaseStore(4, 50);

        road.moveRobot(2, 2);
        ArrayList<int[]> robots = road.robots();
        ArrayList<int[]> stores = road.stores();
        
        assertEquals(48, robots.get(0)[1]);
        assertEquals(0, stores.get(0)[1]);
    }

    @Test
    public void accordingVGShouldResupplyStores() {
        road.pleaseStore(6, 30);
        road.pleaseRobot(5);

        road.moveRobot(5, 1); 
        assertEquals(0, road.stores().get(0)[1]);

        road.resuplyStores();
        assertEquals(30, road.stores().get(0)[1]);
    }

    @Test
    public void accordingVGShouldReturnRobots() {
        road.pleaseRobot(2);
        int x0 = road.robots().get(0)[0]; 
        road.moveRobot(2, 3);
        road.returnRobots();

        int x1 = road.robots().get(0)[0];
        assertEquals(x0, x1);
    }

    @Test
    public void accordingVGShouldRebootRoad() {
        road.pleaseStore(7, 40);
        road.pleaseRobot(2);

        road.moveRobot(2, 5); 
        road.reboot();

        assertEquals(40, road.stores().get(0)[1]);
        assertEquals(35, road.robots().get(0)[1]);
    }

    @Test
    public void accordingVGShouldCalculateTotalProfit() {
        road.pleaseRobot(1);
        road.pleaseStore(2, 20);
        road.moveRobot(1, 1);
        assertEquals(19, road.profit());
    }

    @Test
    public void accordingVGShouldTrackEmptiedStores() {
        road.pleaseStore(5, 15);
        road.pleaseRobot(4);
        road.moveRobot(4, 1);
        ArrayList<int[]> emptied = road.emptiedStores();
        assertEquals(1, emptied.get(0)[1]);
    }

    @Test
    public void accordingVGShouldSaveProfitPerMove() {
        road.pleaseRobot(2);
        road.pleaseStore(4, 25);
        road.moveRobot(2, 2);

        int[][] profits = road.profitPerMove();
        assertEquals(4, profits[0][0]);
        assertEquals(23, profits[0][2]);
    }

    @Test
    public void accordingVGShouldEndWithFinish() {
        road.pleaseRobot(3);
        road.pleaseStore(5, 10);
        road.finis();

        assertTrue(road.ok()); 
        assertEquals(0, road.stores().size());
        assertEquals(0, road.robots().size());
    }

    @Test
    public void accordingVGShouldReturnFalseOnOkWhenStoresHaveTenges() {
        road.pleaseStore(8, 30);
        assertFalse(road.ok());
    }

    @Test
    public void accordingVGShouldReturnTrueOnOkWhenStoresEmpty() {
        road.pleaseStore(9, 15);
        road.pleaseRobot(8);
        road.moveRobot(8, 1);
        assertTrue(road.ok());
    }

    @Test
    public void accordingVGShouldNotAddStoreWhenInvalidLocation() {
        road.pleaseStore(20, 50); 
        assertEquals(0, road.stores().size());
    }

    @Test
    public void accordingVGShouldNotAddRobotWhenInvalidLocation() {
        road.pleaseRobot(0);
        assertEquals(0, road.robots().size());
    }

    @Test
    public void accordingVGShouldNotMoveRobotIfNoStore() {
        road.pleaseRobot(2);
        road.moveRobot(2, 2); 
        assertEquals(-2, road.robots().get(0)[1]);
    }
}

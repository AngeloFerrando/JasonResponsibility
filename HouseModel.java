import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import java.util.*;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

    // constants for the grid objects
    public static final int FRIDGE = 16;
    public static final int OWNER  = 32;
	public static final int DOOR = 64;
	public static final int DIRT = 128;

    // the grid size
    public static final int GSize = 7;

    boolean fridgeOpen   = false; // whether the fridge is open
    boolean carryingBeer = false; // whether the robot is carrying beer
    int sipCount        = 0; // how many sip the owner did
    int availableBeers  = 2; // how many beers are available

    Location lFridge = new Location(0,0);
    Location lOwner  = new Location(GSize-1,GSize-1);
	Location lDoor = new Location(0,GSize-1);
	
	// boolean[][] dirtyLocs = new boolean[][Gsize];
	
    public HouseModel() {
        // create a 7x7 grid with one mobile agent
        super(GSize, GSize, 3);

        // initial location of robot (column 3, line 3)
        // ag code 0 means the robot
        setAgPos(0, GSize/2, GSize/2);
		setAgPos(1, GSize/2, GSize/2);
		setAgPos(2, GSize/2, GSize/2);

        // initial location of fridge and owner
        add(FRIDGE, lFridge);
        add(OWNER, lOwner);
		add(DOOR, lDoor);
		
		//for(int i = 0; i < Gsize; i++) {
		//	dirtyLocs[i] = new Location[Gsize];	
		//}
    }

    boolean openFridge() {
        if (!fridgeOpen) {
            fridgeOpen = true;
            return true;
        } else {
            return false;
        }
    }

    boolean closeFridge() {
        if (fridgeOpen) {
            fridgeOpen = false;
            return true;
        } else {
            return false;
        }
    }
	
	void addDirt(int row, int col) {
		//dirtyLocs[row][col] = true;
		add(DIRT, new Location(row, col));
	}
	
	boolean clean() {
		//dirtyLocs[row][col] = false;
		remove(DIRT, getAgPos(1));
		return true;
	}

    boolean moveTowards(int id, Location dest) {
        Location r1 = getAgPos(id);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
		setAgPos(id, r1); // move the robot in the grid
				
        // repaint the fridge and owner locations
        if (view != null) {
            view.update(lFridge.x,lFridge.y);
            view.update(lOwner.x,lOwner.y);
        }
        return true;
    }

    boolean getBeer() {
        if (fridgeOpen && availableBeers > 0 && !carryingBeer) {
            availableBeers--;
            carryingBeer = true;
            if (view != null)
                view.update(lFridge.x,lFridge.y);
            return true;
        } else {
            return false;
        }
    }

    boolean addBeer(int n) {
        availableBeers += n;
        if (view != null)
            view.update(lFridge.x,lFridge.y);
        return true;
    }

    boolean handInBeer() {
        if (carryingBeer) {
            sipCount = 10;
            carryingBeer = false;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }

    boolean sipBeer() {
        if (sipCount > 0) {
            sipCount--;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }
}

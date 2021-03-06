package Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ElevatorController implements ElevatorControllerFactory {

	public static final int MAX_ELEVATORS = 16;
	Integer numberOfElevators = 0;
	Integer numberOfFloors = 0;
	ArrayList<Elevator> elevators;
	Queue<Integer> pickupLocations;

	public ElevatorController(Integer numberOfElevators, Integer numberOfFloors)
			throws InvalidNumber {
		if (numberOfElevators < 0)
			throw new InvalidNumber("Elevator number must be positive");

		this.numberOfElevators = (numberOfElevators > MAX_ELEVATORS) ? MAX_ELEVATORS
				: numberOfElevators;
		this.numberOfFloors = numberOfFloors;
		initializeElevators();
		pickupLocations = new LinkedList<Integer>();
	}

	private void initializeElevators() {
		elevators = new ArrayList<Elevator>();
		for (int i = 0; i < this.numberOfElevators; i++) {
			elevators.add(new Elevator());
		}
	}

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}

	@Override
	public void pickUp(Integer pickUpFloor) {
		pickupLocations.add(pickUpFloor);
	}

	@Override
	public void destination(Integer elevatorId, Integer destinationFloor) {
		elevators.get(elevatorId).addNewDestinatoin(destinationFloor);
	}

	@Override
	public void step() {
		// Loop though every elevator
		for (Elevator currElevator : elevators) {
			// Check to figure out which ones are unoccupied and update call
			switch (currElevator.status()) {
			case ELEVATOR_EMPTY:
				if (!pickupLocations.isEmpty())
					currElevator.addNewDestinatoin(pickupLocations.poll());
				break;
			// Move occupied Elevators one step closer to next closest
			// destination in direction
			case ELEVATOR_OCCUPIED:
				switch (currElevator.direction()) {
				case ELEVATOR_UP:
					currElevator.moveUp();
					break;
				case ELEVATOR_DOWN:
					currElevator.moveDown();
					break;
				case ELEVATOR_HOLD:
					// TODO: Check timer here to alert users that they are
					// holding the door open to long
					// TODO: Emergency situation where elevator can't be used
					// TODO: Maintenance Mode e.g. movers or maintenance people
					currElevator.popDestination();
					break;
				}
				if (currElevator.direction() == ElevatorDirection.ELEVATOR_UP)
					break;
			}
		}
	}
}

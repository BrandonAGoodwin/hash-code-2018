import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

	private static String[] files = new String[] { "a_example", "b_should_be_easy", "c_no_hurry", "d_metropolis",
			"e_high_bonus" };

	private static int rows;
	private static int columns;

	private static ArrayList<Ride> rides = new ArrayList<Ride>();

	public static ArrayList<ArrayList<Ride>> minDistanceRides = new ArrayList<ArrayList<Ride>>();

	private static int vehicleNo;
	private static BlockingQueue<Vehicle> vehicles = new LinkedBlockingQueue<Vehicle>();

	private static int rideNo;

	private static int bonus;

	private static int steps;

	public static void main(String args[]) {

		/*
		 * for (String file : files) { scheduleRides(file); } System.out.println("End");
		 */

		scheduleRides(files[3]);
		System.out.println("End");
	}

	public static void scheduleRides(String fileName) {
		try (BufferedReader r = new BufferedReader(new FileReader(fileName + ".in"));
				PrintWriter pw = new PrintWriter(new FileWriter("_" + fileName + ".txt"));) {

			String[] property = r.readLine().split(" ");

			rows = Integer.parseInt(property[0]);
			columns = Integer.parseInt(property[1]);
			vehicleNo = Integer.parseInt(property[2]);
			rideNo = Integer.parseInt(property[3]);
			bonus = Integer.parseInt(property[4]);
			steps = Integer.parseInt(property[5]);

			for (int i = 0; i < rideNo; i++) {
				String[] row = r.readLine().split(" ");

				rides.add(new Ride(i, Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2]),
						Integer.parseInt(row[3]), Integer.parseInt(row[4]), Integer.parseInt(row[5])));

			}
			//sort all rides in order of start time
			Collections.sort(rides, new Comparator<Ride>() {

				@Override
				public int compare(Ride o1, Ride o2) {
					if (o1.startTime == o2.startTime) {
						return 0;
					} else if (o1.startTime < o2.startTime) {
						return -1;
					} else {
						return 1;
					}
				}

			});

			for (int i = 0; i < vehicleNo; i++) {
				vehicles.offer(new Vehicle());
			}
			//sort rides of the same start time by shortest distance
			Integer initial = rides.get(0).startTime;
			ArrayList<Ride> currentRideStep = new ArrayList<Ride>();
			for (Ride ride : rides) {
				if (ride.startTime == initial) {
					currentRideStep.add(ride);
				} else {
					Collections.sort(currentRideStep, new Comparator<Ride>() {

						@Override
						public int compare(Ride o1, Ride o2) {
							if (o1.distance == o2.distance) {
								return 0;
							} else if (o1.distance > o2.distance) {
								return -1;
							} else {
								return 1;
							}
						}

					});
					minDistanceRides.add(currentRideStep);
					currentRideStep = new ArrayList<Ride>();
					currentRideStep.add(ride);
					initial = ride.startTime;
				}
			}

			minDistanceRides.add(currentRideStep);

			rides = new ArrayList<Ride>();
			for (ArrayList<Ride> rideList : minDistanceRides) {
				rides.addAll(rideList);
			}

			System.out.println(Arrays.deepToString(rides.toArray()));

			for (Ride ride : rides) {

				for (int i = 0; i < vehicles.size(); i++) {
					Vehicle v = vehicles.take();
					//make sure rides can end in time, if they can't see if next vehicle can
					if (ride.startTime >= v.endTime && ((steps - ride.startTime) - ride.distance) >= 0) {
						v.add(ride.rideNo, ride.distance);
						vehicles.offer(v);
						break;
					}
					vehicles.offer(v);
				}
			}

			for (Vehicle v : vehicles) {
				pw.println(v.generateOutput());
			}

		} catch (FileNotFoundException e) {
			System.err.println("File '" + fileName + "' not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Ride {

	public Integer rideNo;

	public Integer startX;
	public Integer startY;
	public Integer endX;
	public Integer endY;
	public Integer startTime;
	public Integer endTime;

	public Double distance;

	public Ride(Integer rideNo, Integer startX, Integer startY, Integer endX, Integer endY, Integer startTime,
			Integer endTime) {
		this.rideNo = rideNo;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.startTime = startTime;
		this.endTime = endTime;

		distance = calculateDistance() * 2;
	}

	private Double calculateDistance() {
		return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
	}

	public String toString() {
		return rideNo.toString();
	}

}

class Vehicle {

	public ArrayList<Integer> rides = new ArrayList<Integer>();
	public Integer endTime = -1;

	public void add(Integer rideNo, Double endTime) {
		rides.add(rideNo);
	}

	public String generateOutput() {
		String s = "";

		s += rides.size() + " ";

		// System.out.println("1: " + s);

		for (Integer i : rides) {
			// System.out.println("i: " + i);
			s += i + " ";
		}

		s = s.substring(0, s.length() - 1);
		// System.out.println("2: " + s);
		return s;
	}

}

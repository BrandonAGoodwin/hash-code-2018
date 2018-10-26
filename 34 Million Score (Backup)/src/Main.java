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

	private static final String INPUT_FILE_NAME = "e_high_bonus.in";
	private static final String OUTPUT_FILE_NAME = "high.txt";

	private static int rows;
	private static int columns;

	private static ArrayList<Ride> rides = new ArrayList<Ride>();

	private static int vehicleNo;
	private static BlockingQueue<Vehicle> vehicles = new LinkedBlockingQueue<Vehicle>();

	private static int rideNo;

	private static int bonus;

	private static int steps;

	public static void main(String args[]) {
		readData();
	}

	public static void readData() {
		try (BufferedReader r = new BufferedReader(new FileReader(INPUT_FILE_NAME));
				PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE_NAME));) {

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

			for (Ride ride : rides) {
				Vehicle v = vehicles.take();
				if (ride.startTime > v.endTime) {
					v.add(ride.rideNo, ride.distance);
				}
				vehicles.offer(v);
			}

			for (Vehicle v : vehicles) {
				pw.println(v.generateOutput());
			}

		} catch (FileNotFoundException e) {
			System.err.println("File '" + INPUT_FILE_NAME + "' not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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

		distance = calculateDistance();
	}

	private Double calculateDistance() {
		return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
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
		// System.out.println(Arrays.deepToString(rides.toArray()));

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

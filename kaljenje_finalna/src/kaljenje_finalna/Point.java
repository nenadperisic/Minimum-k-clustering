package kaljenje_finalna;

import java.util.ArrayList;
import java.util.List; 
import java.util.Random; 

public class Point { 
	private int x; 
	private int y; 

	public Point(int x, int y) { 
		this.x = x; 
		this.y = y; 
	} 

	public int getCoordX() { 
		return this.x; 
	} 

	public int getCoordY() { 
		return this.y; 
	} 

	public static Point createPoint(int x, int y) { 
		return new Point(x,y); 
	} 

	public static List<Point> createPoints(int numOfPoints, long seed) { 
		List<Point> points = new ArrayList<Point>(5);
		Random generator = new Random(seed); 

		for(int i = 0; i < numOfPoints; i++) {
			int x = generator.nextInt(101);
			int y = generator.nextInt(101);
			points.add(createPoint(x,y));
		}
		
		return points; 
	} 

	public void printPoint() { 
		int x = this.getCoordX(); 
		int y = this.getCoordY(); 
		System.out.format("\nPoint: (%d, %d)", x, y); 
	} 

} 
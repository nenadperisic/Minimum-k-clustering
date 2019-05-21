package kaljenje_finalna;

import java.util.ArrayList;
import java.util.List; 

public class Cluster { 
		
	private List<Point> points; 
	private int pointCount; 
	private int clusterID; 

	
	public Cluster(int c) { 
		this.clusterID = c; 
		this.points = new ArrayList<Point>();
		this.pointCount = 0; 
	} 

	public void addPoint(Point x) { 
		this.points.add(x); 
		this.pointCount = this.pointCount + 1; 
	} 

	public static Cluster createCluster(int c) { 
		return new Cluster(c); 
	}

	public void removePoint(int x) { 
		this.points.remove(x); 
		this.pointCount = this.pointCount - 1; 
	} 

	public int getCount() { 
		return this.pointCount; 
	} 

	public Point getPoint(int x) { 
		return (Point) this.points.get(x); 
	} 

	public static double euclideanDistance(Point a, Point b) { 
		return Math.sqrt(Math.pow(a.getCoordX() - b.getCoordX(), 2) + 
		Math.pow(a.getCoordY() - b.getCoordY(), 2)); 
	} 


	public double intraClusterDistance() {
		double max = 0;
		for(int i = 1; i < this.getCount(); i++) { 
			for (int j = 1; j < this.getCount(); j++){
				Point point1 = (Point) this.getPoint(i - 1);
				Point point2 = (Point) this.getPoint(j);
				double max1 = euclideanDistance(point1, point2);
				if(max1 > max){
					max = max1;
				}
			}
		}
		return max;
	} 


	public Cluster deepCopy(){
	    Cluster copy = new Cluster(this.clusterID);
	    copy.pointCount = this.getCount();
	    copy.clusterID = this.clusterID;
	    
	    for (int i = 0; i < this.pointCount; i++) {
	    	copy.points.add(this.points.get(i));
	    }
	    return copy;
	}
	

	public void printPoints(int c) { 
		System.out.println("Cluster: " + (c+1));
		for(int i = 0; i < this.pointCount; i++) { 
			Point point = (Point) this.getPoint(i); 
			int x = point.getCoordX(); 
			int y = point.getCoordY();
			System.out.format("Point %d: (%d, %d)\n", (i + 1), x, y); 
		}
		System.out.println(); 
	} 

}
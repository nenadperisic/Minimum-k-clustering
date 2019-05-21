package kaljenje_finalna;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList; 
import java.util.List; 
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane; 


public class Main { 
	public static void main(String[] args) {
		
		List<Point> points = new ArrayList<Point>(); 
		ArrayList<Cluster> solution = new ArrayList<Cluster>();
		ArrayList<Cluster> neighbor = new ArrayList<Cluster>(); 

		String seedStr = JOptionPane.showInputDialog("Enter the seed for random number", JOptionPane.PLAIN_MESSAGE); 
		long seed = Integer.parseInt(seedStr); 
		
		Random generator = new Random(seed); 
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the number of points ");
		int numOfPoints = sc.nextInt();
		points = Point.createPoints(numOfPoints, seed);
		System.out.println("Please enter the number of clusters ");
		int numOfClusters = sc.nextInt();
		sc.close();
		long startTime = System.nanoTime();
		
		for (int i = 0; i < numOfClusters; i++) {
			solution.add(Cluster.createCluster(i));
		}
		
		int size = points.size();
		double temp = size*1000;
		double coolRate = 0.003; 
		int x = 0; 
		
		for(int i = 0; i < size; i++) {
			x = generator.nextInt(numOfClusters);
			generateInitialSolution(solution, points, seed, x, i);
		}

		double solutionCost = 0;
		
		double max = 0;
		for (int i = 0; i < numOfClusters; i++) {
			double tmp = solution.get(i).intraClusterDistance();
			if (tmp >=max) {
				max = tmp;
			}
		}
		solutionCost = max;

		neighbor = copySolution(solution, neighbor);

		
		double neighborCost = solutionCost;

		int numOfIterations = 0;
		while (temp > 1) {

			x = generator.nextInt(points.size());
			int y = generator.nextInt(numOfClusters);

			int yy = generator.nextInt(numOfClusters); 
			while(yy == y) 
				yy = generator.nextInt(numOfClusters);

			generateNeighbor(neighbor, points, seed, x, y, yy);
			
			max = 0;
			for (int i = 0; i < numOfClusters; i++) {
				double tmp = neighbor.get(i).intraClusterDistance();
				if (tmp >=max) {
					max = tmp;
				}
			}
			
			neighborCost = max;
			
			if(neighborCost < solutionCost) {
				solutionCost = neighborCost;

				solution.clear();
				solution = copyNeighbor(solution, neighbor);
				
				for (int i = 0; i < numOfClusters; i++) {
					Cluster cluster = (Cluster)solution.get(i);
					System.out.printf("Cluster " + (i+1) + " Intra Cluster Distance = %f", cluster.intraClusterDistance());
					System.out.println("");
				}
				System.out.println(solutionCost + "\n");
				
			}
			
			if(neighborCost > solutionCost) {
				double acceptanceProbability = acceptanceProbability(neighborCost, solutionCost, temp);

				if( acceptanceProbability > 0.9999997) {
					System.out.printf("\nAcceptance Probability: %f", acceptanceProbability);
					solutionCost = neighborCost;
					solution.clear();
					solution = copyNeighbor(solution, neighbor);
				}
			}

			temp = temp - coolRate;
			numOfIterations++;
		} 
		
		long endTime = System.nanoTime();
		
		PrintWriter pw = null;
		PrintWriter pointsFile = null;
		try {
			pw = new PrintWriter("clusters.txt", "UTF-8");
			pointsFile = new PrintWriter("points.txt", "UTF-8");

			for (int i = 0; i < size; i++) {
				if (i == points.size()-1) {
					pointsFile.format("%d,%d", points.get(i).getCoordX(), points.get(i).getCoordY());
				}
				else {
					pointsFile.format("%d,%d\n", points.get(i).getCoordX(), points.get(i).getCoordY());						
				}
			}
			
			for(int i = 0; i < solution.size(); i++) {
				Cluster cluster_i = (Cluster)solution.get(i);
				cluster_i.printPoints(i);
				
				pw.println("Cluster " + (i+1));
				for(int j = 0; j < cluster_i.getCount(); j++) { 
					Point beta = (Point) cluster_i.getPoint(j); 
					int a = beta.getCoordX(); 
					int b = beta.getCoordY();
					pw.format("Point %d: (%d, %d)\n", (j + 1), a, b);
				}
				pw.println();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			pw.close();
			pointsFile.close();
		}
		
		System.out.println("\nNumber of iterations: " + numOfIterations);
		System.out.println("Solution cost: " + solutionCost);
		
		long finalTime = endTime - startTime;
		System.out.println("Execution time: " + finalTime / 1000000000 + "." + finalTime / 1000000 + " seconds");
		
	}

	private static double acceptanceProbability(double neighborCost, double solutionCost, double temp) { 
		double a = (solutionCost - neighborCost) / temp; 
		double ap = Math.exp(a); 
		return ap;
	} 

	private static void generateInitialSolution(List<Cluster> solution, List<Point> points, long seed, int x, int y) { 
		Cluster cluster = (Cluster)solution.get(x); 
		Point point = (Point)points.get(y); 
		cluster.addPoint(point);
	}

	private static void generateNeighbor(List<Cluster> neighbor, List<Point> points, long seed, int x, int y, int yy) { 
		Point point = (Point)points.get(x); 
		int cluster = 0; 
		int pointNum = 0; 

		for(int i = 0; i < neighbor.size(); i++) { 
			Cluster cluster1 = (Cluster)neighbor.get(i); 

			for(int j = 0; j < cluster1.getCount(); j++) { 
				Point point1 = (Point)cluster1.getPoint(j); 
				if(point1.equals(point)) { 
					cluster = i; 
					pointNum = j; 
				} 
			} 
		} 

		Cluster cluster2 = (Cluster) neighbor.get(cluster); 
		cluster2.removePoint(pointNum); 

		Cluster cluster3 = (Cluster) neighbor.get(yy); 
		cluster3.addPoint(point); 
	} 
	
	
	public static ArrayList<Cluster> copySolution(ArrayList<Cluster> solution, ArrayList<Cluster> neighbor) {
		for (Cluster cl : solution) {
			Cluster copy = cl.deepCopy();
			neighbor.add(copy);
		}
		return neighbor;		
	}
	
	public static ArrayList<Cluster> copyNeighbor(ArrayList<Cluster> solution, ArrayList<Cluster> neighbor) {
		for (Cluster cl : neighbor) {
			Cluster copy = cl.deepCopy();
			solution.add(copy);
		}
		return solution;		
	}
	
} 
package world.helper.properties;

import java.awt.Point;

import world.helper.math.HXFloatPoint;
import world.helper.math.HXLine;
import world.helper.math.HXVector;

public interface HXInteractable {
	
	public void interactNotify();
	
	public void interactImpulse(int xDist, int yDist);
	
	public void interactStop();
	
	/**
	 * Get all of the corners of the polygon.
	 * @return Array of points.
	 */
	public HXVector[] getCorners();
	
	public boolean isInteracting();

	/**
	 * Checks if point is inside polygon. Polygon must have at least 3 corners,
	 * and can be convex or concave.
	 * @param x - X coordinate of point to check.
	 * @param y - Y coordinate of point to check.
	 * @param corners - The corners of the polygon.
	 * @return True if the point is located within the bounds formed by the polygon.
	 */
	public static Boolean isPointInPolygon(int x, int y, HXVector[] corners) {
		return isPointInPolygon(new Point(x, y), corners);
	}
	
	/**
	 * Checks if point is inside polygon. Polygon must have at least 3 corners,
	 * and can be convex or concave.
	 * @param p - The point to check.
	 * @param corners - The corners of the polygon.
	 * @return True if the point is located within the bounds formed by the polygon.
	 */
	public static Boolean isPointInPolygon(Point p, HXVector[] corners) {
		if (corners.length < 3) {
			return false;
		}
		
//		System.out.println("Intersection point: " + p.x + " , " + p.y);
//		System.out.println("           Corners: 0 " + corners[0]);
//		System.out.println("                    1 " + corners[1]);
//		System.out.println("                    2 " + corners[2]);
//		System.out.println("                    3 " + corners[3]);
		
		int maxX = (int) corners[0].x;
		int minX = (int) corners[2].x;
		int maxY = (int) corners[0].y;
		int minY = (int) corners[2].y;
		for (int c = 1; c < corners.length; c++ ) {
			if ((int) corners[c].x > maxX) {
				maxX = (int) corners[c].x;
				minX = (int) corners[(c+2)%4].x;
//				System.out.println("No use this corner: " + c + " with min: " + (c+2)%4);
			}
			if ((int) corners[c].y > maxY) {
				maxY = (int) corners[c].y;
				minY = (int) corners[(c+2)%4].y;
//				System.out.println("No use this corner: " + c + " with min: " + (c+2)%4);
			}
		}
		// Preliminary soft check
		if (p.x < minX || p.x > maxX || p.y < minY || p.y > maxY) {
//			System.out.println("Intersection check: \tminX \tmaxX \tminY \tmaxY");
//			System.out.println("                    \t"+minX+" \t"+maxX+" \t"+minY+" \t"+maxY);
			return false;
		}
		
		// === Ray Trace Check ===
		// Establish e to avoid axis aligned bounding box fail
		float e = (minX - minY)/100;
		HXLine ray = new HXLine(new HXFloatPoint(minX - e, p.y), new HXFloatPoint(p.x, p.y));
		
		HXLine[] side = new HXLine[4];
		side[0] = new HXLine(corners[0], corners[1]);
		side[1] = new HXLine(corners[1], corners[2]);
		side[2] = new HXLine(corners[2], corners[3]);
		side[3] = new HXLine(corners[3], corners[0]);
		
		int intersectionCount = 0;
		for (int s = 0; s < corners.length; s++) {
			intersectionCount += HXLine.intersecting(ray, side[s]);
		}
		// Odd number of intersections is inside, even number is outside
		if (intersectionCount%2 == 1) {
//			System.out.println("Intersected check: " + intersectionCount);
			return true;
		} else {
//			System.out.println("Not ntersected check: " + intersectionCount);
			return false;
		}
	}

}

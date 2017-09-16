package world.helper.math;

public class HXLine {
	
	public HXVector a;
	public HXVector b;
	
	/**
	 * Construct line from between two points.
	 */
	public HXLine(HXFloatPoint p1, HXFloatPoint p2) {
		this.a = new HXVector(p1.x, p1.y);
		this.b = new HXVector(p2.x, p2.y);
	}
	public HXLine(HXVector p1, HXVector p2) {
		this.a = p1;
		this.b = p2;
	}
	
	/**
	 * Returns a value signifying the intersection of two lines.
	 * @param l1 - Line 1.
	 * @param l2 - Line 2.
	 * @return Returns 0 if no intersection. Returns 1 if intersected.
	 *  Returns 2 if on the same axis.
	 */
	public static int intersecting(HXLine l1, HXLine l2) {
		double a1 = l1.b.y - l1.a.y;
		double b1 = l1.a.x - l1.b.x;
		double c1 = (l1.b.x * l1.a.y) - (l1.a.x * l1.b.y);
		
		double d1 = (a1 * l2.a.x) + (b1 * l2.a.y) + c1;
		double d2 = (a1 * l2.b.x) + (b1 * l2.b.y) + c1;
		
		if (d1 > 0 && d2 > 0)
			return 0;
		if (d1 < 0 && d2 < 0)
			return 0;
		
		double a2 = l2.b.y - l2.a.y;
		double b2 = l2.a.x - l2.b.x;
		double c2 = (l2.b.x * l2.a.y) - (l2.a.x * l2.b.y);
		
		d1 = (a2 * l1.a.x) + (b2 * l1.a.y) + c2;
		d2 = (a2 * l1.b.x) + (b2 * l1.b.y) + c2;
		
		if (d1 > 0 && d2 > 0)
			return 0;
		if (d1 < 0 && d2 < 0)
			return 0;
		
		if ((a1 * b2) - (a2 * b1) == 0) {
			return 2;
		}
		
		return 1;
	}

}

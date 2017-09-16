package world.helper.properties;

import java.math.BigDecimal;

import world.entities.HXEntity;
import world.helper.math.HXVector;

public class HXCollider {
	
	private HXEntity parent;
	
	// Collider vertices
	private HXVector[] vertices;
	// Average center of verties
	private HXVector center = new HXVector(0, 0);;
	// Bounding circle radius
	private double boundingRadius = 0;
	
	// Collider Results
	boolean colliding = true;
	HXVector mtvDirection = new HXVector();
	double mtvMagnitude = 0;
	boolean mtvMagnitudeSet = false;
	
	// Constructor
	public HXCollider(HXVector[] vertices, HXEntity parent) {
		this.parent = parent;
		
		this.vertices = new HXVector[vertices.length];
		this.vertices = vertices;
		
		if (vertices.length != 0) {
			
			// Calculate center
			double midX = 0;
			double midY = 0;
			for (int v = 0; v < vertices.length; v++) {
				midX += vertices[v].x;
				midY += vertices[v].y;
			}
			midX /= vertices.length;
			midY /= vertices.length;
			this.center.x = midX;
			this.center.y = midY;
		
			// Calculate smallest circle that encompasses all vertices
			double farthestDistance = 0;
			for (int v = 0; v < vertices.length; v++) {
				double distSqrd = Math.pow(vertices[v].x - midX, 2) + Math.pow(vertices[v].y - midY, 2);
				if (distSqrd > farthestDistance) {
					farthestDistance = distSqrd;
				}
			}
			this.boundingRadius = Math.sqrt(farthestDistance);
		}
	}
	
	// Mark: TRANSFORMATIONS
	// =====================
	
	/**
	 * Translate the collider.
	 * <p>
	 * Applies a translation to the vertices of the collider.
	 * @param x - Amount in the x direction to translate collider vertices.
	 * @param y - Amount in the y direction to translate collider vertices.
	 */
	public void translateBy(double x, double y) {
		center.x += x;
		center.y += y;
		for (int v = 0; v < vertices.length; v++) {
			vertices[v].x += x;
			vertices[v].y += y;
		}
	}
	/**
	 * Rotate the collider.
	 * <p>
	 * Applies a rotation to the vertices of the collider.
	 * @param rads - The amount in radians to rotate the collider.
	 * @param anchor - The point around which to rotate the collider.
	 */
	public void rotateBy(double rads, HXVector anchor) {
		for (int v = 0; v < vertices.length; v++) {
			
			// The following has been converted to BigDecimal to avoid (floating point) rounding errors
			// vertices[v].x = anchor.x + (vertices[v].x - anchor.x) * Math.cos(rads) - (vertices[v].y - anchor.y) * Math.sin(rads);
			// vertices[v].y = anchor.y + (vertices[v].x - anchor.x) * Math.sin(rads) + (vertices[v].y - anchor.y) * Math.cos(rads);
			
			BigDecimal t0 = BigDecimal.valueOf(Math.cos(rads)).multiply(BigDecimal.valueOf(vertices[v].x - anchor.x));
			BigDecimal t1 = BigDecimal.valueOf(Math.sin(rads)).multiply(BigDecimal.valueOf(vertices[v].y - anchor.y));
			BigDecimal t2 = BigDecimal.valueOf(Math.sin(rads)).multiply(BigDecimal.valueOf(vertices[v].x - anchor.x));
			BigDecimal t3 = BigDecimal.valueOf(Math.cos(rads)).multiply(BigDecimal.valueOf(vertices[v].y - anchor.y));
			
			vertices[v].x = t0.subtract(t1).add(BigDecimal.valueOf(anchor.x)).doubleValue();
			vertices[v].y = t2.add(t3).add(BigDecimal.valueOf(anchor.y)).doubleValue();
		}
	}
	public void rotateBy(double rads) {
		this.rotateBy(rads, this.getCenter());
	}
	/**
	 * Scale the collider
	 * <p>
	 * Applies a scaling factor to the vertices of the collider.
	 * @param scale - Amount to increase or decrease collider shape by.
	 * @param anchor - The center point of the shape.
	 */
	public void scaleBy(double scale, HXVector anchor) {
		for (int v = 0; v < vertices.length; v++) {
			vertices[v].x = anchor.x + (vertices[v].x - anchor.x) * scale;
			vertices[v].y = anchor.y + (vertices[v].y - anchor.y) * scale;
		}
	}
	
	// Mark: COLLISION TESTING
	// =======================
	/**
	 * Calculate overlap between another collider and this collider.
	 * <p>
	 * This method tests for distance between centers and if necessary 
	 * uses the Separating Axis Theorem method for determing precise
	 * overlap. Will return the minimum translation vector if overlap
	 * is found.
	 * @param peer - The other HXCollider to test for overlap against.
	 * @return 
	 * - Will return a vector representing the MTV (minimum
	 * translation vector) to displace the shapes away from each other.
	 * <br>
	 * - Returns <b>null</b> if shapes do not overlap.
	 */
	public HXVector doesOverlap(HXCollider peer) {
		
//		// Find center points for preliminary check of circle intersections
//		double aMidX = this.getCenter().x;
//		double aMidY = this.getCenter().y;
//		double bMidX = peer.getCenter().x;
//		double bMidY = peer.getCenter().y;
//		// Avoid square roots so distance stays as distance squared.
//		double centersDistanceSqrd = Math.pow(aMidX - bMidX, 2) + Math.pow(aMidY - bMidY, 2);
//		double aRadius = this.getBoundingRadius() + 10; // Add arbitrary buffer to avoid rounding errors
//		double bRadius = peer.getBoundingRadius() + 10;
//		// (D > aR + bR)^2 == (D^2 > aR^2 + aR*bR + bR^2)
//		if (centersDistanceSqrd > Math.pow(aRadius, 2) + aRadius * bRadius + Math.pow(bRadius, 2)) {
//			return null;
//		}
		
		// === Separating Axis Theorem ===
		// If able to get past preliminary circle check, use precision check.
		// Apply SAT:
		
//		// Apply velocity (non-angular) of parent object to detect if the shape *will* overlap next update
//		HXVector[] thisVertices = new HXVector[this.getVertices().length];
//		for (int v = 0; v < this.getVertices().length; v++) {
//			thisVertices[v] = this.getVertices()[v].translate(this.getParent().getxVel(), this.getParent().getyVel());
//		}
		
		// In the case of parallel lines, need to note this vector for checking direction
		HXVector betweenCenters = new HXVector(this.getCenter(), peer.getCenter());
		// Reset all values
		mtvDirection = new HXVector();
		mtvMagnitude = 0;
		mtvMagnitudeSet = false;
		colliding = true;
		// Using axes on this HXCollider, test both this/peer vertices
		testVerticesOnAxes(this.getVertices(), peer.getVertices());
		if (colliding) {
			// If still collidiing, use axes on peer HXCollider, test both this/peer vertices
			testVerticesOnAxes(peer.getVertices(), this.getVertices());
		}
		if (!colliding) {
			return null;
		}
		// Parallel lines fix: 
		if (HXVector.dot(betweenCenters, mtvDirection) < 0) {
			mtvDirection = mtvDirection.opposite();
		}
		// An mtv direction and magnitude has been set by now so return mtv
		double xComp = mtvDirection.x * mtvMagnitude;
		double yComp = mtvDirection.y * mtvMagnitude;
		HXVector mtv = new HXVector(xComp, yComp);
		
		// If all four axes had projection overlap then the objects overlap
		if (mtv.x != 0 || mtv.y != 0) {
			System.out.println("Overlap: " + mtv.x + ", " + mtv.y);
		}
		return mtv;
	}
	/**
	 * Test for overlap from two shapes on one axis set.
	 * <p>
	 * The axes iterated through are from the first array of vertices supplied.
	 * Vertices from both arrays are projected onto the axes created by the first
	 * array of vertices. Will update mtvDirection and mtvMagnitude with the smallest
	 * values of overlap calculated.
	 * @param thisAxisVertices - The first shape and source of axes.
	 * @param peerAxisVertices - the second shape.
	 */
	private void testVerticesOnAxes(HXVector[] thisAxisVertices, HXVector[] peerAxisVertices) {
		// Number of axes == number of vertices
		for (int a = 0; a < thisAxisVertices.length; a++) {
			// Generate axis
			HXVector axis = new HXVector(thisAxisVertices[(a+1)%thisAxisVertices.length], thisAxisVertices[a]).perpendicular().normalize();
			
			HXVector minMaxThis = minMaxForAxis(thisAxisVertices, axis);
			HXVector minMaxPeer = minMaxForAxis(peerAxisVertices, axis);
			
			double overlap = Math.min((minMaxThis.y - minMaxPeer.x),(minMaxPeer.y - minMaxThis.x));
			
			if (minMaxPeer.x > minMaxThis.y || minMaxPeer.y < minMaxThis.x) {
				// No overlap = break out of SAT check, no collision present
				colliding = false;
				return;
			} else if (mtvMagnitudeSet && overlap < mtvMagnitude) {
				mtvDirection = axis;
				mtvMagnitude = overlap;
			} else if (!mtvMagnitudeSet) {
				mtvDirection = axis;
				mtvMagnitude = overlap;
				mtvMagnitudeSet = true;
			}
		}
	}
	/**
	 * Calculate min/max dot products.
	 * <p>
	 * Calculates the minimum and maxmimum dot product on the axis with
	 * the given vertices. Note return syntax.
	 * @param verts - The vectors to project onto the axis.
	 * @param axis - The axis to be projected onto.
	 * @return - Will return a vector whose x value is the minimum and
	 * whose y value is the maximum.
	 */
	private HXVector minMaxForAxis(HXVector[] verts, HXVector axis) {
		// Dot product all corners with the axis then select max/min for object
		double min = HXVector.dot(verts[0], axis);
		double max = min;
		// Find max/min dot of this object.
		for (int c = 1; c < verts.length; c++) {
			double value = HXVector.dot(verts[c], axis);
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}
		return new HXVector(min, max);
	}
	
	
	// Mark: GETTERS & SETTERS
	// =======================
	public HXVector[] getVertices() {
		return this.vertices;
	}
	public HXVector getCenter() {
		return this.center;
	}
	public double getBoundingRadius() {
		return this.boundingRadius;
	}
	public HXEntity getParent() {
		return this.parent;
	}

}

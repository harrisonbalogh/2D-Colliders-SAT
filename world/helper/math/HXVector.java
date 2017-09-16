package world.helper.math;

import java.text.DecimalFormat;


public class HXVector {
	
	public double x;
	public double y;
	
	public HXVector(double xComponent, double yComponent) {
		x = xComponent;
		y = yComponent;
	}
	public HXVector(HXVector a, HXVector b) {
		x = b.x - a.x;
		y = b.y - a.y;
	}
	
	public HXVector() {
		this (0, 0);
	}
	
	public HXFloatPoint toHXFloatPoint() {
		return new HXFloatPoint((float) x, (float) y);
	}
	
	public HXVector opposite() {
		return new HXVector(x/-1, y/-1);
	}
	
	public HXVector perpendicular() {
		return new HXVector(this.y, -this.x);
	}
	
	public HXVector normalize() {
		float magnitude = (float) Math.sqrt(this.x*this.x + this.y*this.y);
		return new HXVector(this.x / magnitude, this.y / magnitude);
	}
	public HXVector translate(double xTranslation, double yTranslation) {
		return new HXVector(this.x + xTranslation, this.y + yTranslation);
	}
 
	/**
	 * HXVector 'a' projected onto HXVector 'b'.
	 * @param a - An HXVector.
	 * @param b - An HXVector.
	 * @return The resultant vector of a projected onto b.
	 */
	public static HXVector proj(HXVector a, HXVector b) {
		double calc = dot(a, b) / (b.x * b.x + b.y * b.y);
//		System.out.println("                   - Vector calc: " + calc);
		double xComponent = b.x * calc;
		double yComponent = b.y * calc;
		
		return new HXVector(xComponent, yComponent);
	}
	
	public static double dot(HXVector a, HXVector b) {
		return a.x * b.x + a.y * b.y;
	}
	
	public double getMagnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public HXVector toUnitVector() {
		double magnitude = getMagnitude();
		return new HXVector(x / magnitude, y / magnitude);
	}
	
	public String toString() {
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
        return "V(" + df.format(x) +", " + df.format(y) + ")";
	}
}

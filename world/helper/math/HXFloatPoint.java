package world.helper.math;

public class HXFloatPoint {
	
	public float x;
	public float y;
	
	public HXFloatPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public HXFloatPoint() {
		this(0, 0);
	}
	
	public String toString() {
        return "HXFloatPoint(x: " + x +", y: " + y + ")";
    }
}

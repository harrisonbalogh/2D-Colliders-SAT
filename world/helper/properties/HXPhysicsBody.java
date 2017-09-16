package world.helper.properties;

import world.helper.math.HXVector;

public interface HXPhysicsBody {

	public void applyForce(HXVector v);
	
	public boolean isStaticObject();
	
}

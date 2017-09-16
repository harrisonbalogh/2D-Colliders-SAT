package world.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import world.HXWorld;
import world.helper.properties.HXCollider;
import world.helper.math.HXLine;
import world.helper.math.HXVector;

public abstract class HXEntity {
	
	private double xPos;
	private double yPos;
	private double rotation = 0;
	// For interpolating rendering. Reference previous positions
	private double xPos_prev;
	private double yPos_prev;
//	private double xAnchor = 0.5; // NYI
	private double width;
	private double height;
	private double scale;
	// draw_ variables are affected by scale
	private int    draw_xPos;
	private int    draw_yPos;
	
	private boolean interacting = false;
	private boolean intersecting = false;
	private HXWorld parentWorld;
	private Image img;
	private double mass;
	private double lifespan; // an entity with -1 lifespan will not decay
	private double xVel; // x velocity
	private double yVel; // y velocity
	private double rVel; // rotational velocity
	
	private double LIFESPAN_DECAY = 0.03;
	
	private HXVector GRAVITY = new HXVector(0, -1); // update ignores GRAVITY.x
	private double TERMINAL_FREE_FALL = 5;
	private boolean statis = false; // Locks place of entity, unless interacting with
	
	// PHYSICS BODY
	// Friction can still be applied to an entity w/o a physicsBody
	private double FRICTION = 0.2;
	private double sphere_radius; // Circle around square shape.
	
	private HXCollider collisionBody = null;
	private double   netForceMagnitude = 0;
	private HXVector netForceDirection = new HXVector(0,0);
	
	
	private HXLine mtvLine = new HXLine(new HXVector(), new HXVector());

	/**
	 * The constructors of the classes in the 'entities' package should call this init() method.
	 * <p>
	 * Adds the newly instantiated entity to the parent HXWorld's array list of all entities and instatiates a new Rectangle object.
	 * @param x - x coordinate of spawn location.
	 * @param y - y coordinate of spawn location.
	 * @param w - width of entity.
	 * @param h - height of entity.
	 * @param m - mass of entity.
	 * @param parent - HXWorld this entity will belong to.
	 */
	protected void init(
			Image i, 
			double x, double y, 
			double withWidth, double withHeight, 
			double velX, double velY, 
			double withMass, double life, 
			double withScale, boolean hasCollider,
			HXWorld parent) {
		this.img = i;
		this.xPos = x;
		this.yPos = y;
		this.width = withWidth;
		this.height = withHeight;
		this.scale = withScale;
		this.mass = withMass;
		this.xPos_prev = x;
		this.yPos_prev = y;
		this.lifespan = life;
		this.parentWorld = parent;
		this.xVel = velX;
		this.yVel = velY;
		
		this.sphere_radius = Math.sqrt(Math.pow((width/2), 2) + Math.pow((height/2), 2));
		
		this.parentWorld.entityAdd(this);
//		if (this instanceof HXPhysicsBody) {
//			this.parentWorld.physicsBodyAdd(this);
//		}
		
		if (hasCollider) {
			double c1x = xPos;
			double c1y = yPos;
			double c2x = xPos + width;
			double c2y = yPos;
			double c3x = xPos + width;
			double c3y = yPos + height;
			double c4x = xPos;
			double c4y = yPos + height;
			
			collisionBody = new HXCollider(new HXVector[]{
					new HXVector(c1x,c1y),
					new HXVector(c2x,c2y),
					new HXVector(c3x,c3y),
					new HXVector(c4x,c4y)
					}, this);
			this.getWorld().colliderAdd(collisionBody);
		}
		
	}
	
	/**
	 * Called by HXWorldPanel within its paintComponent() based on HXClock repaint timer.
	 * <p>
	 * Uses interpolation on constant timestep in HXClock to do smooth drawing as well as update the Rectangle object collider.
	 * @param g - The Graphics object context that will get painted on.
	 * @param interpolation - Sent by the HXClock to smooth movements when thread stutters or CPU lags.
	 */
	public void draw(Graphics g, float interpolation) { 
		draw_xPos = (int) (((xPos - xPos_prev) * interpolation + xPos_prev) * scale);
		draw_yPos = (int) (((yPos - yPos_prev) * interpolation + yPos_prev) * scale);
		
		g.setColor(Color.RED);
		g.drawLine((int) mtvLine.a.x, (int) mtvLine.a.y, (int) mtvLine.b.x, (int) mtvLine.b.y);
	}
	
	/**
	 * Called whenever an entity needs to be updated.
	 * <p>
	 * Used in conjunction with another class changing an entity's x or y positions.
	 */
	public void update(double dT) {
		
		if (this instanceof Square) {
//			yVel = Math.max(yVel - GRAVITY.y, -TERMINAL_FREE_FALL);
		}
		
		
		xPos += xVel;
		yPos += yVel;
		
		xPos_prev = xPos;
		yPos_prev = yPos;
		rotation = (rotation + rVel);
		if (rotation >= 2 * Math.PI) {
			rotation -= (2 * Math.PI);
		} else if (rotation < 0) {
			rotation += (2 * Math.PI);
		}
		
		if (collisionBody != null) {
			if (!intersecting) {
				mtvLine.a.x = collisionBody.getCenter().x;
				mtvLine.a.y = collisionBody.getCenter().y;
				mtvLine.b.x = collisionBody.getCenter().x;
				mtvLine.b.y = collisionBody.getCenter().y;
			}
			
			collisionBody.translateBy(xVel, yVel);
			collisionBody.rotateBy(rVel);
		}
		
		
		if (lifespan != -1) {
			lifespan -= LIFESPAN_DECAY;
			if (lifespan <= 0) {
				remove();
			}
		}
		
		
		
		// TODO Change xPos or yPos variables for movement
	}
	
	private void netForces() {
		if (!intersecting) {
			
		}
	}
	
	/**
	 * Used to delete an entity
	 * <p>
	 * Removes the caller from the world's entity array list.
	 */
	public void remove() {
		this.parentWorld.entityRemove(this);
		this.parentWorld.physicsBodyRemove(this);
	}
	public void interactImpulse(int xDist, int yDist) {
		collisionBody.translateBy(xDist - width/2 - xPos, yDist - height/2 - yPos);
		xPos = xDist - width/2;
		yPos = yDist - height/2;
	}
	public void intersectApplyMTV(HXVector mtv) {
		
		mtvLine.a.x = collisionBody.getCenter().x;
		mtvLine.a.y = collisionBody.getCenter().y;
		mtvLine.b.x = collisionBody.getCenter().x + mtv.x;
		mtvLine.b.y = collisionBody.getCenter().y + mtv.y;
		
		if (!interacting && !statis) {
			this.xPos -= mtv.x;
			this.yPos -= mtv.y;
			
			collisionBody.translateBy(-mtv.x, -mtv.y);
		}
	}
	public HXVector[] getCorners() {
		return collisionBody.getVertices();
	}
	
	// ============================   MARK: Getters and Setters ============================ 
	public boolean isInteracting() {
		return interacting;
	}
	public void interactNotify() {
		interacting = true;
	}
	public void interactStop() {
		interacting = false;
	}
	public boolean isIntersecting() {
		return intersecting;
	}
	public void intersectNotify() {
		intersecting = true;
	}
	public void intersectSet(boolean s) {
		intersecting = s;
	}
	public void intersectStop() {
		intersecting = false;
	}
	
	
	public void setRotationalVelocity(double rVel) {
		this.rVel = rVel;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public void setStatic(boolean statis) {
		this.statis = statis;
	}
	
	
	public double getRotation() {
		return rotation;
	}
	public boolean isStaticObject() {
		return statis;
	}
	public double getSphereRadius() {
		return sphere_radius;
	}
	public double getxPos() {
		return xPos;
	}
	public double getyPos() {
		return yPos;
	}
	public double getxPos_Prev() {
		return xPos_prev;
	}
	public double getyPos_Prev() {
		return yPos_prev;
	}
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	public Double getMass() {
		return mass;
	}
	public HXWorld getWorld() {
		return parentWorld;
	}
	public Double getLifespan() {
		return lifespan;
	}
	public double getxVel() {
		return xVel;
	}
	public double getyVel() {
		return yVel;
	}
	public int getDraw_xPos() {
		return draw_xPos;
	}
	public int getDraw_yPos() {
		return draw_yPos;
	}
	public Image getImg() {
		return this.img;
	}
	public double getScale() {
		return scale;
	}
	public HXCollider getCollider() {
		return collisionBody;
	}
}
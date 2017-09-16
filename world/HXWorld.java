package world;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import world.entities.HXEntity;
import world.entities.Square;
import world.entities.Wall;
import world.entities.WorldBorder;
import world.helper.math.HXVector;
import world.helper.properties.HXCollider;
import world.helper.properties.HXInteractable;
 
public class HXWorld {
	
	private HXInteractable interactTarget = null;
	
	private int width;
	private int height;
	private double scale = 1;
	
	/* === Updates and drawing === */
	private final CopyOnWriteArrayList<HXEntity> entities = new CopyOnWriteArrayList<HXEntity>();
	private final CopyOnWriteArrayList<HXEntity> physicsBodies = new CopyOnWriteArrayList<HXEntity>();
	private final CopyOnWriteArrayList<HXCollider> colliders = new CopyOnWriteArrayList<HXCollider>();

	/**
	 * The HXWorld object owns all entities in a CopyOnWriteArrayList but is drawn in a HXWorldPanel.
	 * <p>
	 * @param parentPanel - The JPanel that draws the world.
	 */
	public HXWorld(int w, int h) {
		this.width = w;
		this.height = h;
		
		// Run anything at start of world...
		
		new WorldBorder(this);
		new Square(40, 60, 0, 0, this);
		new Square(120, 60, 0, 0, this);
		new Square(200, 60, 0, 0, this);
		
		new Wall(0, -20, w, 20, this);
		new Wall(-20, 0, 20, h, this);
		new Wall(0, h, w, 20, this);
		new Wall(w, 0, 20, h, this);
		
		// ...
	}
	
	/**
	 * Calls any targeted entities' interaction methods
	 * <p>
	 * Goes through all entities that deploy the HXInteractable interface
	 * and calls its interact method if its hitbox was intersected by the 
	 * interaction coordinates.
	 * @param x - The x coordinate of the interaction
	 * @param y - The y coordinate of the interactoin
	 */
	public void interactAt(int x, int y) {
		for (HXEntity e : entities) {
			if (e instanceof HXInteractable) {
				if (HXInteractable.isPointInPolygon(x, y, ((HXInteractable) e).getCorners())) {
					interactTarget = ((HXInteractable) e);
					interactTarget.interactNotify();
					break;
				}
			}
		}
	}
	
	public void interactMove(int x, int y) {
		if (interactTarget != null) {
			interactTarget.interactImpulse(x, y);
		}
	}
	
	public void interactStop() {
		if (interactTarget != null) {
			interactTarget.interactStop();
			interactTarget = null;
		}
	}
	
	public void entityAdd(HXEntity e) {
		entities.add(0, e);
	}
	public void entityRemove(HXEntity e) {
		entities.remove(e);
	}
	
	public void physicsBodyAdd(HXEntity e) {
		physicsBodies.add(0, e);
	}
	public void physicsBodyRemove(HXEntity e) {
		physicsBodies.remove(e);
	}
	
	public void colliderAdd(HXCollider e) {
		colliders.add(0, e);
	}
	public void colliderRemove(HXCollider e) {
		colliders.remove(e);
	}
	
	public void draw(Graphics g, float interpolation) {
		for (HXEntity e : entities) {
			e.draw(g, interpolation);
		}
	}
	public void updateTick(double dT) {
		boolean intersection[] = new boolean[colliders.size()];
		for (int c = 0; c < colliders.size(); c++) {
			for (int c2 = c + 1; c2 < colliders.size(); c2++) {
				HXVector mtv = colliders.get(c).doesOverlap(colliders.get(c2));
				if (mtv != null) {
					colliders.get(c).getParent().intersectApplyMTV(mtv);
					colliders.get(c2).getParent().intersectApplyMTV(mtv.opposite());
					intersection[c] = true;
					intersection[c2] = true;
				}
			}
			colliders.get(c).getParent().intersectSet(intersection[c]);
		}
		for (HXEntity e : entities) {
			e.update(dT);
		}
	}
	
	// Mark: Getters/Setters =======================================
	
	public CopyOnWriteArrayList<HXEntity> getPhysicsBodies() {
		return physicsBodies;
	}
	public CopyOnWriteArrayList<HXCollider> getRotatedColliders() {
		return colliders;
	}
	public void setScale(double scale) {
		this.scale = scale;
		for (HXEntity e : entities) {
			e.setScale(scale);
		}
	}
	public double getScale() {
		return scale;
	}
	public boolean isInteracting() {
		if (interactTarget != null) {
			return true;
		}
		return false;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
package world.entities;

import java.awt.Color;
import java.awt.Graphics;

import readers.HXKey;
import world.HXWorld;
import world.helper.properties.HXInteractable;

public class Wall extends HXEntity implements HXInteractable {

	/**
	 * Template for creating a new entiity. Should always override drawing, to have
	 * some sort of visual appearence in the world. But should still call super.draw()
	 * Constructor should always call init() method of HXEntity
	 * @param xPos
	 * @param yPos
	 * @param xVel
	 * @param yVel
	 * @param w
	 */
	public Wall(int xPos, int yPos, int width, int height, HXWorld w) {
		init(null, xPos, yPos, width, height, 0, 0, 1, -1, w.getScale(), true, w);
		
		this.setStatic(true);
	}
	
	/**
	 * Unless it has no visuals, it should override draw() but still call super.draw()
	 */
	@Override
	public void draw(Graphics g, float interpolation) {
		super.draw(g, interpolation);
		
		if (isInteracting()) {
			g.setColor(Color.green);
		} else {
			g.setColor(Color.black);
		}
		
		g.drawLine((int) this.getCollider().getVertices()[0].x, (int) this.getCollider().getVertices()[0].y, 
				(int) this.getCollider().getVertices()[1].x, (int) this.getCollider().getVertices()[1].y);
		g.drawLine((int) this.getCollider().getVertices()[1].x, (int) this.getCollider().getVertices()[1].y, 
				(int) this.getCollider().getVertices()[2].x, (int) this.getCollider().getVertices()[2].y);
		g.drawLine((int) this.getCollider().getVertices()[2].x, (int) this.getCollider().getVertices()[2].y, 
				(int) this.getCollider().getVertices()[3].x, (int) this.getCollider().getVertices()[3].y);
		g.drawLine((int) this.getCollider().getVertices()[3].x, (int) this.getCollider().getVertices()[3].y, 
				(int) this.getCollider().getVertices()[0].x, (int) this.getCollider().getVertices()[0].y);
		
//		if (isIntersecting()) {
//			g.setColor(Color.red);
//			g.drawLine((int) getCorner(1).x, (int) getCorner(1).y, (int) getCorner(3).x, (int) getCorner(3).y);
//			g.drawLine((int) getCorner(2).x, (int) getCorner(2).y, (int) getCorner(4).x, (int) getCorner(4).y);
//		}
	}

	@Override
	public void update(double dT) {
		super.update(dT);
		
		// Rotation with Q & W keys
		if (this instanceof HXInteractable) {
			if (((HXInteractable) this).isInteracting()) {
				if (HXKey.isPressed("q")) {
					this.setRotationalVelocity(4 / 180.0 * Math.PI);			
				} else if (HXKey.isPressed("w")){
					this.setRotationalVelocity(- 4 / 180.0 * Math.PI);
				} else {
					this.setRotationalVelocity(0);
				}
			}
		}
	}
	
	
	
}

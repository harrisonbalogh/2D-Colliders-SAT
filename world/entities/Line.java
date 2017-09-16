package world.entities;

import java.awt.Color;
import java.awt.Graphics;

import world.HXWorld;

public class Line extends HXEntity {
	
	private int xPosA = 0;
	private int yPosA = 0;
	private int xPosB = 0;
	private int yPosB = 0;
	
	private Color c;
	
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
	public Line(int xPosA, int yPosA, int xPosB, int yPosB, Color c, HXWorld w) {
		init(null, xPosA, yPosA, 0, 0, 0, 0, 1, -1, w.getScale(), false, w);
		
		this.xPosA = xPosA;
		this.yPosA = yPosA;
		this.xPosB = xPosB;
		this.yPosB = yPosB;
		
		this.c = c;
	}
	
	/**
	 * Unless it has no visuals, it should override draw() but still call super.draw()
	 */
	@Override
	public void draw(Graphics g, float interpolation) {
//		super.draw(g, interpolation);

		g.setColor(c);
		g.drawLine(
				(int) (xPosA * getScale()), 
				(int) (yPosA * getScale()), 
				(int) (xPosB * getScale()), 
				(int) (yPosB * getScale())
		);
		g.fillOval(
				(int) (xPosB * getScale() - 2), 
				(int) (yPosB * getScale() - 2),  4, 4);

	}

	@Override
	public void update(double dT) {
//		super.update();
	}
	
	public void set(int xA, int yA, int xB, int yB) {
		xPosA = xA;
		yPosA = yA;
		xPosB = xB;
		yPosB = yB;
	}
	
	public void set(double xA, double yA, double xB, double yB) {
		xPosA = (int) xA;
		yPosA = (int) yA;
		xPosB = (int) xB;
		yPosB = (int) yB;
	}
	
}

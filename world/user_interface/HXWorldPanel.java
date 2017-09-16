package world.user_interface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import world.HXWorld;
import world.helper.HXCamera;
import world.helper.HXClock;

public class HXWorldPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener{
	private static final long serialVersionUID = 1L;
	
	// IMPLEMENT
	private final boolean ANIMATED = false;
	
	/* === Drawing === */
	private float interpolation = 0;
	
	/* === 2D render canvas === */
	private Image environment;
	private Graphics graphics;
	
	/* === User Controls === */
	private HXCamera camera;
	private int mouse_x_last = 0;
	private int mouse_y_last = 0;
	
	/* === world === */
	private HXWorld world;
	
	/**
	 * Extension of a JPanel that contains an HXWorld and paints all of this object's entities.
	 * <p>
	 * @param worldWidth - Width in pixels of the world to be rendered.
	 * @param worldHeight - Height in pixels of the world to be rendered.
	 */
	public HXWorldPanel(int worldWidth, int worldHeight) {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		this.world = new HXWorld(worldWidth, worldHeight);
		this.camera = new HXCamera(this, HXClock.CLOCK_HERTZ);
		if (!ANIMATED) 
			new HXClock(this);
	}
	
	public void paint(Graphics g) {
		// - Render setup
		environment = createImage(getWidth(), getHeight());
		graphics = environment.getGraphics();
		paintComponent(graphics);
		g.drawImage(environment, 0, 0, null);
	}
	
	/**
	 * Called by repaintWorld(), which is called by the HXClock
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		Graphics2D g2d = (Graphics2D) g;
		// - Panel background
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		// Applies panning from camera
		g.translate(-camera.getCamera_x(), -camera.getCamera_y());
//		g2d.scale(camera.getZoom(), camera.getZoom());
		// Iterate backwards to draw in appropriate order
		world.draw(g, interpolation);
	}
	
	public void updateTick(double dT) {
		camera.updatePanning();
		world.updateTick(dT);
	}
	
	public void repaintWorld(float withInterpolation) {
		setInterpolation(withInterpolation);
		repaint();
	}
	
	public void setInterpolation(float interpolation) {
		this.interpolation = interpolation;
	}
	
	public HXWorld getWorld() {
		return world;
	}
	
	public void updateWorldEntitiesScale(double s) {
		world.setScale(s);
	}
	
	// MARK: Mouse Listeners =================================================================

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse_x_last = e.getX();
		mouse_y_last = e.getY();
		world.interactAt(
				e.getX() + camera.getCamera_x(), 
				e.getY() + camera.getCamera_y());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setCursor(Cursor.getDefaultCursor());
		world.interactStop();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		if (world.isInteracting()) {
			world.interactMove(
					e.getX() + camera.getCamera_x(), 
					e.getY() + camera.getCamera_y());
		} else {
			camera.dragPan(
					mouse_x_last - e.getX(), 
					e.getY() - mouse_y_last);
		}
		mouse_x_last = e.getX();
		mouse_y_last = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera.scrollZoom(e.getUnitsToScroll());	
	}

	@Override
	public void componentResized(ComponentEvent e) {
		camera.updateSize();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}

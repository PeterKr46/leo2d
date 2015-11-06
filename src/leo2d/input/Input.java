package leo2d.input;

import leo2d.core.Camera;
import leo2d.math.Vector;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;

public class Input implements MouseMotionListener, MouseInputListener, MouseWheelListener, KeyListener {
	
	private static Vector mousePosition = new Vector(0,0);
	private static boolean lMouse;
	
	private static HashMap<Character, Boolean> keysPressed = new HashMap<Character, Boolean>();
	
	public static Vector getMousePosition() {
		return mousePosition.clone();
	}
	
	public static boolean isLMouseDown() {
		return lMouse;
	}

	public static boolean isKeyDown(char c) {
		return keysPressed.containsKey(c) && keysPressed.get(c);
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		Camera c = Camera.main();
		double dx = event.getX() - mousePosition.x;
		double dy = event.getY() - (c.getScreenHeight() - mousePosition.y);
		mousePosition = new Vector(event.getX(), c.getScreenHeight() - event.getY());
		double px = (dx / c.getScreenWidth()) * c.getHorizontalSize();
		double py = (dy / c.getScreenHeight()) * c.getVerticalSize();
		if(c.debug()) {
			c.setPosition(c.getPosition().add(-px, py));
		}
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		if(mouseEvent.getButton() == 1) {
			lMouse = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		if(mouseEvent.getButton() == 1) {
			lMouse = false;
			//Debug.dragging = null;
		}
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == 113) { // 113 = F2
			Camera.main().toggleDebug();
		}
		keysPressed.put(keyEvent.getKeyChar(), true);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		keysPressed.put(keyEvent.getKeyChar(), false);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Camera c = Camera.main();
		if(c.debug()) {
			c.setVerticalSize(c.getVerticalSize() + e.getUnitsToScroll() * 0.05f);
		}
	}
}

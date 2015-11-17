package leo2d.input;

import leo2d.core.Camera;
import leo2d.editor.EditorController;
import leo2d.math.Vector;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;

public class Input extends EditorController implements MouseMotionListener, MouseInputListener, MouseWheelListener, KeyListener {

	public static enum KeyState {
		DOWN, HELD, RELEASED, CLICK
	}

	public Input() {
		super();
	}

	private static Vector lastMousePosition = new Vector(0,0);
	private static Vector mousePosition = new Vector(0,0);

	private static HashMap<Integer, KeyState> keysPressed = new HashMap<>();

	public static Vector getMousePosition() {
		return mousePosition.clone();
	}

	public static Vector getMouseDelta() {
		return Vector.difference(lastMousePosition, mousePosition).multiply(Camera.main().deltatime()).clone();
	}
	
	public static boolean getMouseButton(int button) {
		if(!keysPressed.containsKey(-button)) return false;
		KeyState state = keysPressed.get(-button);
		return state == KeyState.HELD || state == KeyState.DOWN;
	}

	public static boolean getMouseButtonDown(int button) {
		if(!keysPressed.containsKey(-button)) return false;
		KeyState state = keysPressed.get(-button);
		return state == KeyState.CLICK || state == KeyState.DOWN;
	}

	public static boolean getMouseButtonUp(int button) {
		return keysPressed.containsKey(-button) && keysPressed.get(-button) == KeyState.RELEASED;
	}

	public static boolean getKey(char c) {
		if(!keysPressed.containsKey((int)c)) return false;
		KeyState state = keysPressed.get((int)c);
		return state == KeyState.HELD || state == KeyState.DOWN;
	}

	public static boolean getKeyDown(char c) {
		if(!keysPressed.containsKey((int)c)) return false;
		KeyState state = keysPressed.get((int)c);
		return state == KeyState.CLICK || state == KeyState.DOWN;
	}

	public static boolean getKeyUp(char c) {
		return keysPressed.containsKey((int)c) && keysPressed.get((int)c) == KeyState.RELEASED;
	}

	public static KeyState getRawState(int id) {
		return keysPressed.get(id);
	}
	
	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		Camera c = Camera.main();
		mousePosition = new Vector(mouseEvent.getX(), c.getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		Camera c = Camera.main();
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		mousePosition = new Vector(mouseEvent.getX(), c.getScreenHeight() - mouseEvent.getY());
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		keysPressed.put(-mouseEvent.getButton(), KeyState.CLICK);
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		keysPressed.put(-mouseEvent.getButton(), KeyState.DOWN);
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
		keysPressed.put(-mouseEvent.getButton(), KeyState.RELEASED);
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
		keysPressed.put((int)keyEvent.getKeyChar(), KeyState.HELD);
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == 113) { // 113 = F2
			Camera.main().toggleDebug();
		}
		keysPressed.put((int)keyEvent.getKeyChar(), KeyState.DOWN);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		keysPressed.put((int)keyEvent.getKeyChar(), KeyState.RELEASED);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Camera c = Camera.main();
		if(c.debug()) {
			c.setVerticalSize(c.getVerticalSize() + e.getUnitsToScroll() * 0.05f);
		}
	}

	@Override
	public void update() {
		for(Integer id : keysPressed.keySet().toArray(new Integer[keysPressed.size()])) {
			KeyState prev = keysPressed.get(id);
			switch(prev) {
				case DOWN:
					keysPressed.put(id, KeyState.HELD);
					break;
				case RELEASED:
					keysPressed.remove(id);
					break;
				case CLICK:
					keysPressed.put(id, KeyState.RELEASED);
					break;
			}
		}
		lastMousePosition = mousePosition.clone();
	}
}

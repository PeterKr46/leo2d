package eu.saltyscout.leo2d.input;

import eu.saltyscout.leo2d.Camera;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import org.dyn4j.geometry.Vector2;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;

public class Input implements MouseMotionListener, MouseInputListener, MouseWheelListener, KeyListener {

    private static Vector2 lastMousePosition = new Vector2(0, 0);
    private static Vector2 mousePosition = new Vector2(0, 0);
    private static HashMap<Integer, KeyState> keysPressed = new HashMap<>();
    public Input() {
        super();
    }

    public static Vector2 getMousePosition() {
        return mousePosition.copy();
    }

    public static Vector2 getMouseDelta() {
        return mousePosition.difference(lastMousePosition).copy();
    }

    public static boolean getMouseButton(int button) {
        if (!keysPressed.containsKey(-button)) return false;
        KeyState state = keysPressed.get(-button);
        return state == KeyState.HELD || state == KeyState.DOWN;
    }

    public static boolean getMouseButtonDown(int button) {
        if (!keysPressed.containsKey(-button)) return false;
        KeyState state = keysPressed.get(-button);
        return state == KeyState.CLICK || state == KeyState.DOWN;
    }

    public static boolean getMouseButtonUp(int button) {
        return keysPressed.containsKey(-button) && keysPressed.get(-button) == KeyState.RELEASED;
    }

    public static boolean getKey(int c) {
        if (!keysPressed.containsKey((c))) return false;
        KeyState state = keysPressed.get(c);
        return state == KeyState.HELD || state == KeyState.DOWN;
    }

    public static boolean getKeyDown(int c) {
        if (!keysPressed.containsKey(c)) return false;
        KeyState state = keysPressed.get(c);
        return state == KeyState.CLICK || state == KeyState.DOWN;
    }

    public static boolean getKeyUp(int c) {
        return keysPressed.containsKey(c) && keysPressed.get(c) == KeyState.RELEASED;
    }

    public static KeyState getRawState(int id) {
        return keysPressed.get(id);
    }

    public static String getPressedKey() {
        String allowed = "0123456789 QWERTZUIOPÜASDFGHJKLÖÄYXCVBNM.,:-?!";
        String raw = "";
        boolean shiftMask = false;
        for (int id : keysPressed.keySet()) {
            if (id > 0 && keysPressed.get(id) == KeyState.DOWN) {
                raw = KeyEvent.getKeyText(id);
                if (!allowed.contains(raw.toUpperCase())) {
                    raw = "";
                }
                switch (id) {
                    case KeyEvent.VK_SPACE:
                        raw = " ";
                        break;
                    case KeyEvent.VK_MINUS:
                        raw = "-";
                        break;
                    case KeyEvent.VK_PERIOD:
                        raw = ".";
                        break;
                    case KeyEvent.VK_COMMA:
                        raw = ",";
                        break;
                    case KeyEvent.VK_SHIFT:
                        shiftMask = true;
                }
            }
        }
        if (shiftMask) {
            if (getKeyDown(KeyEvent.VK_9)) {
                raw = ")";
            } else if (getKeyDown(KeyEvent.VK_8)) {
                raw = "(";
            } else if (getKeyDown(KeyEvent.VK_PERIOD)) {
                raw = ":";
            } else if (getKeyDown(KeyEvent.VK_MINUS)) {
                raw = "_";
            }
        }
        return raw;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
        keysPressed.put(-mouseEvent.getButton(), KeyState.CLICK);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
        keysPressed.put(-mouseEvent.getButton(), KeyState.DOWN);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
        keysPressed.put(-mouseEvent.getButton(), KeyState.RELEASED);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mousePosition = new Vector2(mouseEvent.getX(), Leo2D.getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (!getKeyDown(keyEvent.getKeyCode())) {
            keysPressed.put(keyEvent.getKeyCode(), KeyState.HELD);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 113) { // 113 = F2
            Leo2D.setDebugEnabled(!Leo2D.isDebugEnabled());
        } else if (keyEvent.getKeyCode() == 114) { // 114 = F3
            Leo2D.setPaused(!Leo2D.isPaused());
        }
        keysPressed.put(keyEvent.getKeyCode(), KeyState.DOWN);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keysPressed.put(keyEvent.getKeyCode(), KeyState.RELEASED);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Camera camera = Scene.getMainCamera();
        if (camera != null && Leo2D.isDebugEnabled()) {
            camera.setVerticalSize(camera.getVerticalSize() + e.getUnitsToScroll() * 0.05f);
        }
    }

    public void update() {
        for (Integer id : keysPressed.keySet().toArray(new Integer[keysPressed.size()])) {
            KeyState prev = keysPressed.get(id);
            switch (prev) {
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
        lastMousePosition = mousePosition.copy();
    }

    public static enum KeyState {
        DOWN, HELD, RELEASED, CLICK
    }
}

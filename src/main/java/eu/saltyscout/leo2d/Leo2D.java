package eu.saltyscout.leo2d;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import eu.saltyscout.leo2d.controllers.EditorController;
import eu.saltyscout.leo2d.input.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Created by Peter on 26.10.2017.
 */
public class Leo2D implements GLEventListener {
    private static Leo2D instance;
    // The Window Components
    private final JFrame frame;
    private final GLCanvas glCanvas;
    private final Animator fpsAnimator;
    private final Input input;
    private GL2 gl;
    // Rendering space properties
    private int[] screenSize = new int[]{800, 480};
    private float aspectRatio = 1;
    // Time elapsed since last Frame
    private long lastFrame = 0;
    private float deltaTime = 1;
    // Controls ticking.
    private boolean paused = false;
    private boolean debugEnabled = false;

    private Leo2D() {
        // Initialize a Frame to hold the GLCanvas.
        frame = new JFrame("Leo2D");
        Image icon = Toolkit.getDefaultToolkit().getImage("assets/icon.png");
        frame.setIconImage(icon);
        frame.setSize(screenSize[0], screenSize[1]);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.flush();
                System.exit(0);
            }
        });

        // Initialize the GLCanvas.
        GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
        glCanvas = new GLCanvas(capabilities);
        glCanvas.addGLEventListener(this);
        fpsAnimator = new Animator(glCanvas); //new FPSAnimator(glCanvas, 120);
        fpsAnimator.start();

        // Initialize Input Controller
        input = new Input();
        glCanvas.addMouseMotionListener(input);
        glCanvas.addMouseListener(input);
        glCanvas.addKeyListener(input);
        glCanvas.addMouseWheelListener(input);

        // Add Canvas to frame.
        frame.add(glCanvas);
        frame.setVisible(true);
    }

    private static Leo2D getInstance() {
        if (instance == null) {
            instance = new Leo2D();
        }
        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static boolean isDebugEnabled() {
        return getInstance().debugEnabled;
    }

    public static void setDebugEnabled(boolean enabled) {
        getInstance().debugEnabled = enabled;
    }

    public static float getAspectRatio() {
        return getInstance().aspectRatio;
    }

    public static int getScreenWidth() {
        return getInstance().screenSize[0];
    }

    public static int getScreenHeight() {
        return getInstance().screenSize[1];
    }

    public static float deltaTime() {
        return getInstance().deltaTime;
    }

    public static GL2 getGL() {
        return getInstance().gl;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        // Only paint if not currently paused.
        if (!paused) {
            // Update deltaTime.
            if(lastFrame == 0) {
                lastFrame = System.currentTimeMillis() - 20;
            }
            deltaTime = System.currentTimeMillis() - lastFrame;
            deltaTime /= 1000f;
            lastFrame = System.currentTimeMillis();
        } else {
            deltaTime = 0;
            lastFrame = System.currentTimeMillis();
        }

        // Clear Canvas and repaint if possible.
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        List<Transform> transforms = Scene.getTransforms();

        // Call earlyUpdate() on all transforms
        for (Transform transform : transforms) {
            transform.earlyUpdateComponents();
        }

        // Paint scene if there is a camera available.
        Camera camera = Scene.getMainCamera();
        if (camera != null) {
            camera.display(glAutoDrawable);
        }

        // Call update() on all Components
        for (Transform transform : transforms) {
            try {
                transform.updateComponents(glAutoDrawable);
            } catch (Exception e) {
                System.out.println("Error in Transform.update():");
                e.printStackTrace();
            }
        }

        // Run Editor Controllers
        for (EditorController controller : EditorController.getControllers()) {
            try {
                controller.update();
            } catch (Exception e) {
                System.out.println("Error in " + controller.getClass().getSimpleName() + ".update():");
                e.printStackTrace();
            }
        }
        input.update();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        aspectRatio = ((float) width) / height;
        screenSize[0] = width;
        screenSize[1] = height;
    }

    public static void pause() {
        getInstance().paused = true;
    }

    public static boolean isPaused() {
        return getInstance().paused;
    }

    public static void setPaused(boolean paused) {
        getInstance().paused = paused;
    }
}

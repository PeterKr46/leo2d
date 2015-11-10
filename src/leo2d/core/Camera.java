package leo2d.core;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import leo2d.Transform;
import leo2d.gl.VoltImg;
import leo2d.input.Input;
import leo2d.math.Segment;
import leo2d.math.Vector;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Camera implements GLEventListener {

	private static Camera main;
	
	public static Camera main() {
		return main;
	}

	private float deltatime = 0;	
	private long lastFrame = 0;
	private boolean paused = false;
	public boolean debug = false;
	
	private Frame windowFrame;
	private FPSAnimator animator;

	private GLCanvas canvas;
	private GLProfile glProfile;
	private GLCapabilities glCapabilities;
	private GL2 gl;
		
	private VoltImg volty;

	private double aspectRatio = 1.3;
	private double verticalSize = 3;
	private int screenWidth, screenHeight;
	
	private Vector position = new Vector(0,0);
	
	public double[] backgroundColor = new double[] {0.15, 0.15, 0.15};
	public double[] debugBackgroundColor = new double[] {0.15, 0.15, 0.15};
	
	
	public Vector localize(Vector worldPos) {
		if(worldPos == null) {
			return null;
		}
		Vector diff = Vector.difference(position.clone(), worldPos);
		diff.setX(diff.x / (verticalSize * aspectRatio));
		diff.setY(diff.y / verticalSize);
		return diff;
	}

	public VoltImg getVolty() {
		return volty;
	}
	
	public GL2 getGL() {
		return gl;
	}

	public double deltatime() {
		return deltatime;
	}

	public Camera() {
		if(main == null) {
			main = this;
		}

		glProfile = GLProfile.getDefault();
		glCapabilities = new GLCapabilities(glProfile);
		canvas = new GLCanvas(glCapabilities);

		volty = new VoltImg();
		volty.bind(this);

		canvas.addGLEventListener(this);

		animator = new FPSAnimator(canvas, 60);
		animator.start();

		Image icon = Toolkit.getDefaultToolkit().getImage("assets/icon.png");
		windowFrame = new Frame("Leo2D");
		windowFrame.setSize(800, 480);
		screenWidth = 800;
		screenHeight = 480;
		
		
		Input input = new Input();
		canvas.addMouseMotionListener(input);
		canvas.addMouseListener(input);
		canvas.addKeyListener(input);
		canvas.addMouseWheelListener(input);
		
		windowFrame.add(canvas);
		windowFrame.setVisible(true);
		windowFrame.setIconImage(icon);
		windowFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.flush();
				System.exit(0);
			}
		});

		aspectRatio = ((float)canvas.getWidth()) / canvas.getHeight();	
	}
	
	public Vector getPosition() {
		return position.clone();
	}

	public void setPosition(Vector position) {
		this.position = position.clone();
	}

	public Vector getHalfsize() {
		return new Vector(verticalSize * aspectRatio, verticalSize);
	}

	public double getVerticalSize() {
		return verticalSize;
	}

	public void setVerticalSize(double verticalSize) {
		this.verticalSize = Math.max(0.3,verticalSize);
	}

	public double getHorizontalSize() {
		return verticalSize * aspectRatio;
	}

	public Vector getMin() {
		return getPosition().add(getHalfsize().multiply(-1, -1));
	}
	
	public Vector getMax() {
		return getPosition().add(getHalfsize());
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}
	
	public boolean debug() {
		return debug;	
	}
	
	public boolean toggleDebug() {
		debug = !debug;
		return debug;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		aspectRatio = ((float) width) / height;		
		screenWidth = width;
		screenHeight = height;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		if(!paused) {
			render(drawable);
		}
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {

	}
	
	private void render(GLAutoDrawable drawable) {
		deltatime = System.currentTimeMillis() - lastFrame;
		deltatime /= 1000f;
		lastFrame = System.currentTimeMillis();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		drawGrid(drawable);
		List<Transform> transforms = Transform.getAllTransforms();

		for(Transform transform : transforms) {
			transform.update(drawable);
		}
		for(Transform transform : transforms) {
			transform.updateBehaviours(drawable);
		}
	}
	
	private void drawGrid(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if(debug()) {
			gl.glColor3d(debugBackgroundColor[0], debugBackgroundColor[1], debugBackgroundColor[2]);
		} else {
			gl.glColor3d(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
		}
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(-1, -1);
		gl.glVertex2d(1, -1);
		gl.glVertex2d(1, 1);
		gl.glVertex2d(-1, 1);
		gl.glEnd();
		gl.glColor3d(1,1,1);
		if(!debug()) {
			return;
		}
		Vector min = getMin();
		Vector max = getMax();
		double color = 0.6;
		for(int i = (int)Math.round(min.y); i < max.y; i++) {
			double alpha = 0.2;
			if( ((int)i) % 10 == 0) {
				alpha = 0.6;
			}
			volty.line(new Vector(min.x, i), new Vector(max.x, i), new double[] {color, color, color, alpha});
		}
		for(int i = (int)Math.round(min.x); i < max.x; i++) {
			double alpha = 0.2;
			if( ((int)i) % 10 == 0) {
				alpha = 0.6;
			}
			volty.line(new Vector(i, min.y), new Vector(i, max.y), new double[] {color, color, color, alpha});
		}
		
	}
	
	public Segment[] getBounds() {
		Vector min = getMin();
		Vector max = getMax();
		return new Segment[] {
			new Segment(max, new Vector(0,getVerticalSize()*-2)),
			new Segment(new Vector(min.x, max.y), new Vector(getHorizontalSize()*2, 0)),
			new Segment(min, new Vector(0,getVerticalSize()*2)),
			new Segment(new Vector(max.x, min.y), new Vector(getHorizontalSize()*-2, 0)),
		};
	}
}

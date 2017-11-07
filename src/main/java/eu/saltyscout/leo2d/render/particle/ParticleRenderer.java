package eu.saltyscout.leo2d.render.particle;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.render.RenderPhase;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.sprite.Sprite;
import eu.saltyscout.leo2d.sprite.Texture;
import eu.saltyscout.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParticleRenderer implements Renderer {

    protected static final int
            POS_X = 0, POS_Y = 1,
            VEL_X = 2, VEL_Y = 3,
            ROT = 4, VEL_ROT = 5,
            SCALE = 6, VEL_SCALE = 7,
            C_R = 8, C_G = 9, C_B = 10, C_A = 11,
            LIFE = 12, LIVED = 13, REN_ID = 14;
    private static final int DATA_SIZE = 15;


    private final Transform transform;
    private boolean enabled = true;

    private boolean globalPositions = true;

    private int layer = 0;
    private int indexInLayer = 0;
    private Sprite sprite = null;
    private int maxParticles = 256;

    private Evaluable<Float> size, velRotDampening;
    private Evaluable<float[]> color, velocityDampening;

    // Location data is 9 floats per particle: x, y, vel.x, vel.y, rotation, vel.rotation, scale, vel.scale, alpha, lifeTime, lived, renderIndex
    private List<float[]> particleData = new ArrayList<>(maxParticles);
    private float spawnRate = 100f, sinceSpawned = 0;

    public ParticleRenderer(Transform transform) {
        this.transform = transform;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void earlyUpdate() {
        // Update all particles, remove the dead ones.
        List<float[]> remove = new ArrayList<>(maxParticles);
        for (float[] data : particleData) {
            // Check if still meant to live.
            data[LIVED] += Leo2D.deltaTime();
            if (data[LIVED] >= data[LIFE]) {
                remove.add(data);
            } else {
                modifyParticle(data);
            }
        }
        particleData.removeAll(remove);
        // Spawn new particles.
        sinceSpawned += Leo2D.deltaTime();
        while (sinceSpawned > 1 / spawnRate) {
            sinceSpawned -= 1 / spawnRate;
            spawnParticle();
        }
    }

    protected void modifyParticle(float[] particle) {
        // Calculate % of life lived.
        float pc = (particle[LIVED] / particle[LIFE]);

        // Change velocity
        particle[VEL_X] += (Math.random() * 0.3 - 0.15f) * Leo2D.deltaTime();
        particle[VEL_Y] += (Math.random() * 0.3 - 0.15f) * Leo2D.deltaTime();
        particle[VEL_ROT] += (Math.random() * 20 - 10f) * Leo2D.deltaTime();
        particle[VEL_SCALE] += (Math.random() * 0.1f - 0.05f) * Leo2D.deltaTime();

        // Apply velocity dampening
        float[] vel = velocityDampening.get(pc);
        particle[VEL_X] -= ((1 - vel[0]) * Leo2D.deltaTime()) * particle[VEL_X];
        particle[VEL_Y] -= ((1 - vel[1]) * Leo2D.deltaTime()) * particle[VEL_Y];

        // Apply velocity
        particle[POS_X] += particle[VEL_X] * Leo2D.deltaTime();
        particle[POS_Y] += particle[VEL_Y] * Leo2D.deltaTime();

        particle[ROT] += (particle[VEL_ROT]) * Leo2D.deltaTime();
        particle[SCALE] = size.get(pc);
        // Change color
        float[] color = this.color.get(pc);
        particle[C_R] = color[0];
        particle[C_G] = color[1];
        particle[C_B] = color[2];
        particle[C_A] = color[3];
    }

    protected void initializeParticle(float[] particle) {
        particle[POS_X] = globalPositions ? transform.getPosition().getX() : 0;
        particle[POS_Y] = globalPositions ? transform.getPosition().getY() : 0;
        particle[ROT] = (float) (Math.random() * 360);
        particle[SCALE] = 0.1f;
        particle[VEL_SCALE] = (float) ((Math.random() * 0.1f + 0.2f));
        particle[VEL_X] = (float) (Math.random() * 0.2 - 0.4f);
        particle[VEL_Y] = (float) (Math.random() * 0.1) + 0.9f;
        particle[C_R] = 0.1f; // (float) Math.random() * .2f + .8f;
        particle[C_G] = 0.1f; // (float) Math.random() * .2f + .8f;
        particle[C_B] = 0.1f; // (float) Math.random() * .2f + .8f;
        particle[C_A] = 0;
        particle[LIFE] = (float) (Math.random() * 2 + 3);
        particle[REN_ID] = indexInLayer;
    }

    private void spawnParticle() {
        if (particleData.size() < maxParticles) {
            float[] data = new float[DATA_SIZE];
            initializeParticle(data);
            particleData.add(data);
        }
    }

    @Override
    public void update() {
        // Nothing.
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int[] getPassIndices() {
        List<Float> passes = particleData.stream().map(data -> data[REN_ID]).distinct().collect(Collectors.toList());
        int[] passIndices = new int[passes.size()];
        for (int i = 0; i < passIndices.length; i++) {
            passIndices[i] = (int) (float) passes.get(i);
        }
        return passIndices;
    }

    @Override
    public RenderPhase getPhase() {
        return RenderPhase.DEFAULT;
    }

    @Override
    public Rect getAABB() {
        float xMin = transform.getPosition().getX(), yMin = transform.getPosition().getY(), xMax = xMin, yMax = yMin;
        for (float[] data : particleData) {
            xMin = Math.min(xMin, data[POS_X] - data[SCALE]);
            xMax = Math.max(xMax, data[POS_X] + data[SCALE]);
            yMin = Math.min(yMin, data[POS_Y] - data[SCALE]);
            yMax = Math.max(yMax, data[POS_Y] + data[SCALE]);
        }
        Rect r = new Rect(xMin, yMin, xMax - xMin, yMax - yMin);
        if (Leo2D.isDebugEnabled()) r.visualize();
        return r;
    }

    @Override
    public void paint(int pass) {
        VoltImg volty = Scene.getMainCamera().getVolty();
        if (sprite == null || sprite.getTexture() == null) {
            return;
        }
        // Load and enable texture in GL
        Texture tex = sprite.getTexture();
        volty.enable(3553);
        tex.loadGLTexture(Leo2D.getGL());
        tex.getTexture(volty.gl()).bind(volty.gl());

        Rect sRect = sprite.getRect();
        float tMin_x = Math.min(1f, sRect.getMinX() / tex.getWidth()),
                tMin_y = Math.min(1f, sRect.getMinY() / tex.getHeight()),
                tMax_x = Math.min(1f, sRect.getMaxX() / tex.getWidth()),
                tMax_y = Math.min(1f, sRect.getMaxY() / tex.getHeight());
        Vector position;
        Vector scale;
        float rotation;

        volty.enable(GL.GL_BLEND);
        volty.blendFunc(GL2.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        volty.begin(GL2.GL_QUADS);

        for (float[] data : particleData) {
            // Only draw what is needed for this render pass.
            if (((int) data[REN_ID]) == pass) {
                // Build usable data from particle array
                position = globalPositions ? Vector.of(data[POS_X], data[POS_Y]) : transform.getPosition().add(data[POS_X], data[POS_Y]);
                rotation = data[ROT];
                scale = Vector.of(data[SCALE], data[SCALE]);

                Vector right = Vector.of(scale.getX(), 0).rotate(rotation).mul(sprite.getWidth() / sprite.getPPU());
                Vector up = Vector.of(0, scale.getY()).rotate(rotation).mul(sprite.getHeight() / sprite.getPPU());

                Vector bl = Vector.copyOnWrite(position.add(sprite.getOffset().mulComponents(scale).rotate(rotation)));
                Vector br = bl.add(right);
                Vector tr = bl.add(right).add(up);
                Vector tl = bl.add(up);
                volty.gl().glColor4f(data[C_R], data[C_G], data[C_B], data[C_A]);

                volty.texCoord(tMin_x, tMax_y);
                volty.vertex(tl);

                volty.texCoord(tMin_x, tMin_y);
                volty.vertex(bl);

                volty.texCoord(tMax_x, tMin_y);
                volty.vertex(br);

                volty.texCoord(tMax_x, tMax_y);
                volty.vertex(tr);
            }
        }

        volty.end();
        volty.gl().glColor4f(1, 1, 1, 1);
        volty.disable(3553);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setSize(Evaluable<Float> size) {
        this.size = size;
    }

    public void setRotationalDampening(Evaluable<Float> velRotDampening) {
        this.velRotDampening = velRotDampening;
    }

    public void setVelocityDampening(Evaluable<float[]> velocityDampening) {
        this.velocityDampening = velocityDampening;
    }

    public void setColor(Evaluable<float[]> color) {
        this.color = color;
    }

    public int getIndexInLayer() {
        return indexInLayer;
    }

    public void setIndexInLayer(int indexInLayer) {
        this.indexInLayer = indexInLayer;
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }
}

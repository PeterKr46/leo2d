package eu.saltyscout.leo2d.component;

import eu.saltyscout.leo2d.GameObject;

/**
 * Components are Attachments for {@link GameObject} instances.<br/>
 * Every subtype T must offer a public constructor T(GameObject owner).
 */
public interface Component {
    /**
     * Indicates whether or not this Component is currently active.<br/>
     * Inactive Component will not have {@link Component#update()} called.
     *
     * @return true if this Component is currently active.
     */
    boolean isEnabled();

    /**
     * Sets the activity status of this Component. See {@link Component#isEnabled()}.
     */
    void setEnabled(boolean enabled);

    /**
     * This method is called <i>immediately before</i> the Scene is rendered.
     */
    void earlyUpdate();

    /**
     * This method is designed for real-time ticks and called every time a full frame has been rendered to the Canvas.
     */
    void update();

    /**
     * Each component is attached to one(!) GameObject which is given upon initialization.
     *
     * @return the GameObject (final) to which this Component belongs.
     */
    GameObject getGameObject();

    /**
     * Called by the GameObject upon destruction of this component.
     */
    void onDestroy();
}

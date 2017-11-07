package eu.saltyscout.leo2d.component;

import eu.saltyscout.leo2d.Transform;

/**
 * Components are Attachments for {@link Transform} instances.<br/>
 * Every subtype T must offer a public constructor T(Transform owner).
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
     * Each component is attached to one(!) Transform which is given upon initialization.
     *
     * @return the Transform (final) to which this Component belongs.
     */
    Transform getTransform();

}

package eu.saltyscout.leo2d.component;

import java.nio.ByteBuffer;

/**
 * Created by Peter on 03.11.2017.
 */
public interface SerializableComponent extends Component {

    /**
     * Serializes the Component as a collection of bytes stored within a ByteBuffer.<br/>
     * The first couple of bytes should be the fully class name in Little Endian UTF-8 alongside its length, also little endian.
     *
     * @return a ByteBuffer.
     */
    ByteBuffer serialize();

    /**
     * Deserializes the Component from a ByteBuffer.
     * See {@link #serialize()} for information on the format.
     *
     * @param data a ByteBuffer.
     */
    void deserialize(ByteBuffer data);

}

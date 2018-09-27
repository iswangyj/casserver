package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;

/**
 * @author SxL
 * Created on 9/25/2018 5:04 PM.
 */
public final class SimpleWebApplicationServiceSerializer extends Serializer<SimpleWebApplicationServiceImpl> {
    public SimpleWebApplicationServiceSerializer() {
    }

    @Override
    public void write(Kryo kryo, Output output, SimpleWebApplicationServiceImpl service) {
        kryo.writeObject(output, service.getId());
    }

    @Override
    public SimpleWebApplicationServiceImpl read(Kryo kryo, Input input, Class<SimpleWebApplicationServiceImpl> type) {
        return new SimpleWebApplicationServiceImpl((String)kryo.readObject(input, String.class));
    }
}

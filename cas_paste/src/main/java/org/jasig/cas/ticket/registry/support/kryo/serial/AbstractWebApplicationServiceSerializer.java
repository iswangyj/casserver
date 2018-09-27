package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jasig.cas.authentication.principal.AbstractWebApplicationService;
import org.jasig.cas.ticket.registry.support.kryo.FieldHelper;

/**
 * @author SxL
 * Created on 9/25/2018 5:02 PM.
 */
public abstract class AbstractWebApplicationServiceSerializer<T extends AbstractWebApplicationService> extends Serializer<T> {
    protected final FieldHelper fieldHelper;

    public AbstractWebApplicationServiceSerializer(FieldHelper helper) {
        this.fieldHelper = helper;
    }

    public void write(Kryo kryo, Output output, T service) {
        kryo.writeObject(output, service.getId());
        kryo.writeObject(output, this.fieldHelper.getFieldValue(service, "originalUrl"));
        kryo.writeObject(output, service.getArtifactId());
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        return this.createService(kryo, input, (String)kryo.readObject(input, String.class), (String)kryo.readObject(input, String.class), (String)kryo.readObject(input, String.class));
    }

    protected abstract T createService(Kryo var1, Input var2, String var3, String var4, String var5);
}


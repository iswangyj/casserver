package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jasig.cas.support.saml.authentication.principal.SamlService;
import org.jasig.cas.ticket.registry.support.kryo.FieldHelper;
import org.jasig.cas.util.HttpClient;
import org.jasig.cas.util.SimpleHttpClient;

import java.lang.reflect.Constructor;

/**
 * @author SxL
 * Created on 9/25/2018 5:04 PM.
 */
public final class SamlServiceSerializer extends AbstractWebApplicationServiceSerializer<SamlService> {
    private static final Constructor CONSTRUCTOR;

    public SamlServiceSerializer(FieldHelper helper) {
        super(helper);
    }

    public void write(Kryo kryo, Output output, SamlService service) {
        super.write(kryo, output, service);
        kryo.writeObject(output, service.getRequestID());
    }

    protected SamlService createService(Kryo kryo, Input input, String id, String originalUrl, String artifactId) {
        String requestId = (String)kryo.readObject(input, String.class);

        try {
            return (SamlService)CONSTRUCTOR.newInstance(id, originalUrl, artifactId, new SimpleHttpClient(), requestId);
        } catch (Exception var8) {
            throw new IllegalStateException("Error creating SamlService", var8);
        }
    }

    static {
        try {
            CONSTRUCTOR = SamlService.class.getDeclaredConstructor(String.class, String.class, String.class, HttpClient.class, String.class);
            CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException var1) {
            throw new IllegalStateException("Expected constructor signature not found.", var1);
        }
    }
}

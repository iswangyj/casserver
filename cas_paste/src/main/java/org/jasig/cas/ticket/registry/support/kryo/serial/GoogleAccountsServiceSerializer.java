package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jasig.cas.support.saml.authentication.principal.GoogleAccountsService;
import org.jasig.cas.ticket.registry.support.kryo.FieldHelper;

import java.lang.reflect.Constructor;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author SxL
 * Created on 9/25/2018 5:02 PM.
 */
public final class GoogleAccountsServiceSerializer extends AbstractWebApplicationServiceSerializer<GoogleAccountsService> {
    private static final Constructor CONSTRUCTOR;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String alternateUsername;

    public GoogleAccountsServiceSerializer(FieldHelper helper, PublicKey publicKey, PrivateKey privateKey, String alternateUsername) {
        super(helper);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.alternateUsername = alternateUsername;
    }

    public void write(Kryo kryo, Output output, GoogleAccountsService service) {
        super.write(kryo, output, service);
        kryo.writeObject(output, this.fieldHelper.getFieldValue(service, "requestId"));
        kryo.writeObject(output, this.fieldHelper.getFieldValue(service, "relayState"));
    }

    protected GoogleAccountsService createService(Kryo kryo, Input input, String id, String originalUrl, String artifactId) {
        String requestId = (String)kryo.readObject(input, String.class);
        String relayState = (String)kryo.readObject(input, String.class);

        try {
            return (GoogleAccountsService)CONSTRUCTOR.newInstance(id, originalUrl, artifactId, relayState, requestId, this.privateKey, this.publicKey, this.alternateUsername);
        } catch (Exception var9) {
            throw new IllegalStateException("Error creating SamlService", var9);
        }
    }

    static {
        try {
            CONSTRUCTOR = GoogleAccountsService.class.getDeclaredConstructor(String.class, String.class, String.class, String.class, String.class, PrivateKey.class, PublicKey.class, String.class);
            CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException var1) {
            throw new IllegalStateException("Expected constructor signature not found.", var1);
        }
    }
}

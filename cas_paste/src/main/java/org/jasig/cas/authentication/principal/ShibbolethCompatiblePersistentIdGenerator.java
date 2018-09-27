package org.jasig.cas.authentication.principal;

import org.apache.commons.codec.binary.Base64;

import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author SxL
 * Created on 9/25/2018 2:48 PM.
 */
public final class ShibbolethCompatiblePersistentIdGenerator implements PersistentIdGenerator {
    private static final byte CONST_SEPARATOR = 33;
    @NotNull
    private byte[] salt;

    public ShibbolethCompatiblePersistentIdGenerator() {
    }

    @Override
    public String generate(Principal principal, Service service) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(service.getId().getBytes());
            md.update((byte)33);
            md.update(principal.getId().getBytes());
            md.update((byte)33);
            return Base64.encodeBase64String(md.digest(this.salt)).replaceAll(System.getProperty("line.separator"), "");
        } catch (NoSuchAlgorithmException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void setSalt(String salt) {
        this.salt = salt.getBytes();
    }
}

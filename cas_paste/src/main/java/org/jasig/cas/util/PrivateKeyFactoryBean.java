package org.jasig.cas.util;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author SxL
 * Created on 9/25/2018 5:12 PM.
 */
public final class PrivateKeyFactoryBean extends AbstractFactoryBean {
    @NotNull
    private Resource location;
    @NotNull
    private String algorithm;

    public PrivateKeyFactoryBean() {
    }

    @Override
    protected Object createInstance() throws Exception {
        InputStream privKey = this.location.getInputStream();

        PrivateKey var5;
        try {
            byte[] bytes = new byte[privKey.available()];
            privKey.read(bytes);
            privKey.close();
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(this.algorithm);
            var5 = factory.generatePrivate(privSpec);
        } finally {
            privKey.close();
        }

        return var5;
    }

    @Override
    public Class getObjectType() {
        return PrivateKey.class;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}

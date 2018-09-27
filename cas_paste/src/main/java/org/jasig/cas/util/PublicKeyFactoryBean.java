package org.jasig.cas.util;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author SxL
 * Created on 9/25/2018 5:13 PM.
 */
public class PublicKeyFactoryBean extends AbstractFactoryBean {
    @NotNull
    private Resource resource;
    @NotNull
    private String algorithm;

    public PublicKeyFactoryBean() {
    }

    @Override
    protected final Object createInstance() throws Exception {
        InputStream pubKey = this.resource.getInputStream();

        PublicKey var5;
        try {
            byte[] bytes = new byte[pubKey.available()];
            pubKey.read(bytes);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(this.algorithm);
            var5 = factory.generatePublic(pubSpec);
        } finally {
            pubKey.close();
        }

        return var5;
    }

    @Override
    public Class getObjectType() {
        return PublicKey.class;
    }

    public void setLocation(Resource resource) {
        this.resource = resource;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}

package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author SxL
 * Created on 9/25/2018 5:04 PM.
 */
public final class URLSerializer extends Serializer<URL> {
    public URLSerializer() {
    }

    @Override
    public URL read(Kryo kryo, Input input, Class<URL> type) {
        String url = (String)kryo.readObject(input, String.class);

        try {
            return new URL(url);
        } catch (MalformedURLException var6) {
            throw new RuntimeException(var6);
        }
    }

    @Override
    public void write(Kryo kryo, Output output, URL url) {
        kryo.writeObject(output, url.toExternalForm());
    }
}


package org.jasig.cas.ticket.registry.support.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;
import org.apache.commons.io.IOUtils;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.ImmutableAuthentication;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.services.RegexRegisteredService;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.jasig.cas.ticket.ServiceTicketImpl;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.jasig.cas.ticket.registry.support.kryo.serial.RegisteredServiceSerializer;
import org.jasig.cas.ticket.registry.support.kryo.serial.SimpleWebApplicationServiceSerializer;
import org.jasig.cas.ticket.registry.support.kryo.serial.URLSerializer;
import org.jasig.cas.ticket.support.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 4:58 PM.
 */
public class KryoTranscoder implements Transcoder<Object> {
    private final Kryo kryo = new Kryo();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<Class<?>, Serializer> serializerMap;

    public KryoTranscoder() {
    }

    /** @deprecated */
    @Deprecated
    public KryoTranscoder(int initialBufferSize) {
        this.logger.warn("It's no longer necessary to define the initialBufferSize. Use the empty constructor.");
    }

    public void setSerializerMap(Map<Class<?>, Serializer> map) {
        this.serializerMap = map;
    }

    public void initialize() {
        this.kryo.register(ArrayList.class);
        this.kryo.register(BasicCredentialMetaData.class);
        this.kryo.register(Class.class, new DefaultSerializers.ClassSerializer());
        this.kryo.register(Date.class, new DefaultSerializers.DateSerializer());
        this.kryo.register(HardTimeoutExpirationPolicy.class);
        this.kryo.register(HashMap.class);
        this.kryo.register(HandlerResult.class);
        this.kryo.register(ImmutableAuthentication.class);
        this.kryo.register(MultiTimeUseOrTimeoutExpirationPolicy.class);
        this.kryo.register(NeverExpiresExpirationPolicy.class);
        this.kryo.register(RememberMeDelegatingExpirationPolicy.class);
        this.kryo.register(ServiceTicketImpl.class);
        this.kryo.register(SimpleWebApplicationServiceImpl.class, new SimpleWebApplicationServiceSerializer());
        this.kryo.register(ThrottledUseAndTimeoutExpirationPolicy.class);
        this.kryo.register(TicketGrantingTicketExpirationPolicy.class);
        this.kryo.register(TicketGrantingTicketImpl.class);
        this.kryo.register(TimeoutExpirationPolicy.class);
        this.kryo.register(URL.class, new URLSerializer());
        this.kryo.register(RegisteredServiceImpl.class, new RegisteredServiceSerializer());
        this.kryo.register(RegexRegisteredService.class, new RegisteredServiceSerializer());
        this.kryo.register(DateTime.class, new JodaDateTimeSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(this.kryo);
        if (this.serializerMap != null) {
            Iterator var1 = this.serializerMap.entrySet().iterator();

            while(var1.hasNext()) {
                Map.Entry<Class<?>, Serializer> clazz = (Map.Entry)var1.next();
                this.kryo.register((Class)clazz.getKey(), (Serializer)clazz.getValue());
            }
        }

        this.kryo.setAutoReset(false);
        this.kryo.setReferences(false);
        this.kryo.setRegistrationRequired(false);
    }

    @Override
    public boolean asyncDecode(CachedData d) {
        return false;
    }

    @Override
    public CachedData encode(Object obj) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Output output = new Output(byteStream);
        this.kryo.writeClassAndObject(output, obj);
        output.flush();
        IOUtils.closeQuietly(output);
        byte[] bytes = byteStream.toByteArray();
        return new CachedData(0, bytes, bytes.length);
    }

    @Override
    public Object decode(CachedData d) {
        byte[] bytes = d.getData();
        Input input = new Input(new ByteArrayInputStream(bytes));
        Object obj = this.kryo.readClassAndObject(input);
        IOUtils.closeQuietly(input);
        return obj;
    }

    @Override
    public int getMaxSize() {
        return 20971520;
    }

    public Kryo getKryo() {
        return this.kryo;
    }
}

package org.jasig.cas.ticket.registry.support.kryo.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jasig.cas.services.AbstractRegisteredService;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceImpl;

/**
 * @author SxL
 * Created on 9/25/2018 5:03 PM.
 */
public class RegisteredServiceSerializer extends Serializer<RegisteredService> {
    public RegisteredServiceSerializer() {
    }

    public void write(Kryo kryo, Output output, RegisteredService service) {
        kryo.writeObject(output, service.getServiceId());
    }

    public RegisteredService read(Kryo kryo, Input input, Class<RegisteredService> type) {
        String id = (String)kryo.readObject(input, String.class);
        AbstractRegisteredService svc = new RegisteredServiceImpl();
        svc.setServiceId(id);
        return svc;
    }
}

package org.jasig.cas;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author SxL
 * Created on 9/25/2018 1:43 PM.
 */
public class Message {
    private static final long serialVersionUID = 1227390629186486032L;
    private String code;
    private String defaultMessage;
    private Serializable[] params;

    public Message(String code) {
        this(code, code);
    }

    public Message(String code, String defaultMessage, Serializable... params) {
        Assert.hasText(code, "Code cannot be null or empty");
        Assert.hasText(defaultMessage, "Default message cannot be null or empty");
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.params = params;
    }

    public String getCode() {
        return this.code;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public Serializable[] getParams() {
        return this.params;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(99, 31);
        builder.append(this.code);
        builder.append(this.defaultMessage);
        builder.append(this.params);
        return builder.toHashCode();
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof Message) {
            if (other == this) {
                return true;
            } else {
                Message m = (Message)other;
                return this.code.equals(m.getCode()) && this.defaultMessage.equals(m.getDefaultMessage()) && Arrays.equals(this.params, m.getParams());
            }
        } else {
            return false;
        }
    }
}

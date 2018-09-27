package org.jasig.cas.authentication.handler;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author SxL
 * Created on 9/25/2018 2:13 PM.
 */
public final class DefaultPasswordEncoder implements PasswordEncoder {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    @NotNull
    private final String encodingAlgorithm;
    private String characterEncoding;

    public DefaultPasswordEncoder(String encodingAlgorithm) {
        this.encodingAlgorithm = encodingAlgorithm;
    }

    @Override
    public String encode(String password) {
        if (password == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(this.encodingAlgorithm);
                if (StringUtils.hasText(this.characterEncoding)) {
                    messageDigest.update(password.getBytes(this.characterEncoding));
                } else {
                    messageDigest.update(password.getBytes());
                }

                byte[] digest = messageDigest.digest();
                return this.getFormattedText(digest);
            } catch (NoSuchAlgorithmException var4) {
                throw new SecurityException(var4);
            } catch (UnsupportedEncodingException var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    private String getFormattedText(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);

        for(int j = 0; j < bytes.length; ++j) {
            buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);
            buf.append(HEX_DIGITS[bytes[j] & 15]);
        }

        return buf.toString();
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
}
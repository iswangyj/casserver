package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:14 PM.
 */
public class PrefixSuffixPrincipalNameTransformer implements PrincipalNameTransformer {
    private String prefix;
    private String suffix;

    public PrefixSuffixPrincipalNameTransformer() {
    }

    @Override
    public String transform(String formUserId) {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.prefix != null) {
            stringBuilder.append(this.prefix);
        }

        stringBuilder.append(formUserId);
        if (this.suffix != null) {
            stringBuilder.append(this.suffix);
        }

        return stringBuilder.toString();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 2:58 PM.
 */
public class CacheStatus extends Status {
    private final CacheStatistics[] statistics;

    public CacheStatus(StatusCode code, String description, CacheStatistics... statistics) {
        super(code, buildDescription(description, statistics));
        this.statistics = statistics;
    }

    public CacheStatus(Exception e) {
        super(StatusCode.ERROR, String.format("Error fetching cache status: %s::%s", e.getClass().getSimpleName(), e.getMessage()));
        this.statistics = null;
    }

    public CacheStatistics[] getStatistics() {
        return this.statistics;
    }

    private static String buildDescription(String desc, CacheStatistics... statistics) {
        if (statistics != null && statistics.length != 0) {
            StringBuilder sb = new StringBuilder();
            if (desc != null) {
                sb.append(desc);
                if (!desc.endsWith(".")) {
                    sb.append('.');
                }

                sb.append(' ');
            }

            sb.append("Cache statistics: [");
            int i = 0;
            CacheStatistics[] var4 = statistics;
            int var5 = statistics.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                CacheStatistics stats = var4[var6];
                if (i++ > 0) {
                    sb.append('|');
                }

                stats.toString(sb);
            }

            sb.append(']');
            return sb.toString();
        } else {
            return desc;
        }
    }
}

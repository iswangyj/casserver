package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:01 PM.
 */
public class PoolStatus extends Status {
    public static final int UNKNOWN_COUNT = -1;
    private final int idleCount;
    private final int activeCount;

    public PoolStatus(StatusCode code, String desc, int active, int idle) {
        super(code, buildDescription(desc, active, idle));
        this.activeCount = active;
        this.idleCount = idle;
    }

    public int getIdleCount() {
        return this.idleCount;
    }

    public int getActiveCount() {
        return this.activeCount;
    }

    private static String buildDescription(String desc, int active, int idle) {
        StringBuilder sb = new StringBuilder();
        if (desc != null) {
            sb.append(desc);
            if (!desc.endsWith(".")) {
                sb.append('.');
            }

            sb.append(' ');
        }

        if (active != -1) {
            sb.append(active).append(" active");
        }

        if (idle != -1) {
            sb.append(", ").append(idle).append(" idle.");
        }

        return sb.length() > 0 ? sb.toString() : null;
    }
}

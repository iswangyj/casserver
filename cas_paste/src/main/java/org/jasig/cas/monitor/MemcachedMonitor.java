package org.jasig.cas.monitor;

import net.spy.memcached.MemcachedClientIF;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 2:59 PM.
 */
public class MemcachedMonitor extends AbstractCacheMonitor {
    @NotNull
    private final MemcachedClientIF memcachedClient;

    public MemcachedMonitor(MemcachedClientIF client) {
        this.memcachedClient = client;
    }

    public CacheStatus observe() {
        if (this.memcachedClient.getAvailableServers().size() == 0) {
            return new CacheStatus(StatusCode.ERROR, "No memcached servers available.", new CacheStatistics[0]);
        } else {
            Collection<SocketAddress> unavailableList = this.memcachedClient.getUnavailableServers();
            CacheStatus status;
            if (unavailableList.size() > 0) {
                String description = "One or more memcached servers is unavailable: " + unavailableList;
                status = new CacheStatus(StatusCode.WARN, description, this.getStatistics());
            } else {
                status = super.observe();
            }

            return status;
        }
    }

    @Override
    protected CacheStatistics[] getStatistics() {
        Map<SocketAddress, Map<String, String>> allStats = this.memcachedClient.getStats();
        List<CacheStatistics> statsList = new ArrayList();
        Iterator var11 = allStats.keySet().iterator();

        while(var11.hasNext()) {
            SocketAddress address = (SocketAddress)var11.next();
            Map<String, String> statsMap = (Map)allStats.get(address);
            if (statsMap.size() > 0) {
                long size = Long.parseLong((String)statsMap.get("bytes"));
                long capacity = Long.parseLong((String)statsMap.get("limit_maxbytes"));
                long evictions = Long.parseLong((String)statsMap.get("evictions"));
                String name;
                if (address instanceof InetSocketAddress) {
                    name = ((InetSocketAddress)address).getHostName();
                } else {
                    name = address.toString();
                }

                statsList.add(new SimpleCacheStatistics(size, capacity, evictions, name));
            }
        }

        return (CacheStatistics[])statsList.toArray(new CacheStatistics[statsList.size()]);
    }
}

package com.alibaba.csp.sentinel.dashboard.discovery;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author fly
 */
@Slf4j
@Component
public class InRedisMachineDiscovery implements MachineDiscovery{

    private static final String SENTINEL_DISCOVERY_MACHINE = "sentinel:discovery_machine";
    @Autowired
    private RedissonClient redisson;

    @Override
    public List<String> getAppNames() {
        return new ArrayList<>(getMap().keySet());
    }

    @Override
    public Set<AppInfo> getBriefApps() {
        return new HashSet<>(getMap().values());
    }

    @Override
    public AppInfo getDetailApp(String app) {
        return getMap().get(app);
    }

    @Override
    public void removeApp(String app) {
        getMap().remove(app);
    }

    @Override
    public long addMachine(MachineInfo machineInfo) {
        RMap<String, AppInfo> rmap = getMap();

        AppInfo appInfo = rmap.get(machineInfo.getApp());
        if (appInfo == null) {
            appInfo = new AppInfo(machineInfo.getApp(), machineInfo.getAppType());
        }
        appInfo.addMachine(machineInfo);
        rmap.put(machineInfo.getApp(), appInfo);
        return 1;
    }

    @Override
    public boolean removeMachine(String app, String ip, int port) {
        RMap<String, AppInfo> rmap = getMap();

        AppInfo appInfo = rmap.get(app);
        if (appInfo != null) {
            boolean result = appInfo.removeMachine(ip, port);
            rmap.put(app, appInfo);
            return result;
        }
        return false;
    }

    private RMap<String, AppInfo> getMap() {
        return redisson.getMap(SENTINEL_DISCOVERY_MACHINE);
    }
}

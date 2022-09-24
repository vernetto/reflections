package org.pierre.reflection;

import java.util.ArrayList;
import java.util.List;

public class ServiceUsage {
    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public List getUsedServices() {
        return usedServices;
    }

    public void setUsedServices(List usedServices) {
        this.usedServices = usedServices;
    }

    private Class service;
    private List usedServices = new ArrayList();
    public ServiceUsage(Class service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "ServiceUsage{" +
                "service=" + service +
                ", usedServices=" + usedServices +
                '}';
    }
}

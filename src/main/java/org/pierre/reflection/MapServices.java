package org.pierre.reflection;

import org.pierre.reflection.services.Service;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MapServices {
    public static void main(String[] args) throws IOException {
        Reflections reflections = new Reflections("org.pierre.reflection.services");
        Set<Class<? extends Service>> services = reflections.getSubTypesOf(Service.class);
        System.out.println("all subtypes (interfaces and implementations) =" + services);

        List<Class<? extends Service>> allServiceImpl = services.stream().filter(aClass -> !aClass.isInterface()).collect(Collectors.toList());
        allServiceImpl.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        System.out.println("service implementations = " + allServiceImpl);
        List<ServiceUsage> serviceUsages = new ArrayList<>();
        for (Class<? extends Service> serviceImpl : allServiceImpl) {
            ServiceUsage serviceUsage = new ServiceUsage(serviceImpl);
            serviceUsages.add(serviceUsage);
            Field[] allFields = serviceImpl.getDeclaredFields();
            for (Field field : allFields) {
                if (isService(field)) {
                    System.out.println("service " + serviceImpl + " uses a member of interface : " + field.getType());
                    serviceUsage.getUsedServices().add(field.getType());
                }
            }
        }
        serviceUsages.forEach(serviceUsage -> serviceUsage.setService(getServiceInterface(serviceUsage.getService())));
        System.out.println("serviceUsages = " + serviceUsages);
        renderAsHTML(serviceUsages);
    }

    private static void renderAsHTML(List<ServiceUsage> serviceUsages) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append("<body>");
        for (ServiceUsage serviceUsage : serviceUsages) {
            String service = serviceUsage.getService().getName();
            sb.append("<h2 id=\"").append(service).append("\">").append(service).append(" usage:").append("</br>");
            sb.append("<ul>");
            for (Class usedService : serviceUsage.getUsedServices()) {
                sb.append("<li>");
                sb.append("<a href=\"#").append(usedService.getName()).append("\">").append(usedService.getName()).append("</a></br>");
                sb.append("</li>");
            }
            sb.append("</ul>");
            sb.append("<p/>");
        }
        Path path = Paths.get("serviceReport.html");
        Files.write(path, sb.toString().getBytes());
    }

    // return only classes which implement a Service interface
    private static boolean isService(Field field) {

        return Arrays.stream(field.getType().getInterfaces()).filter(aClass -> aClass == Service.class).count() > 0;
    }


    private static Class getServiceInterface(Class serviceImplementationClass) {
        return Arrays.stream(serviceImplementationClass.getInterfaces()).filter(anInterface -> Service.class.isAssignableFrom(anInterface)).findFirst().get();
    }


}

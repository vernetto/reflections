package org.pierre.reflection;

import org.pierre.reflection.services.Service;
import org.reflections.Reflections;

import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class MapServices {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("org.pierre.reflection.services");
        Set<Class<? extends Service>> services = reflections.getSubTypesOf(Service.class);
        System.out.println("all subtypes (interfaces and implementations) =" + services);
        services.stream().filter(aClass -> !aClass.isInterface()).forEach(aClass -> System.out.println("Service implementation " + aClass));

    }
}

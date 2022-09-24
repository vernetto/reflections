package org.pierre.reflection;

import org.pierre.reflection.services.Service;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MapServices {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("org.pierre.reflection.services");
        Set<Class<? extends Service>> services = reflections.getSubTypesOf(Service.class);
        System.out.println("all subtypes (interfaces and implementations) =" + services);

        List<Class<? extends Service>> allServiceImpl = services.stream().filter(aClass -> !aClass.isInterface()).collect(Collectors.toList());
        allServiceImpl.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        System.out.println("service implementations = " + allServiceImpl);
        for (Class<? extends Service> serviceImpl : allServiceImpl) {
            Field[] allFields = serviceImpl.getDeclaredFields();
            for (Field field : allFields) {
                if (isService(field)) {
                    System.out.println("service " + serviceImpl + " uses a member of interface : " + field.getType());
                }
            }
        }
    }

    private static boolean isService(Field field) {
        return Arrays.stream(field.getType().getInterfaces()).filter(aClass -> aClass == Service.class).count() > 0;
    }


}

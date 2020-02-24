/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import static java.lang.System.out;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author Wei.Cheng
 */
public class ObjectFieldUtils {

    public static Object runGetter(Field field, Object o) {
        // MZ: Find the correct method
        for (Method method : getMethods(o.getClass())) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        out.println("Could not determine method: " + method.getName());
                    }

                }
            }
        }

        return null;
    }

    private static Stack<Method> getMethods(Class type) {

        // MZ: Optionally, for performance reasons, cache the (non segmented) methods per type in a static map
        // MZ: this is just an example, and isn't threadsafe
        //if (classMethodCache.containsKey(type))
        //{
        //      return classMethodCache.get(type);
        //}
        Stack<Method> result = new Stack<>();
        try {
            for (Class<?> c = type; c != null; c = c.getSuperclass()) {
                Method[] methods = c.getDeclaredMethods();
                result.addAll(Arrays.asList(methods));
            }
        } catch (SecurityException e) {
            // MZ: Add your own logger instance here
            // Logger.error("Could not fetch object methods", e);
        }

        // MZ: Add to caching map
        // classMethodCache.put(type, result);
        return result;
    }
}

package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveResolver {


    public static String getHTTPClass(String cst) throws ClassNotFoundException {

        String output = "";
        Class c = Class.forName(cst);
        for(Field f : c.getFields()){
            output += "        <p> Field: "+ f.getName() + " </p> \n";
        }

        for(Method m : c.getMethods()){
            output += "        <p> Method: "+ m.getName() +" </p> \n";
        }

        return output;
    }

    public static String getHTTPInvoke(String cst, String mth) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(cst);
        Object res = null;
        for (Method m : c.getMethods()){
            if (m.getName().equals(mth)){
                res = m.invoke(null);
            }
        }
        String output = "<p> \n";
        output+=res.toString();
        output += "</p> \n";

        return output;
    }

    public static String getHTTPUnaryInvoke(String cst, String mth, String arg) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(cst);
        Object res = null;
        for (Method m : c.getMethods()){
            if (m.getName().equals(mth)){
                res = m.invoke(null, arg);
            }
        }
        String output = "<p> \n";
        output+= res.toString();
        output += "</p> \n";

        return output;
    }

    public static String getHTTPBinaryInvoke(String cst, String mth, String arg1, String arg2) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(cst);
        Object res = null;
        for (Method m : c.getMethods()){
            if (m.getName().equals(mth)){
                
                res = m.invoke(null, arg1, arg2);
            }
        }
        String output = "<p> \n";
        output+= res.toString();
        output += "</p> \n";

        return output;
    }
}

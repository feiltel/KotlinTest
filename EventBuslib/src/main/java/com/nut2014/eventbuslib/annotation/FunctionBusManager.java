package com.nut2014.eventbuslib.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionBusManager {
    private static volatile FunctionBusManager instance;
    private Map<Object, List<Method>> listMap;


    private FunctionBusManager() {
        listMap = new HashMap<>();
    }


    public static FunctionBusManager getDefault() {
        if (instance == null) {
            synchronized (FunctionBusManager.class) {
                if (instance == null) {
                    instance = new FunctionBusManager();
                }
            }
        }
        return instance;
    }


    //注册
    public void registerObserver(Object object) {
        listMap.remove(object);
        assert object != null;
        List<Method> beanList = finAllAnnotationList(object);
        if (beanList != null) {
            listMap.put(object, beanList);
        }
    }

    public <P> Object invoke(String className, String methodName, P param) {
        for (Object obj : listMap.keySet()) {
            if (obj.getClass().getName().equals(className)) {
                List<Method> beanList = listMap.get(obj);
                if (beanList != null && beanList.size() > 0) {
                    for (Method method : beanList) {
                        if (method.getName().equals(methodName)) {
                            try {
                                if (param == null) {
                                    return method.invoke(obj);
                                }
                                return method.invoke(obj, param);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * //InvokeObj类的无参show()方法
     * System.out.println("InvokeObj类的无参show()方法:");
     * //getMethod方法中参数，第一个是方法名，第二个是方法名中的参数类型
     * Method method1=clazz.getMethod("show", null);
     * //会执行的无参show()方法
     * Object object1=method1.invoke(new InvokeObj(), null);
     * System.out.println("输出无参show()方法的返回值："+object1);
     * <p>
     * //会执行的InvokeObj类的show()方法
     * System.out.println("InvokeObj类的show()方法：");
     * Method method2=clazz.getMethod("show", String.class);
     * //对InvokeObj类中的带参的show方法进行传值，值为Hello World
     * Object object2=method2.invoke(new InvokeObj(), "Hello World");
     * System.out.println("输出有参数的show()方法："+object2);
     * <p>
     * //InvokeObj类的arrayShow()方法：
     * System.out.println("InvokeObj类的arrayShow()方法：");
     * Method method3=clazz.getMethod("arrayShow", String[].class);
     * String[] str1=new String[]{"Hello","World","!"};
     * //数组类型的参数必须包含在new Object[]{}中，否则就会报IllegalAccessException异常
     * String[] str2=(String[]) method3.invoke(new InvokeObj(),new Object[]{str1});
     * for(String str : str2){
     * System.out.println("arrayShow方法中的数组元素："+str);
     * }
     * <p>
     * //InvokeObj类的StringShow()方法：
     * System.out.println("InvokeObj类的StringShow()方法：");
     * Method method4=clazz.getMethod("StringShow", String.class);
     * String str3=(String) method4.invoke(new InvokeObj(), "thinking in java");
     * System.out.println("StringShow()方法的返回值："+str3);
     * <p>
     * //InvokeObj类的intShow()方法：
     * System.out.println("InvokeObj类的intShow()方法：");
     * Method method5=clazz.getMethod("intShow", int.class);
     * int num=(Integer) method5.invoke(new InvokeObj(), 89);
     * System.out.println("intShow()方法的返回值："+num);
     */
    //总运费

    private List<Method> finAllAnnotationList(Object object) {
        List<Method> beanList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            FunctionBus functionBus = method.getAnnotation(FunctionBus.class);
            if (functionBus == null) {//本方法没有添加 FunctionBus 注解 跳出本次循环
                continue;
            }
            //获取方法参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 1) {
                //过滤参数大于1
                continue;
            }
            beanList.add(method);
        }
        return beanList;
    }

    //解除注册
    public void unRegisterObserver(Object object) {
        assert listMap!=null;
        listMap.remove(object);
    }
}

package com.nut2014.eventbuslib;


import java.util.HashMap;
import java.util.Map;

public class FunctionManager {

    //合同编号 承运编号 座机 现付 回付油卡 借款 承 定位 封签
    private static FunctionManager instance;

    private Map<String, FunctionNoParamNoResult> noParamNoResultMap;
    private Map<String, FunctionNoParamHasResult> noParamHasResultMap;
    private Map<String, FunctionHasParamNoResult> hasParamNoResultMap;
    private Map<String, FunctionHasParamHasResult> hasParamHasResultMap;

    public static FunctionManager getInstance() {
        if (instance == null) {
            instance = new FunctionManager();
        }
        return instance;
    }

    private FunctionManager() {
        noParamNoResultMap = new HashMap<>();
        noParamHasResultMap = new HashMap<>();
        hasParamNoResultMap = new HashMap<>();
        hasParamHasResultMap = new HashMap<>();
    }

    public void addFunction(FunctionNoParamNoResult function) {
        noParamNoResultMap.remove(function.functionName);
        noParamNoResultMap.put(function.functionName, function);
    }

    //执行方法
    public void invokeFunction(String functionName) {

        if (functionName.length() < 1) {
            return;
        }
        if (noParamNoResultMap != null) {
            System.out.println("方法个数：" + noParamNoResultMap.size());
            FunctionNoParamNoResult f = noParamNoResultMap.get(functionName);
            if (f != null) {
                f.function();
            } else {
                System.out.println("未找到方法" + functionName);
            }
        }
    }


    public void addFunction(FunctionNoParamHasResult function) {
        noParamHasResultMap.put(function.functionName, function);
    }

    //执行方法
    public <T> T invokeFunction(String functionName, Class<T> t) {
        if (functionName.length() < 1) {
            return null;
        }
        if (noParamHasResultMap != null) {
            FunctionNoParamHasResult f = noParamHasResultMap.get(functionName);
            if (f != null) {
                if (t != null) {
                    return t.cast(f.function());
                }

            } else {
                System.out.println("未找到方法" + functionName);
            }
        }
        return null;
    }

    public void addFunction(FunctionHasParamNoResult function) {
        hasParamNoResultMap.put(function.functionName, function);
    }

    //执行方法
    public <P> void invokeFunction(String functionName, P p) {
        if (functionName.length() < 1) {
            return;
        }
        if (hasParamNoResultMap != null) {
            FunctionHasParamNoResult f = hasParamNoResultMap.get(functionName);
            if (f != null) {
                f.function(p);
            } else {
                System.out.println("未找到方法" + functionName);
            }
        }
    }

    public void addFunction(FunctionHasParamHasResult function) {
        hasParamHasResultMap.put(function.functionName, function);
    }

    //执行方法
    public <T, P> T invokeFunction(String functionName, Class<T> t, P p) {
        if (functionName.length() < 1) {
            return null;
        }
        if (hasParamHasResultMap != null) {
            FunctionHasParamHasResult f = hasParamHasResultMap.get(functionName);
            if (f != null) {
                if (t != null) {
                    return t.cast(f.function(p));
                }

            } else {
                System.out.println("未找到方法" + functionName);
            }
        }
        return null;
    }

    public void removeAll() {
        noParamNoResultMap.clear();
        noParamHasResultMap.clear();
        hasParamNoResultMap.clear();
        hasParamHasResultMap.clear();
    }
}

package com.nut2014.eventbuslib;

public abstract class FunctionNoParamNoResult extends Function {
    public FunctionNoParamNoResult(String functionName) {
        super(functionName);
    }
    public abstract void function();
}

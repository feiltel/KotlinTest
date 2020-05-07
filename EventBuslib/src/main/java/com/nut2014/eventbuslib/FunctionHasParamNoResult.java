package com.nut2014.eventbuslib;

public abstract class FunctionHasParamNoResult<P> extends Function {
    public FunctionHasParamNoResult(String functionName) {
        super(functionName);
    }
    public abstract void function(P p);
}

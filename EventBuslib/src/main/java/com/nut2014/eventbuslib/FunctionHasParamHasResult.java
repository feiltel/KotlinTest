package com.nut2014.eventbuslib;

public abstract class FunctionHasParamHasResult<T,P> extends Function {
    public FunctionHasParamHasResult(String functionName) {
        super(functionName);
    }
    public abstract T function(P p);
}

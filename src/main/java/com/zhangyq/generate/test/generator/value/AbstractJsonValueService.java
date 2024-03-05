package com.zhangyq.generate.test.generator.value;

import com.zhangyq.generate.test.common.ValueContext;

public abstract class AbstractJsonValueService<T> implements JsonValueService<T>  {
    protected ValueContext valueContext;

    public ValueContext getValueContext() {
        return valueContext;
    }

    public void setValueContext(ValueContext valueContext) {
        this.valueContext = valueContext;
    }
}

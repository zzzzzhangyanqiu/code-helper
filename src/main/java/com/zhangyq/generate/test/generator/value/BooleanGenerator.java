package com.zhangyq.generate.test.generator.value;


public class BooleanGenerator extends AbstractJsonValueService<Boolean> {

    @Override
    public Boolean defaultValue() {
        return true;
    }

    @Override
    public Boolean randomValue() {
        return false;
    }
}

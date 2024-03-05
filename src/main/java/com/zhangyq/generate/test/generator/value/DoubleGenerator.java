package com.zhangyq.generate.test.generator.value;


import com.zhangyq.generate.test.common.ValueContext;

public class DoubleGenerator extends AbstractJsonValueService<Double> {

    @Override
    public Double defaultValue() {
        return 1.0;
    }

    @Override
    public Double randomValue() {
        return (double) ValueContext.getFaker().number().randomNumber(3, false);
    }
}

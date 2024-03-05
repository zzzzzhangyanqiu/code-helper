package com.zhangyq.generate.test.generator.value;

import com.zhangyq.generate.test.common.ValueContext;

public class FloatGenerator extends AbstractJsonValueService<Float> {

    @Override
    public Float defaultValue() {
        return 1.0f;
    }

    @Override
    public Float randomValue() {
        return (float) ValueContext.getFaker().number().randomNumber(3, false);
    }
}

package com.zhangyq.generate.test.generator.value;


import com.zhangyq.generate.test.common.ValueContext;

public class LongGenerator extends AbstractJsonValueService<Long> {

    @Override
    public Long defaultValue() {
        return 1L;
    }

    @Override
    public Long randomValue() {
        return ValueContext.getFaker().number().randomNumber();
    }
}

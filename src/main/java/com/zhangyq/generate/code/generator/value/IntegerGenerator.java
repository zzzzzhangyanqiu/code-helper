package com.zhangyq.generate.code.generator.value;


import com.zhangyq.generate.code.common.ValueContext;

public class IntegerGenerator extends AbstractJsonValueService<Integer> {

    @Override
    public Integer defaultValue() {
        return 1;
    }

    @Override
    public Integer randomValue() {
        return ValueContext.getFaker().number().randomDigit();
    }
}

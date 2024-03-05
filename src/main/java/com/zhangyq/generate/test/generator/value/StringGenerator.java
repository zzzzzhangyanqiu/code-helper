package com.zhangyq.generate.test.generator.value;


import com.zhangyq.generate.test.common.ValueContext;

public class StringGenerator extends AbstractJsonValueService<String> {

    @Override
    public String defaultValue() {
        return "test";
    }

    @Override
    public String randomValue() {
        return ValueContext.getFaker().name().username();
    }
}

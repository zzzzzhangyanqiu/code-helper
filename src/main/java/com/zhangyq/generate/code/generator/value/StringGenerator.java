package com.zhangyq.generate.code.generator.value;


import com.zhangyq.generate.code.common.ValueContext;

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

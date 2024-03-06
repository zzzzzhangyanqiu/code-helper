package com.zhangyq.generate.code.generator.value;


import com.zhangyq.generate.code.common.ValueContext;
import java.util.Date;

public class DateGenerator extends AbstractJsonValueService<Date> {

    @Override
    public Date defaultValue() {
        return new Date();
    }

    @Override
    public Date randomValue() {
        return ValueContext.getFaker().date().birthday();
    }
}

package com.zhangyq.generate.code.generator.value;



public class ByteGenerator extends AbstractJsonValueService<Byte> {

    @Override
    public Byte defaultValue() {
        return 1;
    }

    @Override
    public Byte randomValue() {
        return 0;
    }
}

package com.zhangyq.generate.code.generator.value;

public interface JsonValueService<T> {
    T defaultValue();
    T randomValue();
}

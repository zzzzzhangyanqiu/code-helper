package com.zhangyq.generate.test.generator.value;

public interface JsonValueService<T> {
    T defaultValue();
    T randomValue();
}

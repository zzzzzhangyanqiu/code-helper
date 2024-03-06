package com.zhangyq.generate.code.generator.value;


import com.zhangyq.generate.code.common.ValueContext;

public class CharacterGenerator extends AbstractJsonValueService<Character> {

    @Override
    public Character defaultValue() {
        return 'a';
    }

    @Override
    public Character randomValue() {
        return ValueContext.getFaker().lorem().character();
    }
}

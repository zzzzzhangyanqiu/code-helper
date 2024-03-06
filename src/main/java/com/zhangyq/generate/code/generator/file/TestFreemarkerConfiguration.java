package com.zhangyq.generate.code.generator.file;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

public class TestFreemarkerConfiguration extends Configuration {

    public TestFreemarkerConfiguration() {
        this("/test");
    }

    private TestFreemarkerConfiguration(String basePackagePath) {
        super(Configuration.VERSION_2_3_32);
        setDefaultEncoding("UTF-8");
        setClassForTemplateLoading(getClass(), basePackagePath);
    }

    public Template getTemplate(String ftl) throws IOException {
        return this.getTemplate(ftl, "UTF-8");
    }

}

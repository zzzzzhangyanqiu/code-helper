package com.zhangyq.generate.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author 有尘
 * @date 2021/9/28
 */
public class FileUtil {
    public static String getUnitFilePath(PsiFile psiFile) {
        String classPath = psiFile.getParent().getVirtualFile().getPath();
        return classPath.replace("/src/main/java", "/src/test/java");
    }

    public static String genJavaFileName(PsiClass psiClass) {
        String name = psiClass.getName();
        return name + "Test.java";
    }

    public static String getJsonFilePath(PsiFile psiFile) {

        String classPath = psiFile.getVirtualFile().getPath();
        classPath = classPath.replace("/src/main/java", "/src/test/java");
        classPath = classPath.replace(".java", "");
        return classPath.replace("java", "resources");
    }

    public static String getJsonFileName(String name) {
        return name + ".json";
    }

    public static String generateString(String templateName, Map<String, Object> dataModel, Configuration configuration) {
        // 获取模板文件
        try(Writer out = new StringWriter()) {
            Template template = configuration.getTemplate(templateName);

            template.process(dataModel, out);

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }
}

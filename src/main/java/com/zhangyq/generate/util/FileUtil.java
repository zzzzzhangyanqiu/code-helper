package com.zhangyq.generate.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;


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

    public static String getRelativePath(Project project, PsiFile psiFile) {
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (baseDir != null && virtualFile != null) {
            return com.intellij.openapi.util.io.FileUtil.getRelativePath(baseDir.getPath(), virtualFile.getPath(), '/');
        }
        return StringUtils.EMPTY;
    }

    //获取className
    public static String extractClassName(PsiFile psiFile) {
        // 检查 PsiFile 是否是 Java 文件
        if (psiFile instanceof PsiJavaFile) {
            PsiJavaFile javaFile = (PsiJavaFile) psiFile;

            // 获取 Java 文件中的类
            PsiClass[] classes = javaFile.getClasses();
            if (classes.length > 0) {
                // 获取第一个类的名称
                return classes[0].getName();
            }
        }

        // 如果不是 Java 文件或者没有类，则返回空字符串
        return "";
    }
}

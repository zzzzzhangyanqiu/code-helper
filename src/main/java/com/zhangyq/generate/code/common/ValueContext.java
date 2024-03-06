package com.zhangyq.generate.code.common;

import com.github.javafaker.Faker;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.zhangyq.generate.util.CodeUtil;
import com.zhangyq.generate.util.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyq01
 * @ClassName: ValueContext
 * @date 2024/1/25
 */
public class ValueContext {
    private static Faker faker = new Faker();

    public static ValueContext INSTANCE = new ValueContext();
    public static boolean isJsonFileSource = false;

    public static String resourcePath;
    /**
     * 事件
     */
    public static AnActionEvent event;
    /**
     * 目标java类
     */
    public static PsiClass psiClass;
    /**
     * 选择的baseTest
     */
    public static PsiFile baseTestFile;

    /**
     * java文件
     */
    public static PsiFile psiFile;
    /**
     * 测试文件路径
     */
    public static String filePath;
    /**
     * 测试文件名称
     */
    public static String fileName;
    /**
     * 测试文件路径
     */
    public static Path path;

    private final Map<String, PsiClass> cachedCLass = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, PsiMethod> cachedMethod = Collections.synchronizedMap(new HashMap<>());

    public static void setEvent(AnActionEvent e, PsiFile pf, PsiClass pc) {
        event = e;
        psiFile = pf;
        psiClass = pc;
        filePath = FileUtil.getUnitFilePath(psiFile);
        fileName = FileUtil.genJavaFileName(psiClass);
        path = Paths.get(filePath, fileName);
        resourcePath = psiFile.getProject().getBasePath() + "/src/main/resources";
    }

    public void loadClass() {
        Arrays.stream(psiClass.getAllFields())
                .filter(a -> !CodeUtil.isPrimitiveType(a.getType()))
                .forEach(field -> {
                            String fieldTypeName = field.getType().getCanonicalText();
                            PsiClass psiClass1 = PsiUtil.resolveClassInClassTypeOnly(field.getType());
                            if (psiClass1 != null) {
                                cachedCLass.put(fieldTypeName, psiClass1);
                                PsiMethod[] methods = psiClass1.getAllMethods();
                                for (PsiMethod a : methods) {
                                    if (!a.getModifierList().hasModifierProperty(PsiModifier.PRIVATE)) {
                                        // 参数列表
                                        PsiParameterList parameterList = a.getParameterList();
                                        String name = fieldTypeName + a.getName() + parameterList.getParametersCount();
                                        String shortName = field.getType().getPresentableText() + a.getName() + parameterList
                                                .getParametersCount();
                                        cachedMethod.put(name, a);
                                        cachedMethod.put(shortName, a);
                                        for (int i = 0; i < parameterList.getParametersCount(); i++) {
                                            PsiType fieldArgType = parameterList.getParameters()[i].getType();
                                            PsiClass fieldArgClass = PsiUtil.resolveClassInClassTypeOnly(fieldArgType);
                                            cachedCLass.put(fieldArgType.getCanonicalText(), fieldArgClass);
                                        }
                                    }

                                }
                            }
                        }
                );
    }

    public PsiClass getClass(String typeName) {
        return cachedCLass.get(typeName);
    }

    public PsiMethod getMethod(String className, String methodName, int argSize) {
        return getMethod(className + methodName + argSize);
    }

    public PsiMethod getMethod(String methodKey) {
        return cachedMethod.get(methodKey);
    }

    public void clear() {
        cachedCLass.clear();
        cachedMethod.clear();
    }

    public static Faker getFaker() {
        return faker;
    }

    public static void setFaker(Faker f) {
        faker = f;
    }

    public static PsiClass getPsiClass() {
        return psiClass;
    }

    public static PsiFile getPsiFile() {
        return psiFile;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static String getFileName() {
        return fileName;
    }

    public static Path getPath() {
        return path;
    }

    public static AnActionEvent getEvent() {
        return event;
    }

    public static boolean isJsonFileSource() {
        return isJsonFileSource;
    }

    public static ValueContext getContext() {
        return INSTANCE;
    }

    public static String getResourcePath() {
        return resourcePath;
    }

    public static PsiFile getBaseTestFile() {
        return baseTestFile;
    }

    public static void setBaseTestFile(PsiFile baseTestFile) {
        ValueContext.baseTestFile = baseTestFile;
    }
}

package com.zhangyq.generate.code.generator.file;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.zhangyq.generate.code.common.ValueContext;
import com.zhangyq.generate.code.generator.value.JsonFileGenerator;
import com.zhangyq.generate.code.pojo.MyField;
import com.zhangyq.generate.code.pojo.MyMethod;
import com.zhangyq.generate.util.CodeUtil;
import com.zhangyq.generate.util.FileUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangyq01
 * @ClassName: 单元测试代码生成
 * @date 2024/3/5
 */
public class UnitTestCodeGenerator extends TestFreemarkerConfiguration {
    ValueContext valueContext = ValueContext.getContext();
    /**
     * 当前类
     */
    private PsiClass psiClass;
    /**
     * 需要mock的类变量
     */
    private List<PsiField> needMockFields;
    /**
     * 需要输出测试的方法
     */
    private List<MyMethod> needMockMethods;

    /**
     * 当前文件，主要为了获取路径
     */
    private PsiFile psiFile;
    private Set<String> needImports = new HashSet<>();
    private Map<String, Integer> methodCount = new HashMap<>();
    private JsonFileGenerator jsonFileGenerator = new JsonFileGenerator();

    public UnitTestCodeGenerator(List<PsiField> fields, List<PsiMethod> needMockMethods) {
        this.psiClass = ValueContext.getPsiClass();
        this.needMockFields = fields;
        this.needMockMethods = needMockMethods.stream().map(a -> new MyMethod(a, this)).collect(Collectors.toList());
        this.psiFile = ValueContext.getPsiFile();
        if (!ValueContext.isJsonFileSource()) {
            needImports.add("import com.util.TestUtils;\n");
            needImports.add("import org.junit.jupiter.params.provider.ValueSource;\n");
            saveTestUtils();
        }
    }

    public UnitTestCodeGenerator(PsiClass psiClass, List<PsiMethod> needMockMethods, PsiFile psiFile) {
        this.psiClass = psiClass;
        this.needMockMethods = needMockMethods.stream().map(a -> new MyMethod(a, this)).collect(Collectors.toList());
        // todo 有一些小问题，比如不需要mock的fields
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(ValueContext.getPath().toFile());
        PsiJavaFile file = (PsiJavaFile)PsiManager.getInstance(ValueContext.getEvent().getProject()).findFile(virtualFile);
        PsiClass aClass = file.getClasses()[0];
        this.needMockFields = Arrays.asList(aClass.getAllFields());
        this.psiFile = psiFile;
    }

    private void saveTestUtils() {
        PsiDirectory parent = psiFile.getParent();
        if(Objects.isNull(parent)) {
            return;
        }
        String path = parent.getVirtualFile().getPath();
        String s = path.replaceAll("/src/main/java.*", "/src/test/java/com/util/");
        File file = new File(s + "TestUtils.java");
        if(file.exists()) {
           return;
        }
        FileCreateTask fileCreateTask = new FileCreateTask(s, "TestUtils.java", FileUtil.generateString("TestUtils.ftl", Maps.newHashMap(), this));
        ApplicationManager.getApplication().runWriteAction(fileCreateTask);
    }

    public String genContent() {
        generateImports();
        for (MyMethod method : needMockMethods) {
            method.build();
            needImports.addAll(method.getNeedImports());
        }
        needImports = needImports.stream()
                .filter(a -> a.contains(".") && !a.contains("String[]"))
                .collect(Collectors.toSet());

        List<String> methodList = needMockMethods.stream().map(MyMethod::getText).collect(Collectors.toList());

        Map<String, Object> modelMap = Maps.newHashMap();

        PsiFile baseTestFile = ValueContext.getBaseTestFile();
        if(Objects.nonNull(baseTestFile) && baseTestFile instanceof PsiJavaFile) {
            String packageName = ((PsiJavaFile) baseTestFile).getPackageName();
            String className = FileUtil.extractClassName(baseTestFile);
            needImports.add(String.format("import %s.%s;\n", packageName, className));
            modelMap.put("baseTestClass", className);
        }

        modelMap.put("packageName", PsiUtil.getPackageName(psiClass));
        modelMap.put("needImports", needImports);
        modelMap.put("targetClassName", psiClass.getName());
        modelMap.put("needMockFields", generateMockObjects());
        modelMap.put("methodList", methodList);

        return FileUtil.generateString("TestClass.ftl", modelMap, this);
    }

    /**
     * 生成需要mock的成员  直接使用freemarker获取presentableText有些问题
     *
     * @return
     */
    private List<MyField> generateMockObjects() {
        return needMockFields.stream().map(MyField::new).collect(Collectors.toList());
    }

    private void generateImports() {
        needMockFields.stream().map(a -> {
            String canonicalText = a.getType().getCanonicalText();
            return CodeUtil.filterGeneric(canonicalText);
        }).distinct().forEach(t -> needImports.add(String.format("import %s;\n", t)));
    }

    public String genMethodBody() {
        StringBuilder code = new StringBuilder();
        for (MyMethod method : needMockMethods) {
            method.build();
            code.append(method.getText());
        }
        return code.toString();
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public List<PsiField> getNeedMockFields() {
        return needMockFields;
    }

    public PsiFile getPsiFile() {
        return psiFile;
    }

    public Map<String, Integer> getMethodCount() {
        return methodCount;
    }
}

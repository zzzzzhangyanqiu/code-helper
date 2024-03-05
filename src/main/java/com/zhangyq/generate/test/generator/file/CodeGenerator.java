package com.zhangyq.generate.test.generator.file;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.zhangyq.generate.test.common.MockitoConstants;
import com.zhangyq.generate.test.common.ValueContext;
import com.zhangyq.generate.test.generator.value.JsonFileGenerator;
import com.zhangyq.generate.test.pojo.MyMethod;
import com.zhangyq.generate.util.CodeUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.zhangyq.generate.test.common.MockitoConstants.*;

/**
 * @author zhangyq01
 * @ClassName: CodeGenerator
 * @date 2024/3/5
 */
@Data
public class CodeGenerator {
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

    public CodeGenerator(List<PsiField> fields, List<PsiMethod> needMockMethods) {
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

    private void saveTestUtils() {
        PsiDirectory parent = psiFile.getParent();
        if(Objects.isNull(parent)) {
            return;
        }
        String path = parent.getVirtualFile().getPath();
        String s = path.replaceAll("/src/main/java.*", "/src/test/java/com/util/");
        ApplicationManager.getApplication().runWriteAction(
                new FileCreateTask(s, "TestUtils.java", MockitoConstants.TEST_UTILS_CLASS));
    }

    public String genContent() {
        generateImports();
        for (MyMethod method : needMockMethods) {
            method.build();
            needImports.addAll(method.getNeedImports());
        }
        StringBuilder code = new StringBuilder();
        code.append(generatePackageInfo());
        code.append("\n");
        needImports.stream().filter(a -> a.contains(".")).forEach(a -> code.append(a));
        code.append(generateCommonUnitImport());
        code.append(generateClassDeclaration());
        code.append(generateTestObject());
        code.append(generateMockObjects());
        code.append(generateSetUpMethod());

        for (MyMethod method : needMockMethods) {
            code.append(method.getText());
        }
        code.append("}");
        return code.toString();
    }

    private void generateImports() {
        needMockFields.stream().map(a -> {
            String canonicalText = a.getType().getCanonicalText();
            return CodeUtil.filterGeneric(canonicalText);
        }).distinct().forEach(t -> needImports.add(String.format("import %s;\n", t)));
    }

    private String generatePackageInfo() {
        return String.format("package %s;\n", PsiUtil.getPackageName(psiClass));
    }

    private String generateCommonUnitImport() {
        return COMMON_IMPORT;
    }

    private String generateClassDeclaration() {
        return COMMON_ANNOTATION + String.format("public class %sTest  {\n", psiClass.getName());
    }

    private String generateTestObject() {
        String name = psiClass.getName();
        assert name != null;
        return "\t@InjectMocks\n" + String.format("\tprivate %s %s=new %s(); \n", name, CodeUtil.getCamelCase(name), name);
    }

    private String generateMockObjects() {
        return needMockFields.stream().map(a -> {
            String canonicalText = a.getType().getPresentableText();
            return "\t@Mock\n" + String.format("\tprivate %s %s; \n\n", canonicalText,
                    a.getName());
        }).collect(Collectors.joining());
    }

    private String generateSetUpMethod() {
        return BEFORE_SETUP + "\n";
    }
}

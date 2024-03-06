package com.zhangyq.generate.test.pojo;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import lombok.Data;

/**
 * @author zhangyq01
 * @ClassName: MyField
 * @date 2024/3/6
 */
@Data
public class MyField {
    private final PsiType psiType;

    private final PsiField psiField;

    private final String presentableText;

    private final String name;


    public MyField(PsiField psiField) {
        this.psiField = psiField;
        this.psiType = psiField.getType();
        this.presentableText = psiField.getType().getPresentableText();
        this.name = psiField.getName();
    }
}

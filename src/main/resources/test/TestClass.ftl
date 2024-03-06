package ${packageName};

<#if needImports??>
<#list needImports as import>${import}</#list>
</#if>
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

<#if !baseTestClass??>
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)</#if>
public class ${targetClassName}Test <#if baseTestClass??>extends ${baseTestClass} </#if>{

    @Resource
    @InjectMocks
    private ${targetClassName} ${targetClassName?uncap_first};

<#if needMockFields??>
    <#list needMockFields as field>
    <#if baseTestClass??>
    private ${field.presentableText} ${field.name} = (${field.presentableText}) getSpy(${targetClassName?uncap_first}, "${field.name}");
    <#else>
    @Mock
    private ${field.presentableText} ${field.name};
    </#if>
    </#list>
</#if>
<#if !baseTestClass??>
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
</#if>
<#if methodList??>
    <#list methodList as method>
${method}</#list></#if>
}
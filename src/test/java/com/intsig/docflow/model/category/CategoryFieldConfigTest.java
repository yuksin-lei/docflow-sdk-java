package com.intsig.docflow.model.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CategoryFieldConfig 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryFieldConfigTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testBasicFields() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("发票号码");
        config.setDescription("发票号码字段");
        config.setPrompt("提取发票上的号码");

        assertEquals("发票号码", config.getName());
        assertEquals("发票号码字段", config.getDescription());
        assertEquals("提取发票上的号码", config.getPrompt());
    }

    @Test
    public void testNewFields() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("发票号码");
        config.setUsePrompt(true);
        config.setAlias(Arrays.asList("发票编号", "编号"));
        config.setIdentity("invoice_number");
        config.setMultiValue(true);
        config.setDuplicateValueDistinct(true);

        assertTrue(config.getUsePrompt());
        assertEquals(Arrays.asList("发票编号", "编号"), config.getAlias());
        assertEquals("invoice_number", config.getIdentity());
        assertTrue(config.getMultiValue());
        assertTrue(config.getDuplicateValueDistinct());
    }

    @Test
    public void testBuilderPattern() {
        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("发票金额")
                .description("发票金额字段")
                .prompt("提取发票金额")
                .usePrompt(true)
                .alias(Arrays.asList("金额", "总金额"))
                .identity("invoice_amount")
                .multiValue(false)
                .build();

        assertNotNull(config);
        assertEquals("发票金额", config.getName());
        assertTrue(config.getUsePrompt());
        assertEquals("invoice_amount", config.getIdentity());
    }

    @Test
    public void testDatetimeTransformSettings() {
        // 创建日期转换配置
        TransformSettings.DatetimeSettings datetimeSettings = TransformSettings.DatetimeSettings.builder()
                .format("yyyy-MM-dd HH:mm:ss")
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("datetime")
                .datetimeSettings(datetimeSettings)
                .build();

        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("日期字段")
                .transformSettings(transformSettings)
                .build();

        assertNotNull(config.getTransformSettings());
        assertEquals("datetime", config.getTransformSettings().getType());
        assertEquals("yyyy-MM-dd HH:mm:ss", config.getTransformSettings().getDatetimeSettings().getFormat());
    }

    @Test
    public void testEnumerateTransformSettings() {
        // 创建枚举转换配置
        TransformSettings.EnumerateSettings enumerateSettings = TransformSettings.EnumerateSettings.builder()
                .items(Arrays.asList("选项1", "选项2", "选项3"))
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("enumerate")
                .enumerateSettings(enumerateSettings)
                .build();

        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("状态字段")
                .transformSettings(transformSettings)
                .build();

        assertNotNull(config.getTransformSettings());
        assertEquals("enumerate", config.getTransformSettings().getType());
        assertEquals(3, config.getTransformSettings().getEnumerateSettings().getItems().size());
    }

    @Test
    public void testRegexTransformSettings() {
        // 创建正则转换配置
        TransformSettings.RegexSettings regexSettings = TransformSettings.RegexSettings.builder()
                .match("^(\\d{4})-(\\d{2})-(\\d{2})$")
                .replace("$1/$2/$3")
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("regex")
                .regexSettings(regexSettings)
                .build();

        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("格式化字段")
                .transformSettings(transformSettings)
                .build();

        assertNotNull(config.getTransformSettings());
        assertEquals("regex", config.getTransformSettings().getType());
        assertNotNull(config.getTransformSettings().getRegexSettings().getMatch());
    }

    @Test
    public void testMismatchAction() {
        // 创建不匹配处理配置
        TransformSettings.MismatchAction mismatchAction = TransformSettings.MismatchAction.builder()
                .mode("default")
                .defaultValue("未知")
                .build();

        TransformSettings.EnumerateSettings enumerateSettings = TransformSettings.EnumerateSettings.builder()
                .items(Arrays.asList("正常", "异常"))
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("enumerate")
                .enumerateSettings(enumerateSettings)
                .mismatchAction(mismatchAction)
                .build();

        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("状态")
                .transformSettings(transformSettings)
                .build();

        assertNotNull(config.getTransformSettings().getMismatchAction());
        assertEquals("default", config.getTransformSettings().getMismatchAction().getMode());
        assertEquals("未知", config.getTransformSettings().getMismatchAction().getDefaultValue());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("测试字段")
                .description("测试描述")
                .prompt("测试提示")
                .usePrompt(true)
                .alias(Arrays.asList("别名1", "别名2"))
                .identity("test_field")
                .multiValue(true)
                .duplicateValueDistinct(true)
                .build();

        // 序列化
        String json = objectMapper.writeValueAsString(config);
        assertNotNull(json);
        assertTrue(json.contains("test_field"));

        // 反序列化
        CategoryFieldConfig deserialized = objectMapper.readValue(json, CategoryFieldConfig.class);
        assertEquals(config.getName(), deserialized.getName());
        assertEquals(config.getIdentity(), deserialized.getIdentity());
        assertEquals(config.getMultiValue(), deserialized.getMultiValue());
    }

    @Test
    public void testCompleteExample() {
        // 创建一个完整的字段配置示例
        TransformSettings.DatetimeSettings datetimeSettings = TransformSettings.DatetimeSettings.builder()
                .format("yyyy-MM-dd")
                .build();

        TransformSettings.MismatchAction mismatchAction = TransformSettings.MismatchAction.builder()
                .mode("warning")
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("datetime")
                .datetimeSettings(datetimeSettings)
                .mismatchAction(mismatchAction)
                .build();

        CategoryFieldConfig config = CategoryFieldConfig.builder()
                .name("发票日期")
                .description("发票的开票日期")
                .prompt("提取发票上的日期信息")
                .usePrompt(true)
                .alias(Arrays.asList("日期", "开票日期", "开具日期"))
                .identity("invoice_date")
                .multiValue(false)
                .transformSettings(transformSettings)
                .build();

        // 验证所有字段
        assertNotNull(config);
        assertEquals("发票日期", config.getName());
        assertEquals("发票的开票日期", config.getDescription());
        assertEquals("提取发票上的日期信息", config.getPrompt());
        assertTrue(config.getUsePrompt());
        assertEquals(3, config.getAlias().size());
        assertEquals("invoice_date", config.getIdentity());
        assertFalse(config.getMultiValue());
        assertNotNull(config.getTransformSettings());
        assertEquals("datetime", config.getTransformSettings().getType());
        assertEquals("yyyy-MM-dd", config.getTransformSettings().getDatetimeSettings().getFormat());
    }
}

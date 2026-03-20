package com.intsig.docflow.model.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CategoryField 测试类
 * <p>
 * 测试 CategoryField 继承 CategoryFieldConfig 的正确性
 * </p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryFieldTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testInheritance() {
        CategoryField field = new CategoryField();
        field.setId("field123");
        field.setName("发票号码");
        field.setDescription("发票号码字段");

        // 验证ID字段
        assertEquals("field123", field.getId());

        // 验证继承的字段
        assertEquals("发票号码", field.getName());
        assertEquals("发票号码字段", field.getDescription());
    }

    @Test
    public void testAllFields() {
        CategoryField field = new CategoryField();
        field.setId("field456");
        field.setName("发票金额");
        field.setDescription("发票金额字段");
        field.setPrompt("提取发票金额");
        field.setUsePrompt(true);
        field.setAlias(Arrays.asList("金额", "总金额"));
        field.setIdentity("invoice_amount");
        field.setMultiValue(false);
        field.setDuplicateValueDistinct(false);

        // 验证所有字段
        assertEquals("field456", field.getId());
        assertEquals("发票金额", field.getName());
        assertEquals("发票金额字段", field.getDescription());
        assertEquals("提取发票金额", field.getPrompt());
        assertTrue(field.getUsePrompt());
        assertEquals(2, field.getAlias().size());
        assertEquals("invoice_amount", field.getIdentity());
        assertFalse(field.getMultiValue());
        assertFalse(field.getDuplicateValueDistinct());
    }

    @Test
    public void testWithTransformSettings() {
        // 创建转换配置
        TransformSettings.DatetimeSettings datetimeSettings = TransformSettings.DatetimeSettings.builder()
                .format("yyyy-MM-dd")
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("datetime")
                .datetimeSettings(datetimeSettings)
                .build();

        CategoryField field = new CategoryField();
        field.setId("field789");
        field.setName("日期字段");
        field.setTransformSettings(transformSettings);

        // 验证
        assertNotNull(field.getTransformSettings());
        assertEquals("datetime", field.getTransformSettings().getType());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        CategoryField field = new CategoryField();
        field.setId("field001");
        field.setName("测试字段");
        field.setDescription("测试描述");
        field.setIdentity("test_field");
        field.setMultiValue(true);
        field.setAlias(Arrays.asList("别名1", "别名2"));

        // 序列化
        String json = objectMapper.writeValueAsString(field);
        assertNotNull(json);
        assertTrue(json.contains("field001"));
        assertTrue(json.contains("test_field"));

        // 反序列化
        CategoryField deserialized = objectMapper.readValue(json, CategoryField.class);
        assertEquals(field.getId(), deserialized.getId());
        assertEquals(field.getName(), deserialized.getName());
        assertEquals(field.getIdentity(), deserialized.getIdentity());
        assertEquals(field.getMultiValue(), deserialized.getMultiValue());
        assertEquals(field.getAlias().size(), deserialized.getAlias().size());
    }

    @Test
    public void testJsonWithTransformSettings() throws Exception {
        // 创建完整的字段对象
        TransformSettings.EnumerateSettings enumerateSettings = TransformSettings.EnumerateSettings.builder()
                .items(Arrays.asList("选项A", "选项B", "选项C"))
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("enumerate")
                .enumerateSettings(enumerateSettings)
                .build();

        CategoryField field = new CategoryField();
        field.setId("field002");
        field.setName("枚举字段");
        field.setTransformSettings(transformSettings);

        // 序列化和反序列化
        String json = objectMapper.writeValueAsString(field);
        CategoryField deserialized = objectMapper.readValue(json, CategoryField.class);

        // 验证转换配置被正确序列化和反序列化
        assertNotNull(deserialized.getTransformSettings());
        assertEquals("enumerate", deserialized.getTransformSettings().getType());
        assertEquals(3, deserialized.getTransformSettings().getEnumerateSettings().getItems().size());
    }

    @Test
    public void testEqualsAndHashCode() {
        CategoryField field1 = new CategoryField();
        field1.setId("field001");
        field1.setName("字段1");

        CategoryField field2 = new CategoryField();
        field2.setId("field001");
        field2.setName("字段1");

        CategoryField field3 = new CategoryField();
        field3.setId("field002");
        field3.setName("字段2");

        // 测试 equals（通过Lombok的@EqualsAndHashCode生成）
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);

        // 测试 hashCode
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testCompleteExample() {
        // 创建一个完整的字段示例
        TransformSettings.RegexSettings regexSettings = TransformSettings.RegexSettings.builder()
                .match("^\\d{18}$")
                .replace("****")
                .build();

        TransformSettings.MismatchAction mismatchAction = TransformSettings.MismatchAction.builder()
                .mode("default")
                .defaultValue("无效")
                .build();

        TransformSettings transformSettings = TransformSettings.builder()
                .type("regex")
                .regexSettings(regexSettings)
                .mismatchAction(mismatchAction)
                .build();

        CategoryField field = new CategoryField();
        field.setId("field_id_card");
        field.setName("身份证号");
        field.setDescription("身份证号码字段");
        field.setPrompt("提取身份证号码");
        field.setUsePrompt(true);
        field.setAlias(Arrays.asList("身份证", "ID卡号"));
        field.setIdentity("id_card_number");
        field.setMultiValue(false);
        field.setTransformSettings(transformSettings);

        // 验证所有字段
        assertEquals("field_id_card", field.getId());
        assertEquals("身份证号", field.getName());
        assertEquals("身份证号码字段", field.getDescription());
        assertTrue(field.getUsePrompt());
        assertEquals("id_card_number", field.getIdentity());
        assertFalse(field.getMultiValue());
        assertNotNull(field.getTransformSettings());
        assertEquals("regex", field.getTransformSettings().getType());
    }
}

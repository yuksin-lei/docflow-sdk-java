package com.intsig.docflow.resource;

import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.model.category.CategoryFieldConfig;
import com.intsig.docflow.model.category.CategoryFieldsListResponse;

import java.util.Collections;
import java.util.List;

/**
 * 绑定的字段资源操作类
 * <p>
 * 提供字段管理的链式调用API，无需重复传递 workspace_id 和 category_id
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 通过类别资源创建绑定的字段资源
 * BoundCategoryResource cat = client.workspace("ws-id").category("cat-id");
 * BoundFieldsResource fields = cat.fields();
 *
 * // 添加字段（无需传递 workspace_id 和 category_id）
 * CategoryFieldConfig config = CategoryFieldConfig.builder()
 *     .name("发票号码")
 *     .description("发票唯一标识")
 *     .build();
 * String fieldId = fields.add(config);
 *
 * // 获取字段列表
 * CategoryFieldsListResponse list = fields.list();
 *
 * // 更新字段
 * fields.update("field-id", updatedConfig);
 *
 * // 删除字段
 * fields.delete("field-id");
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class BoundFieldsResource {

    private final CategoryResource categoryResource;
    private final String workspaceId;
    private final String categoryId;

    /**
     * 构造绑定的字段资源
     *
     * @param categoryResource 类别资源操作接口
     * @param workspaceId      工作空间ID
     * @param categoryId       类别ID
     */
    public BoundFieldsResource(CategoryResource categoryResource,
                               String workspaceId,
                               String categoryId) {
        this.categoryResource = categoryResource;
        this.workspaceId = workspaceId;
        this.categoryId = categoryId;
    }

    /**
     * 获取绑定的工作空间ID
     *
     * @return 工作空间ID
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * 获取绑定的类别ID
     *
     * @return 类别ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 获取字段列表
     *
     * @return 字段列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategoryFieldsListResponse list() throws DocflowException {
        return categoryResource.listFields(workspaceId, categoryId);
    }

    /**
     * 添加字段（普通字段，非表格字段）
     *
     * @param fieldConfig 字段配置
     * @return 字段ID
     * @throws DocflowException API调用失败时抛出
     */
    public String add(CategoryFieldConfig fieldConfig) throws DocflowException {
        return categoryResource.addField(workspaceId, categoryId, null, fieldConfig);
    }

    /**
     * 添加表格字段
     *
     * @param tableId     表格ID
     * @param fieldConfig 字段配置
     * @return 字段ID
     * @throws DocflowException API调用失败时抛出
     */
    public String add(String tableId, CategoryFieldConfig fieldConfig) throws DocflowException {
        return categoryResource.addField(workspaceId, categoryId, tableId, fieldConfig);
    }

    /**
     * 更新字段（普通字段）
     *
     * @param fieldId     字段ID
     * @param fieldConfig 字段配置
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String fieldId, CategoryFieldConfig fieldConfig) throws DocflowException {
        categoryResource.updateField(workspaceId, categoryId, fieldId, null, fieldConfig);
    }

    /**
     * 更新表格字段
     *
     * @param fieldId     字段ID
     * @param tableId     表格ID
     * @param fieldConfig 字段配置
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String fieldId, String tableId, CategoryFieldConfig fieldConfig) throws DocflowException {
        categoryResource.updateField(workspaceId, categoryId, fieldId, tableId, fieldConfig);
    }

    /**
     * 删除字段（普通字段）
     *
     * @param fieldId 字段ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(String fieldId) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, Collections.singletonList(fieldId), null);
    }

    /**
     * 批量删除字段（普通字段）
     *
     * @param fieldIds 字段ID列表
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(List<String> fieldIds) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, fieldIds, null);
    }

    /**
     * 删除表格字段
     *
     * @param fieldId 字段ID
     * @param tableId 表格ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(String fieldId, String tableId) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, Collections.singletonList(fieldId), tableId);
    }

    /**
     * 批量删除表格字段
     *
     * @param fieldIds 字段ID列表
     * @param tableId  表格ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(List<String> fieldIds, String tableId) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, fieldIds, tableId);
    }
}

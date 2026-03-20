package com.intsig.docflow.resource;

import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.model.category.CategoryFieldConfig;

import java.util.Collections;
import java.util.List;

/**
 * 绑定的表格字段资源操作类
 * <p>
 * 提供表格字段管理的链式调用API，无需重复传递 workspace_id、category_id 和 table_id
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 通过表格资源创建绑定的表格字段资源
 * BoundTablesResource tables = client.workspace("ws-id").category("cat-id").tables();
 * BoundTableFieldsResource fields = tables.fields("table-id");
 *
 * // 添加表格字段（无需传递 workspace_id、category_id、table_id）
 * CategoryFieldConfig config = CategoryFieldConfig.builder()
 *     .name("商品名称")
 *     .description("商品的名称")
 *     .build();
 * String fieldId = fields.add(config);
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
public class BoundTableFieldsResource {

    private final CategoryResource categoryResource;
    private final String workspaceId;
    private final String categoryId;
    private final String tableId;

    /**
     * 构造绑定的表格字段资源
     *
     * @param categoryResource 类别资源操作接口
     * @param workspaceId      工作空间ID
     * @param categoryId       类别ID
     * @param tableId          表格ID
     */
    public BoundTableFieldsResource(CategoryResource categoryResource,
                                    String workspaceId,
                                    String categoryId,
                                    String tableId) {
        this.categoryResource = categoryResource;
        this.workspaceId = workspaceId;
        this.categoryId = categoryId;
        this.tableId = tableId;
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
     * 获取绑定的表格ID
     *
     * @return 表格ID
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * 添加表格字段
     *
     * @param fieldConfig 字段配置
     * @return 字段ID
     * @throws DocflowException API调用失败时抛出
     */
    public String add(CategoryFieldConfig fieldConfig) throws DocflowException {
        return categoryResource.addField(workspaceId, categoryId, tableId, fieldConfig);
    }

    /**
     * 更新表格字段
     *
     * @param fieldId     字段ID
     * @param fieldConfig 字段配置
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String fieldId, CategoryFieldConfig fieldConfig) throws DocflowException {
        categoryResource.updateField(workspaceId, categoryId, fieldId, tableId, fieldConfig);
    }

    /**
     * 删除表格字段
     *
     * @param fieldId 字段ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(String fieldId) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, Collections.singletonList(fieldId), tableId);
    }

    /**
     * 批量删除表格字段
     *
     * @param fieldIds 字段ID列表
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(List<String> fieldIds) throws DocflowException {
        categoryResource.deleteFields(workspaceId, categoryId, fieldIds, tableId);
    }
}

package com.intsig.docflow.resource;

import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.model.category.CategoryFieldsListResponse;
import com.intsig.docflow.model.category.CategoryTablesListResponse;

import java.util.Collections;

/**
 * 绑定类别ID的资源操作类
 * <p>
 * 提供链式调用API，减少重复参数传递
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 创建绑定的类别资源
 * BoundCategoryResource cat = client.workspace("workspace-id").category("category-id");
 *
 * // 更新类别
 * cat.update("新类别名称");
 *
 * // 删除类别
 * cat.delete();
 *
 * // 字段管理（链式调用）
 * BoundFieldsResource fields = cat.fields();
 * fields.add(fieldConfig);
 * fields.list();
 * fields.delete("field-id");
 *
 * // 表格管理（链式调用）
 * BoundTablesResource tables = cat.tables();
 * tables.add("表格名称");
 * tables.list();
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class BoundCategoryResource {

    private final CategoryResource categoryResource;
    private final String workspaceId;
    private final String categoryId;

    /**
     * 构造绑定的类别资源
     *
     * @param categoryResource 类别资源操作接口
     * @param workspaceId      工作空间ID
     * @param categoryId       类别ID
     */
    public BoundCategoryResource(CategoryResource categoryResource,
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
     * 更新类别名称
     *
     * @param name 新的类别名称
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String name) throws DocflowException {
        categoryResource.update(workspaceId, categoryId, name, null, null);
    }

    /**
     * 更新类别信息
     *
     * @param name           新的类别名称
     * @param categoryPrompt 分类提示词
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String name, String categoryPrompt) throws DocflowException {
        categoryResource.update(workspaceId, categoryId, name, categoryPrompt, null);
    }

    /**
     * 更新类别信息（包含启用状态）
     *
     * @param name           新的类别名称
     * @param categoryPrompt 分类提示词
     * @param enabled        启用状态
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String name, String categoryPrompt, EnabledStatus enabled) throws DocflowException {
        categoryResource.update(workspaceId, categoryId, name, categoryPrompt, enabled);
    }

    /**
     * 删除类别
     *
     * @throws DocflowException API调用失败时抛出
     */
    public void delete() throws DocflowException {
        categoryResource.delete(workspaceId, Collections.singletonList(categoryId));
    }

    /**
     * 创建绑定的字段资源（链式调用）
     *
     * @return 绑定的字段资源
     */
    public BoundFieldsResource fields() {
        return new BoundFieldsResource(categoryResource, workspaceId, categoryId);
    }

    /**
     * 创建绑定的表格资源（链式调用）
     *
     * @return 绑定的表格资源
     */
    public BoundTablesResource tables() {
        return new BoundTablesResource(categoryResource, workspaceId, categoryId);
    }

    /**
     * 创建绑定的样本资源（链式调用）
     *
     * @return 绑定的样本资源
     */
    public BoundSamplesResource samples() {
        return new BoundSamplesResource(categoryResource, workspaceId, categoryId);
    }

    /**
     * 获取字段列表（快捷方法）
     *
     * @return 字段列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategoryFieldsListResponse listFields() throws DocflowException {
        return categoryResource.listFields(workspaceId, categoryId);
    }

    /**
     * 获取表格列表（快捷方法）
     *
     * @return 表格列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategoryTablesListResponse listTables() throws DocflowException {
        return categoryResource.listTables(workspaceId, categoryId);
    }
}

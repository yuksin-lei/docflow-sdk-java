package com.intsig.docflow.resource;

import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.model.category.CategoryTablesListResponse;

import java.util.Collections;
import java.util.List;

/**
 * 绑定的表格资源操作类
 * <p>
 * 提供表格管理的链式调用API，无需重复传递 workspace_id 和 category_id
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 通过类别资源创建绑定的表格资源
 * BoundCategoryResource cat = client.workspace("ws-id").category("cat-id");
 * BoundTablesResource tables = cat.tables();
 *
 * // 添加表格（无需传递 workspace_id 和 category_id）
 * String tableId = tables.add("商品明细表", "提取商品信息", true);
 *
 * // 获取表格列表
 * CategoryTablesListResponse list = tables.list();
 *
 * // 更新表格
 * tables.update("table-id", "新表格名称", null, false);
 *
 * // 删除表格
 * tables.delete("table-id");
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class BoundTablesResource {

    private final CategoryResource categoryResource;
    private final String workspaceId;
    private final String categoryId;

    /**
     * 构造绑定的表格资源
     *
     * @param categoryResource 类别资源操作接口
     * @param workspaceId      工作空间ID
     * @param categoryId       类别ID
     */
    public BoundTablesResource(CategoryResource categoryResource,
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
     * 获取表格列表
     *
     * @return 表格列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategoryTablesListResponse list() throws DocflowException {
        return categoryResource.listTables(workspaceId, categoryId);
    }

    /**
     * 添加表格（仅指定名称）
     *
     * @param name 表格名称
     * @return 表格ID
     * @throws DocflowException API调用失败时抛出
     */
    public String add(String name) throws DocflowException {
        return categoryResource.addTable(workspaceId, categoryId, name, null, null);
    }

    /**
     * 添加表格
     *
     * @param name                   表格名称
     * @param prompt                 表格语义抽取提示词
     * @param collectFromMultiTable  是否多表合并
     * @return 表格ID
     * @throws DocflowException API调用失败时抛出
     */
    public String add(String name, String prompt, Boolean collectFromMultiTable) throws DocflowException {
        return categoryResource.addTable(workspaceId, categoryId, name, prompt, collectFromMultiTable);
    }

    /**
     * 更新表格
     *
     * @param tableId                表格ID
     * @param name                   表格名称
     * @param prompt                 表格语义抽取提示词
     * @param collectFromMultiTable  是否多表合并
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String tableId, String name, String prompt, Boolean collectFromMultiTable) throws DocflowException {
        categoryResource.updateTable(workspaceId, categoryId, tableId, name, prompt, collectFromMultiTable);
    }

    /**
     * 删除表格
     *
     * @param tableId 表格ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(String tableId) throws DocflowException {
        categoryResource.deleteTables(workspaceId, categoryId, Collections.singletonList(tableId));
    }

    /**
     * 批量删除表格
     *
     * @param tableIds 表格ID列表
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(List<String> tableIds) throws DocflowException {
        categoryResource.deleteTables(workspaceId, categoryId, tableIds);
    }

    /**
     * 获取指定表格的字段资源（链式调用）
     *
     * @param tableId 表格ID
     * @return 绑定的表格字段资源
     */
    public BoundTableFieldsResource fields(String tableId) {
        return new BoundTableFieldsResource(categoryResource, workspaceId, categoryId, tableId);
    }
}

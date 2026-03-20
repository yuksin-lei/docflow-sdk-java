package com.intsig.docflow.resource;

import com.intsig.docflow.enums.AuthScope;
import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.model.workspace.WorkspaceInfo;

/**
 * 绑定工作空间ID的资源操作类
 * <p>
 * 提供链式调用API，减少重复参数传递
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 创建绑定的工作空间资源
 * BoundWorkspaceResource ws = client.workspace("workspace-id-123");
 *
 * // 获取工作空间详情（无需再传workspace_id）
 * WorkspaceInfo info = ws.get();
 *
 * // 更新工作空间
 * ws.update("新名称", AuthScope.PUBLIC);
 *
 * // 删除工作空间
 * ws.delete();
 *
 * // 链式访问类别资源
 * BoundCategoryResource cat = ws.category("category-id-456");
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class BoundWorkspaceResource {

    private final WorkspaceResource workspaceResource;
    private final CategoryResource categoryResource;
    private final String workspaceId;

    /**
     * 构造绑定的工作空间资源
     *
     * @param workspaceResource 工作空间资源操作接口
     * @param categoryResource  类别资源操作接口
     * @param workspaceId       工作空间ID
     */
    public BoundWorkspaceResource(WorkspaceResource workspaceResource,
                                  CategoryResource categoryResource,
                                  String workspaceId) {
        this.workspaceResource = workspaceResource;
        this.categoryResource = categoryResource;
        this.workspaceId = workspaceId;
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
     * 获取工作空间详情
     *
     * @return 工作空间信息
     * @throws DocflowException API调用失败时抛出
     */
    public WorkspaceInfo get() throws DocflowException {
        return workspaceResource.get(workspaceId);
    }

    /**
     * 更新工作空间信息
     *
     * @param name      新的工作空间名称
     * @param authScope 协作范围
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String name, AuthScope authScope) throws DocflowException {
        workspaceResource.update(workspaceId, name, authScope.getValue());
    }

    /**
     * 更新工作空间名称
     *
     * @param name 新的工作空间名称
     * @throws DocflowException API调用失败时抛出
     */
    public void update(String name) throws DocflowException {
        workspaceResource.update(workspaceId, name, null);
    }

    /**
     * 删除工作空间
     *
     * @throws DocflowException API调用失败时抛出
     */
    public void delete() throws DocflowException {
        workspaceResource.delete(workspaceId);
    }

    /**
     * 创建绑定的类别资源（链式调用）
     *
     * @param categoryId 类别ID
     * @return 绑定的类别资源
     */
    public BoundCategoryResource category(String categoryId) {
        return new BoundCategoryResource(categoryResource, workspaceId, categoryId);
    }
}

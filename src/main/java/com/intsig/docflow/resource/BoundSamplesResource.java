package com.intsig.docflow.resource;

import com.intsig.docflow.exception.DocflowException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.model.category.CategorySample;
import com.intsig.docflow.model.category.CategorySampleListResponse;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 绑定的样本资源操作类
 * <p>
 * 提供样本管理的链式调用API，无需重复传递 workspace_id 和 category_id
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 通过类别资源创建绑定的样本资源
 * BoundCategoryResource cat = client.workspace("ws-id").category("cat-id");
 * BoundSamplesResource samples = cat.samples();
 *
 * // 上传样本（无需传递 workspace_id 和 category_id）
 * String sampleId = samples.upload(new File("/path/to/sample.pdf"));
 *
 * // 获取样本列表
 * CategorySampleListResponse list = samples.list();
 *
 * // 迭代所有样本
 * for (CategorySample sample : samples.iter()) {
 *     System.out.println(sample.getSampleId());
 * }
 *
 * // 下载样本
 * HttpClient.DownloadResult result = samples.download("sample-id");
 *
 * // 删除样本
 * samples.delete("sample-id");
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class BoundSamplesResource {

    private final CategoryResource categoryResource;
    private final String workspaceId;
    private final String categoryId;

    /**
     * 构造绑定的样本资源
     *
     * @param categoryResource 类别资源操作接口
     * @param workspaceId      工作空间ID
     * @param categoryId       类别ID
     */
    public BoundSamplesResource(CategoryResource categoryResource,
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
     * 上传样本
     *
     * @param file 样本文件
     * @return 样本ID
     * @throws DocflowException API调用失败时抛出
     */
    public String upload(File file) throws DocflowException {
        return categoryResource.uploadSample(workspaceId, categoryId, file);
    }

    /**
     * 上传样本（使用文件路径）
     *
     * @param filePath 样本文件路径
     * @return 样本ID
     * @throws DocflowException API调用失败时抛出
     */
    public String upload(String filePath) throws DocflowException {
        return categoryResource.uploadSample(workspaceId, categoryId, new File(filePath));
    }

    /**
     * 获取样本列表
     *
     * @return 样本列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategorySampleListResponse list() throws DocflowException {
        return categoryResource.listSamples(workspaceId, categoryId);
    }

    /**
     * 获取样本列表（分页）
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 样本列表响应
     * @throws DocflowException API调用失败时抛出
     */
    public CategorySampleListResponse list(Integer page, Integer pageSize) throws DocflowException {
        return categoryResource.listSamples(workspaceId, categoryId, page, pageSize);
    }

    /**
     * 迭代获取所有样本
     * <p>
     * 自动处理分页，返回可迭代对象
     * </p>
     *
     * @return 样本信息的可迭代对象
     * @throws DocflowException API调用失败时抛出
     */
    public PageIterable<CategorySample> iter() throws DocflowException {
        return categoryResource.iterSamples(workspaceId, categoryId);
    }

    /**
     * 迭代获取所有样本（指定分页参数）
     *
     * @param pageSize 每页数量
     * @param maxPages 最大页数限制
     * @return 样本信息的可迭代对象
     * @throws DocflowException API调用失败时抛出
     */
    public PageIterable<CategorySample> iter(Integer pageSize, Integer maxPages) throws DocflowException {
        return categoryResource.iterSamples(workspaceId, categoryId, pageSize, maxPages);
    }

    /**
     * 下载样本
     *
     * @param sampleId 样本ID
     * @return 下载结果，包含文件数据和文件名
     * @throws DocflowException API调用失败时抛出
     */
    public HttpClient.DownloadResult download(String sampleId) throws DocflowException {
        return categoryResource.downloadSample(workspaceId, categoryId, sampleId);
    }

    /**
     * 删除样本
     *
     * @param sampleId 样本ID
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(String sampleId) throws DocflowException {
        categoryResource.deleteSamples(workspaceId, categoryId, Collections.singletonList(sampleId));
    }

    /**
     * 批量删除样本
     *
     * @param sampleIds 样本ID列表
     * @throws DocflowException API调用失败时抛出
     */
    public void delete(List<String> sampleIds) throws DocflowException {
        categoryResource.deleteSamples(workspaceId, categoryId, sampleIds);
    }
}

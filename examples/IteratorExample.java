import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.config.DocflowConfig;
import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.model.category.CategoryInfo;
import com.intsig.docflow.model.category.CategorySample;
import com.intsig.docflow.model.file.FileInfo;
import com.intsig.docflow.model.review.ReviewRepoInfo;
import com.intsig.docflow.model.workspace.WorkspaceInfo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 分页迭代器使用示例
 * <p>
 * 演示如何使用迭代器自动处理分页查询
 * </p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class IteratorExample {

    public static void main(String[] args) {
        // 初始化客户端
        DocflowConfig config = DocflowConfig.builder()
                .apiKey("your-api-key")
                .apiSecret("your-api-secret")
                .build();

        DocflowClient client = new DocflowClient(config);

        String workspaceId = "your-workspace-id";
        Long enterpriseId = 12345L;

        // 示例1: 遍历所有工作空间
        iterateWorkspaces(client, enterpriseId);

        // 示例2: 遍历所有文件
        iterateFiles(client, workspaceId);

        // 示例3: 遍历所有类别
        iterateCategories(client, workspaceId);

        // 示例4: 遍历类别样本
        iterateCategorySamples(client, workspaceId, "category-id");

        // 示例5: 遍历审核规则库
        iterateReviewRepos(client, workspaceId);

        // 示例6: 使用 Stream API 进行过滤和转换
        useStreamAPI(client, workspaceId);

        // 示例7: 限制最大页数
        limitMaxPages(client, workspaceId);

        // 示例8: 提前终止迭代
        earlyBreak(client, workspaceId);
    }

    /**
     * 示例1: 遍历所有工作空间
     */
    private static void iterateWorkspaces(DocflowClient client, Long enterpriseId) {
        System.out.println("=== 示例1: 遍历所有工作空间 ===");

        PageIterable<WorkspaceInfo> workspaces = client.workspace.iter(enterpriseId);

        for (WorkspaceInfo workspace : workspaces) {
            System.out.println("工作空间: " + workspace.getWorkspaceId() + " - " + workspace.getName());
        }
    }

    /**
     * 示例2: 遍历所有文件
     */
    private static void iterateFiles(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例2: 遍历所有文件 ===");

        // 使用默认参数
        PageIterable<FileInfo> files = client.file.iter(workspaceId);

        for (FileInfo file : files) {
            System.out.println("文件: " + file.getFileId() + " - " + file.getFileName());
        }
    }

    /**
     * 示例3: 遍历所有类别
     */
    private static void iterateCategories(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例3: 遍历所有类别 ===");

        // 只获取启用的类别
        PageIterable<CategoryInfo> categories = client.category.iter(workspaceId, EnabledStatus.ENABLED);

        for (CategoryInfo category : categories) {
            System.out.println("类别: " + category.getCategoryId() + " - " + category.getName());
        }
    }

    /**
     * 示例4: 遍历类别样本
     */
    private static void iterateCategorySamples(DocflowClient client, String workspaceId, String categoryId) {
        System.out.println("\n=== 示例4: 遍历类别样本 ===");

        PageIterable<CategorySample> samples = client.category.iterSamples(workspaceId, categoryId);

        for (CategorySample sample : samples) {
            System.out.println("样本: " + sample.getSampleId() + " - " + sample.getFileName());
        }
    }

    /**
     * 示例5: 遍历审核规则库
     */
    private static void iterateReviewRepos(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例5: 遍历审核规则库 ===");

        PageIterable<ReviewRepoInfo> repos = client.review.iterRepos(workspaceId);

        for (ReviewRepoInfo repo : repos) {
            System.out.println("规则库: " + repo.getRepoId() + " - " + repo.getName());
        }
    }

    /**
     * 示例6: 使用 Stream API 进行过滤和转换
     */
    private static void useStreamAPI(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例6: 使用 Stream API ===");

        PageIterable<FileInfo> files = client.file.iter(workspaceId);

        // 使用 Stream API 过滤和收集
        List<String> pdfFiles = StreamSupport.stream(files.spliterator(), false)
                .filter(file -> file.getFileName() != null && file.getFileName().endsWith(".pdf"))
                .map(FileInfo::getFileName)
                .collect(Collectors.toList());

        System.out.println("PDF 文件列表:");
        pdfFiles.forEach(System.out::println);
    }

    /**
     * 示例7: 限制最大页数
     */
    private static void limitMaxPages(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例7: 限制最大页数 ===");

        // 只获取前3页数据
        PageIterable<FileInfo> files = client.file.iter(
                workspaceId,
                null,    // 使用默认 pageSize
                3,       // 最多获取3页
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        int count = 0;
        for (FileInfo file : files) {
            count++;
        }

        System.out.println("获取到的文件数量: " + count);
    }

    /**
     * 示例8: 提前终止迭代
     */
    private static void earlyBreak(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例8: 提前终止迭代 ===");

        PageIterable<FileInfo> files = client.file.iter(workspaceId);

        int count = 0;
        for (FileInfo file : files) {
            System.out.println("文件: " + file.getFileName());
            count++;

            // 只处理前10个文件
            if (count >= 10) {
                System.out.println("已处理10个文件，提前终止");
                break;
            }
        }
    }

    /**
     * 示例9: 带筛选条件的文件迭代
     */
    private static void iterateFilesWithFilters(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例9: 带筛选条件的文件迭代 ===");

        // 只遍历指定类别和识别状态的文件
        PageIterable<FileInfo> files = client.file.iter(
                workspaceId,
                "batch-001",        // 批次编号
                "发票",              // 类别
                "success",           // 识别状态
                null                 // 核对状态
        );

        for (FileInfo file : files) {
            System.out.println("文件: " + file.getFileName() + ", 状态: " + file.getRecognitionStatus());
        }
    }

    /**
     * 示例10: 自定义分页大小
     */
    private static void customPageSize(DocflowClient client, Long enterpriseId) {
        System.out.println("\n=== 示例10: 自定义分页大小 ===");

        // 每页获取50条数据（默认是20）
        PageIterable<WorkspaceInfo> workspaces = client.workspace.iter(
                enterpriseId,
                50,     // 自定义 pageSize
                null    // 不限制最大页数
        );

        for (WorkspaceInfo workspace : workspaces) {
            System.out.println("工作空间: " + workspace.getName());
        }
    }

    /**
     * 示例11: 统计数据
     */
    private static void countData(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例11: 统计数据 ===");

        PageIterable<CategoryInfo> categories = client.category.iter(workspaceId);

        // 使用 Stream API 统计
        long totalCategories = StreamSupport.stream(categories.spliterator(), false).count();

        System.out.println("类别总数: " + totalCategories);
    }

    /**
     * 示例12: 批量处理
     */
    private static void batchProcessing(DocflowClient client, String workspaceId) {
        System.out.println("\n=== 示例12: 批量处理 ===");

        PageIterable<FileInfo> files = client.file.iter(workspaceId);

        int processedCount = 0;
        for (FileInfo file : files) {
            // 模拟处理逻辑
            processFile(file);
            processedCount++;

            // 每处理100个文件输出一次进度
            if (processedCount % 100 == 0) {
                System.out.println("已处理 " + processedCount + " 个文件");
            }
        }

        System.out.println("总共处理了 " + processedCount + " 个文件");
    }

    private static void processFile(FileInfo file) {
        // 实际的文件处理逻辑
        // ...
    }
}

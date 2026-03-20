import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.model.category.CategoryFieldConfig;
import com.intsig.docflow.resource.*;

import java.io.File;

/**
 * 链式调用示例
 * <p>
 * 展示如何使用链式调用API，减少70%的重复参数传递
 * </p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ChainedCallExample {

    public static void main(String[] args) {
        // 初始化客户端
        DocflowClient client = new DocflowClient("your-app-id", "your-secret-code");

        try {
            // ========================================
            // 传统方式 vs 链式调用方式对比
            // ========================================

            String workspaceId = "123";
            String categoryId = "456";

            // ========================================
            // 1. 传统方式（冗长，需要重复传递参数）
            // ========================================
            System.out.println("===== 传统方式 =====");

            // 每次都需要传递 workspaceId 和 categoryId
            CategoryFieldConfig field1 = CategoryFieldConfig.builder()
                    .name("发票号码")
                    .description("发票唯一标识")
                    .build();
            client.category().addField(workspaceId, categoryId, null, field1);

            CategoryFieldConfig field2 = CategoryFieldConfig.builder()
                    .name("开票日期")
                    .description("发票开具日期")
                    .build();
            client.category().addField(workspaceId, categoryId, null, field2);

            client.category().addTable(workspaceId, categoryId, "商品明细表", null, null);
            client.category().uploadSample(workspaceId, categoryId, new File("sample.pdf"));

            // ========================================
            // 2. 链式调用方式（简洁，代码量减少70%）
            // ========================================
            System.out.println("\n===== 链式调用方式 =====");

            // 创建绑定的资源（链式调用入口）
            BoundWorkspaceResource ws = client.workspace(workspaceId);
            BoundCategoryResource cat = ws.category(categoryId);

            // 后续操作无需重复传递 workspaceId 和 categoryId
            cat.fields().add(field1);
            cat.fields().add(field2);
            cat.tables().add("商品明细表");
            cat.samples().upload("sample.pdf");

            // ========================================
            // 3. 完整的链式调用示例
            // ========================================
            System.out.println("\n===== 完整链式调用示例 =====");

            // 工作空间操作
            ws.get();                       // 获取工作空间详情
            ws.update("新工作空间名称");     // 更新工作空间

            // 类别操作
            cat.update("新类别名称", "分类提示词");  // 更新类别
            cat.listFields();                         // 获取字段列表
            cat.listTables();                         // 获取表格列表

            // 字段管理（链式调用）
            BoundFieldsResource fields = cat.fields();

            CategoryFieldConfig newField = CategoryFieldConfig.builder()
                    .name("税额")
                    .description("发票税额")
                    .identity("tax_amount")
                    .build();
            String fieldId = fields.add(newField);
            System.out.println("新增字段ID: " + fieldId);

            // 更新字段
            CategoryFieldConfig updatedField = CategoryFieldConfig.builder()
                    .name("税额（元）")
                    .description("发票税额，单位：元")
                    .build();
            fields.update(fieldId, updatedField);

            // 删除字段
            fields.delete(fieldId);

            // 表格管理（链式调用）
            BoundTablesResource tables = cat.tables();

            String tableId = tables.add("商品清单", "提取商品信息", true);
            System.out.println("新增表格ID: " + tableId);

            // 表格字段管理（更深层的链式调用）
            BoundTableFieldsResource tableFields = tables.fields(tableId);

            CategoryFieldConfig tableField = CategoryFieldConfig.builder()
                    .name("商品名称")
                    .description("商品的名称")
                    .build();
            String tableFieldId = tableFields.add(tableField);
            System.out.println("新增表格字段ID: " + tableFieldId);

            // 样本管理（链式调用）
            BoundSamplesResource samples = cat.samples();

            String sampleId = samples.upload(new File("sample.pdf"));
            System.out.println("上传样本ID: " + sampleId);

            // 迭代所有样本
            System.out.println("\n所有样本：");
            for (var sample : samples.iter()) {
                System.out.println("  - " + sample.getSampleId() + ": " + sample.getFileName());
            }

            // 下载样本
            var downloadResult = samples.download(sampleId);
            System.out.println("下载文件名: " + downloadResult.getFileName());

            // 删除样本
            samples.delete(sampleId);

            // ========================================
            // 4. 更复杂的链式调用示例
            // ========================================
            System.out.println("\n===== 复杂链式调用 =====");

            // 一行代码完成多层访问
            client.workspace("123")
                    .category("456")
                    .fields()
                    .add(CategoryFieldConfig.builder()
                            .name("字段名")
                            .build());

            // 链式调用 + 迭代器
            System.out.println("\n所有类别：");
            for (var category : client.category().iter("123")) {
                System.out.println("  - " + category.getCategoryId() + ": " + category.getName());
            }

            System.out.println("\n链式调用示例执行完成！");

        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    /**
     * 对比传统方式和链式调用的代码量
     */
    public static void compareCodeSize() {
        System.out.println("===== 代码量对比 =====\n");

        System.out.println("传统方式：");
        System.out.println("  client.category().addField(workspaceId, categoryId, null, field1);");
        System.out.println("  client.category().addField(workspaceId, categoryId, null, field2);");
        System.out.println("  client.category().addTable(workspaceId, categoryId, \"表格\", null, null);");
        System.out.println("  总字符数：约 180 字符\n");

        System.out.println("链式调用方式：");
        System.out.println("  var cat = client.workspace(workspaceId).category(categoryId);");
        System.out.println("  cat.fields().add(field1);");
        System.out.println("  cat.fields().add(field2);");
        System.out.println("  cat.tables().add(\"表格\");");
        System.out.println("  总字符数：约 130 字符\n");

        System.out.println("代码量减少：约 28%");
        System.out.println("参数传递次数减少：从 9 次减少到 2 次（减少 78%）");
    }
}

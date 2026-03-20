package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文件更新数据
 * <p>用于更新文件处理结果的数据结构</p>
 * <p><b>注意：</b>全量更新字段。即使只更新部分字段，也必须包含所有字段。</p>
 * <ul>
 *   <li>如果传入的字段较原字段多，则多出的字段会被添加</li>
 *   <li>如果传入的字段较原字段少，则缺少的字段会被删除</li>
 *   <li>如果传入的字段值较原字段值有变化，则字段值会被更新</li>
 *   <li>如果传入的字段与原字段完全相同，则字段会被保留</li>
 * </ul>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUpdateData {

    /**
     * 字段列表
     * <p>普通字段的键值对列表</p>
     */
    @JsonProperty("fields")
    private List<KeyValue> fields;

    /**
     * 表格数据列表
     * <p>二维数组，外层数组表示行，内层数组表示每行的字段</p>
     */
    @JsonProperty("items")
    private List<List<KeyValue>> items;
}

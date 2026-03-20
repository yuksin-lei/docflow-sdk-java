package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 类别字段信息
 * <p>
 * 继承自 CategoryFieldConfig，并添加字段ID
 * </p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryField extends CategoryFieldConfig {

    /**
     * 字段ID
     */
    @JsonProperty("id")
    private String id;
}

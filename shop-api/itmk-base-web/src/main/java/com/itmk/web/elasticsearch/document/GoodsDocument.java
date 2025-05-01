package com.itmk.web.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "shop_goods")
public class GoodsDocument {

    @Id
    private Long goodsId;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String goodsName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String goodsDesc;

    @Field(type = FieldType.Double)
    private BigDecimal goodsPrice;

    @Field(type = FieldType.Text)
    private String userName;

    @Field(type = FieldType.Text)
    private String phone;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String address;

    @Field(type = FieldType.Text)
    private String image;

    @Field(type = FieldType.Text)
    private String wxNum;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private String sellStatus;

    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @Field(type = FieldType.Keyword)
    private String setIndex;

    @Field(type = FieldType.Keyword)
    private String deleteStatus;
}
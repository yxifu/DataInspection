package com.yxifu.datainspection.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yxifu
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("di_setting")
public class Setting implements Serializable {

    private static final long serialVersionUID = 112132132L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String code;
    private String value;
    private Integer status;
    private String createTime;
    private String lastUpdateTime;
    private String remark;

}

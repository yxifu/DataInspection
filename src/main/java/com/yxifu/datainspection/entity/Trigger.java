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
 * @since 2020-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("di_trigger")
public class Trigger implements Serializable {

    private static final long serialVersionUID = 12344875L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer triggerType;
    private String corn;
    private Integer interval;
    private String remark;
    private Integer status;
    private Integer groupId;
    private String createTime;
    private String lastUpdateTime;
}

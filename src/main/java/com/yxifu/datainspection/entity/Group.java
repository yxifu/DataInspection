package com.yxifu.datainspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

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
@TableName("di_group")
public class Group implements Serializable {

    private static final long serialVersionUID = 123423L;

    @TableId(value = "group_id", type = IdType.AUTO)
    private Integer groupId;
    private String groupCode;
    private String name;
    private Integer status;
    private String remark;
    private Integer isSend;
    private Integer triggerCondition;
    private Integer isSendEmail;
    private String emails;
    private String createTime;
    private String lastUpdateTime;
}

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
@TableName("di_item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1343434L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer connId;
    private String title;
    private String sql;
    private Integer status;
    private String createTime;
    private String lastUpdateTime;
    private String remark;
    private Integer groupId;
    private Integer successType;
}

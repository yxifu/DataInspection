package com.yxifu.datainspection.entity;

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
 * @since 2020-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("di_conn")
public class Conn {

    private static final long serialVersionUID = 10785601L;

    @TableId(value = "conn_id", type = IdType.AUTO)
    private Integer connId;
    private String name;
    private String type;
    private String connString;
    private String username;
    private String password;
    private String remark;
    // private LocalDateTime createTime;
    private String createTime;
    private String lastUpdateTime;
    private Integer status;
}

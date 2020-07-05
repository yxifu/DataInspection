package com.yxifu.datainspection.bean;

import com.yxifu.datainspection.entity.Conn;
import com.yxifu.datainspection.entity.Item;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yxifu
 * @date 2020/06/15
 **/
@Data
public class ItemResultBean {
    Conn conn;
    Item item;
    List<Map<String ,Object>> data;
    Exception exception;
}

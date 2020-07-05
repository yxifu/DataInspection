package com.yxifu.datainspection.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxifu.datainspection.bean.GroupResultBean;
import com.yxifu.datainspection.bean.ItemResultBean;
import com.yxifu.datainspection.entity.Conn;
import com.yxifu.datainspection.entity.Group;
import com.yxifu.datainspection.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;

/**
 * @author yxifu
 * @date 2020/06/15
 **/
@Component
public class GroupResultService {

    @Autowired
    IConnService iConnService;
    @Autowired
    IItemService iItemService;

    @Autowired
    DBService dbService;

    @Autowired
    EmailService emailService;

    public static Hashtable<Integer,Conn> connTable = new Hashtable<>();
    public static Hashtable<Integer,List<Item>> itemTable = new Hashtable<>();

    public void exec(Group group)
    {
        boolean hasTrue = false;
        boolean hasFalse = false;
        GroupResultBean executeResultBean =getGroupResultBean (group);

        if(executeResultBean.canSendEmail()){
            emailService.SendByExecuteResultBean(group,executeResultBean);
        }
    }

    //获取查询结果
    public GroupResultBean getGroupResultBean(Group group) {
        GroupResultBean executeResultBean = new GroupResultBean();
        executeResultBean.setGroup(group);
        List<Item> itemList =getItemList(group.getGroupId());

        for (Item item:itemList) {
            Conn conn = getConn(item.getConnId());
            ItemResultBean itemResultBean = this.dbService.getItemResultBean(conn,item);
            executeResultBean.add(itemResultBean);
        }
        return executeResultBean;
    }

    private Conn getConn(int connId){
        if(connTable.containsKey(connId)){
            return connTable.get(connId);
        } else {
            Conn conn = iConnService.getById(connId);
            //connTable.put(connId,conn);
            return conn;
        }
    }
    private List<Item> getItemList(int groupId){
        if(itemTable.containsKey(groupId)){
            return itemTable.get(groupId);
        } else {
            QueryWrapper<Item> queryWapper = new QueryWrapper<>();
            queryWapper.lambda().eq(Item::getGroupId,groupId)
            .eq(Item::getStatus,1);
            List<Item> itemList = this.iItemService.list(queryWapper);
            //itemTable.put(groupId,itemList);
            return itemList;
        }
    }

}

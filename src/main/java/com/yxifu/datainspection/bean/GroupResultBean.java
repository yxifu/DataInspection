package com.yxifu.datainspection.bean;

import com.yxifu.datainspection.entity.Group;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxifu
 * @date 2020/06/15
 **/
@Data
public class GroupResultBean {
    Group group;
    List<ItemResultBean> itemResultList = new ArrayList<>();
    public boolean add(ItemResultBean  itemResultBean){
        return itemResultList.add(itemResultBean);
    }

    public boolean canSendEmail() {
        if(group.getIsSendEmail()>0){
            if(group.getTriggerCondition()==1) {
                return true;
            } else if(group.getTriggerCondition()==0) {
                for (ItemResultBean itemResultBean: itemResultList) {
                    if(itemResultBean.getException()!=null){
                        return true;
                    } else if(itemResultBean.getData()!=null && itemResultBean.getData().size()>0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

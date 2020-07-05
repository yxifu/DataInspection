package com.yxifu.datainspection.service.impl;

import com.yxifu.datainspection.entity.Item;
import com.yxifu.datainspection.mapper.ItemMapper;
import com.yxifu.datainspection.service.IItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yxifu
 * @since 2020-06-14
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

}

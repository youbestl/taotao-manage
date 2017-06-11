package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {
    
    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    public boolean saveItem(Item item,String desc,String itemParams){
        item.setStatus(1);
        item.setId(null);//出于安全考虑，防止有人恶意出入id参数，将id赋值为Null
        Integer count1 = super.save(item);
        
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = itemDescService.save(itemDesc);
        
        //保存规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = this.itemParamItemService.save(itemParamItem);
        
        return count1==1 && count2==1 && count3==1;
        
    }
    
    public EasyUIResult queryItemList(Integer page,Integer rows){
        
        //设置分页数据
        PageHelper.startPage(page, rows);
        
        Example example = new Example(Item.class);
        example.setOrderByClause("created DESC");
        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(items);
        
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
    
    public boolean updateItem(Item item,String desc,String itemParams){
        item.setStatus(null);
        Integer count1 = super.updateSelective(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        Integer count2 = this.itemDescService.updateSelective(itemDesc);
        Integer count3 = this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
        return count1.intValue()>0 && count2.intValue()>0 && count3.intValue()>0;
    }
}

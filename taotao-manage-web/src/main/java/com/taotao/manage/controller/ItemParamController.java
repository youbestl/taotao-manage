package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController  {
    
    @Autowired
    private ItemParamService itemParamService;
    /**
     * 根据商品类目查询商品规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId")Long itemCatId){
        ItemParam record = new ItemParam();
        record.setItemCatId(itemCatId);
        try {
            ItemParam itemParam = this.itemParamService.queryOne(record);
            if(null==itemParam){
                //404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(itemParam);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 新增商品规格参数模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId")Long itemCatId
            ,@RequestParam("paramData")String paramData){
        
        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            itemParam.setId(null);
            itemParam.setParamData(paramData);
            this.itemParamService.save(itemParam);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

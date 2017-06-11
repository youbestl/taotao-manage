package com.taotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCat.class);
    @Autowired
    private ItemCatService itemCatService;
    /**
     *  查询商品类目列表
     * @return
     */
    
    @RequestMapping(method=RequestMethod.GET)                           //初始时并没有id的参数，因此需要设置初始值为0
    public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value="id",defaultValue="0")Long pid){
        try {
            ItemCat record = new ItemCat();
            record.setParentId(pid);
            List<ItemCat> list = this.itemCatService.queryListByWhere(record);
            if(null==list || list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCat> queryItemCatById(@RequestParam(value="cid")Long id){
        
        ItemCat itemCat = this.itemCatService.queryById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemCat);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("查询类目异常 itemCat = " + itemCat,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        
    }*/
    
    

}

package com.taotao.manage.controller.api;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("api/item/param/item")
@Controller
public class ApiItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 根据商品id查询商品规格参数
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryItemById(@PathVariable("itemId")Long itemId){

        try {
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParam = this.itemParamItemService.queryOne(record);
            if(itemParam==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(itemParam);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService ItemDescService;
    
    /**
     * 新增商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("新增商品,item = {}",item);
            }
            if (item.getTitle() == null) {//TODO 验证未完成，待优化
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            boolean res = this.itemService.saveItem(item, desc,itemParams);
            if (!res) {
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("新增商品失败,item = {}",item);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品成功,item = {}",item);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("新增商品异常,itme = "+item,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
    
    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value="page",defaultValue="1")Integer page
            ,@RequestParam(value="rows",defaultValue="30")Integer rows){
        EasyUIResult items = itemService.queryItemList(page, rows);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(items);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("查询商品出错 page = " + page,"rows = " + rows,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("编辑商品,item = {}",item);
            }
            if (item.getTitle() == null) {//TODO 验证未完成，待优化
                //参数有误 400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            boolean res = this.itemService.updateItem(item, desc,itemParams);
            if (!res) {
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("编辑商品失败,item = {}",item);
                }
                //500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("编辑商品成功,item = {}",item);
            }
            //204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("编辑商品异常,itme = "+item,e);
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
    
}

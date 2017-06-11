package com.taotao.manage.controller.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ItemCatService itemCatService;
    
    /*@RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<String> queryItemCatList(@RequestParam(value="callback",required=false)String callback){
        try {
            ItemCatResult itemCatResult = itemCatService.queryAllToTree();
            String json =  MAPPER.writeValueAsString(itemCatResult);
            //isEmpty 判断空格为false isBlank 判断空格为空
            if(StringUtils.isEmpty(callback)){
                return ResponseEntity.ok(json);
            }
            return ResponseEntity.ok(callback +"(" + json + ")");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
    
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatList(){
        try {
            ItemCatResult ItemCatResult = itemCatService.queryAllToTree();
            //isEmpty 判断空格为false isBlank 判断空格为空
                return ResponseEntity.ok(ItemCatResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

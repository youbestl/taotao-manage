package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEM_CAT_API"; //命名规则：项目名_模块名_业务名

    private static final int REDIS_TIME = 60 * 60 * 60 * 24 * 30 * 3;
    /**
     * 使用spring 4.0新特性泛型注入优化代码 泛型注入：根据泛型的类型到spring 容器中查找这个类型的Mapper并注入
     * 虽然编码时并未用到ItemCatMapper,但是仍不可删除,注入时spring需要改对象
     */

    /*
     * @Autowired private ItemCatMapper itemCatMapper;
     */

    /*
     * public List<ItemCat> queryItemCatListByParentId(Long pid){ ItemCat record = new ItemCat();
     * record.setParentId(pid); return itemCatMapper.select(record); }
     */

    /*
     * @Override public Mapper<ItemCat> getMapper() { // TODO Auto-generated method stub return
     * itemCatMapper; }
     */

    @Autowired
    private RedisService redisService;

    public ItemCatResult queryAllToTree() {
        ItemCatResult result = new ItemCatResult();
        String cacheData = null;

        try {
            //缓存逻辑不能影响，原有业务逻辑
            //先从缓存中命中，如果命中就从缓存中拿，没有命中就继续操作
            cacheData = this.redisService.get(REDIS_KEY);
            //命中
            if (StringUtils.isNotEmpty(cacheData)) {
                //将字符串反序列化为对象
                return MAPPER.readValue(cacheData, ItemCatResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //查出全部数据，并放入内存中
        List<ItemCat> cats = super.queryAll();

        // 转储Map数据
        HashMap<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setNname("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }
            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setNname(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }
        //将查到的数据放入缓存中，将ItemCatResult 对象 序列化为 字符串
        try {
            this.redisService.set(REDIS_KEY, MAPPER.writeValueAsString(result), REDIS_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

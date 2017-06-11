package com.taotao.manage.service;

import com.taotao.manage.pojo.ContentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JARVIS on 2017.6.11.
 */
@Service
public class ContentCategoryService extends BaseService<ContentCategory> {
    public void saveContentCategory(ContentCategory contentCategory) {
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        super.save(contentCategory);
        //根据内容分类的pid查询分类判断isParent属性是否为true,如果不为true,则改为true
        if (!contentCategory.getIsParent()) {
            ContentCategory category = super.queryById(contentCategory.getParentId());
            category.setIsParent(true);
            super.update(category);
        }
    }

    public void deleteAll(ContentCategory contentCategory) {
        List<Object> ids = new ArrayList<>();
        ids.add(contentCategory.getId());
        //递归查找该节点下，所有子节点
        this.findAllSubNode(ids,contentCategory.getId());
        this.deleteByIds(ids, ContentCategory.class, "id");
        //判断该节点是否还有兄弟节点如果没有，将isParent修改为false
        ContentCategory record = new ContentCategory();
        record.setParentId(contentCategory.getParentId());
        List<ContentCategory> list = this.queryListByWhere(record);
        if (null == list || list.isEmpty()) {
            ContentCategory parent = this.queryById(contentCategory.getParentId());
            parent.setIsParent(false);
            this.updateSelective(parent);
        }
    }

    private void findAllSubNode(List<Object> ids, Long id) {
        ContentCategory record = new ContentCategory();
        record.setParentId(id);
        List<ContentCategory> list = this.queryListByWhere(record);
        for (ContentCategory category : list) {
            ids.add(category.getId());
            //判断该节点是否为父节点，如果是，继续调用该方法，查找子节点
            if (category.getIsParent()) {
                //开始递归
                this.findAllSubNode(ids,category.getId());
            }
        }

    }
}

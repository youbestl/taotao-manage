package com.taotao.manage.mapper;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.pojo.Content;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by JARVIS on 2017.6.11.
 */
@Repository
public interface ContentMapper extends Mapper<Content> {
    /**
     * 根据categoryId查询内容列表，并按更新时间倒序排序
     * @param categoryId
     * @return
     */
    List<Content> queryContentList(Long categoryId);
}

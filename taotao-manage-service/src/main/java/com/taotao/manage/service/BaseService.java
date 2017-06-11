package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {
    
    @Autowired
    private Mapper<T> mapper;
    
   
    //public abstract Mapper<T> getMapper();
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询所有
     * @return
     */
    public List<T> queryAll(){
        return this.mapper.select(null);
    }
    
    /**
     * 根据条件查询一条数据，如果查询结果为多条，则会抛异常
     * @param record
     * @return
     */
    public T queryOne(T record){
        return this.mapper.selectOne(record);
    }
    
    /**
     * 根据条件查询多条数据
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record){
        return this.mapper.select(record);
    }
    
    /**
     * 根据条件进行分页查询
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record,Integer page,Integer rows){
        PageHelper.startPage(page, rows);
        List<T> list = this.mapper.select(record);
        return new PageInfo<T>(list);
    }
    
    /**
     * 新增对象
     * @param t
     * @return
     */
    public Integer save(T t){
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insert(t);
    }
    
    /**
     * 插入不为null的属性
     * @param t
     * @return
     */
    public Integer saveSelective(T t){
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insertSelective(t);
    }
    
    /**
     * 更新对象
     * @param t
     * @return
     */
    public Integer update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }
    
    /**
     * 更新不为null的属性
     * @param t
     * @return
     */
    public Integer updateSelective(T t){
        t.setUpdated(new Date());
        t.setCreated(null); //不允许用户自己更新创建时间
        return this.mapper.updateByPrimaryKeySelective(t);
    }
    
    /**
     * 物理删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 批量删除数据
     * @param ids
     * @param clazz
     * @param primaryKey
     * @return
     */
    public Integer deleteByIds(List<Object> ids,Class<T> clazz,String property){
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }
    
    /**
     * 根据条件删除数据
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return this.mapper.delete(record);
    }
    
}

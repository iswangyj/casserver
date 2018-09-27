package org.jasig.cas.dao;


import org.jasig.cas.entity.SppUser;

public interface SppUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SppUser record);

    int insertSelective(SppUser record);

    SppUser selectByPrimaryKey(Integer id);

    SppUser selectByAccount(String id);

    int updateByPrimaryKeySelective(SppUser record);

    int updateByPrimaryKey(SppUser record);
}
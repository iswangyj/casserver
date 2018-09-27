package org.jasig.cas.dao;


import org.jasig.cas.entity.Provider;

import java.util.List;

/**
 * Created by daisygao on 2018/9/5.
 */
public interface ProviderMapper {
    int insertSelective(Provider record);

    int updateByPrimaryKeySelective(Provider record);

    Provider selectByPrimaryKey(Integer id);

    List<Provider> selectProviderList();
}

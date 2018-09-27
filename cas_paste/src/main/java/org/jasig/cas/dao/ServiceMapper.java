package org.jasig.cas.dao;


import org.jasig.cas.entity.Service;

import java.util.List;

/**
 * @author SxL
 * Created on 7/15/2018 3:43 PM.
 */

public interface ServiceMapper {
    /**
     * 根据主键查询子服务
     * @param id 主键
     * @return 子服务
     */
    Service selectByPrimaryKey(Integer id);

    /**
     * 查询所有服务
     * @return 服务列表
     */
    List<Service> selectServiceList();
}
package com.mysiteforme.admin.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.entity.Role;
import com.mysiteforme.admin.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 */
public interface OrderDao extends BaseMapper<Order> {
	Order selectOrderByMap(Map<String, Object> map);
}
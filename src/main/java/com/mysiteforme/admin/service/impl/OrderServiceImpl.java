package com.mysiteforme.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.mysiteforme.admin.dao.OrderDao;
import com.mysiteforme.admin.dao.UserDao;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.entity.Role;
import com.mysiteforme.admin.entity.User;
import com.mysiteforme.admin.service.OrderService;
import com.mysiteforme.admin.service.UserService;
import com.mysiteforme.admin.util.ToolUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangl
 * @since 2017-10-31
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

	/* 这里caching不能添加put 因为添加了总会执行该方法
	 * @see com.mysiteforme.service.OrderService#findUserByOrderNo(java.lang.String)
	 */
	@Cacheable(value = "order", key = "'order_no_'+#orderNo",unless = "#result == null")
	@Override
	public Order findOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		Map<String,Object> map = Maps.newHashMap();
		map.put("orderNo", orderNo);
		Order u = baseMapper.selectOrderByMap(map);
		return u;
	}


	@Cacheable(value = "order",key="'order_id_'+T(String).valueOf(#id)",unless = "#result == null")
	@Override
	public Order findOrderById(Long id) {
		// TODO Auto-generated method stub
		Map<String,Object> map = Maps.newHashMap();
		map.put("id", id);
		return baseMapper.selectOrderByMap(map);
	}

	@Override
	@Caching(put = {
			@CachePut(value = "order", key = "'order_id_'+T(String).valueOf(#result.id)",condition = "#result.id != null and #result.id != 0"),
			@CachePut(value = "order", key = "'order_no_'+#order.orderNo", condition = "#order.orderNo !=null and #order.orderNo != ''")
	})
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Order saveOrder(Order order) {
		baseMapper.insert(order);
		return findOrderById(order.getId());
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "order", key = "'order_id_'+T(String).valueOf(#order.id)",condition = "#order.id != null and #order.id != 0"),
			@CacheEvict(value = "order", key = "'order_no_'+#order.orderNo", condition = "#order.orderNo !=null and #order.orderNo != ''")
	})
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Order updateOrder(Order order) {
		baseMapper.updateById(order);
		return order;
	}

	@Override
	public int orderCount(String param) {
		EntityWrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("order_no",param);
		int count = baseMapper.selectCount(wrapper);
		return count;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	@Caching(evict = {
			@CacheEvict(value = "order", key = "'order_id_'+T(String).valueOf(#order.id)",condition = "#order.id != null and #order.id != 0"),
			@CacheEvict(value = "order", key = "'order_id_'+#order.orderNo", condition = "#order.orderNo !=null and #order.orderNo != ''")
	})
	public void deleteOrder(Order order) {
		order.setDelFlag(true);
		order.updateById();
	}
}

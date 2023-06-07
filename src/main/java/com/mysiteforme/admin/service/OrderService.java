package com.mysiteforme.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.mysiteforme.admin.entity.Order;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface OrderService extends IService<Order> {

	Order findOrderByOrderNo(String orderNo);

	Order findOrderById(Long id);

	Order saveOrder(Order order);

	Order updateOrder(Order order);

	int orderCount(String param);

	void deleteOrder(Order order);
}

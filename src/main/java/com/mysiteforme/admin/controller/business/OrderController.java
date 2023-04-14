package com.mysiteforme.admin.controller.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mysiteforme.admin.annotation.SysLog;
import com.mysiteforme.admin.base.BaseController;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/11/21.
 *
 */
@Controller
@RequestMapping("biz/order")
public class OrderController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("list")
    @SysLog("跳转订单列表页面")
    public String list(){
        return "business/order/list";
    }

    @RequiresPermissions("biz:order:list")
    @PostMapping("list")
    @ResponseBody
    public LayerData<Order> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "limit",defaultValue = "10")Integer limit,
                                ServletRequest request){
        Map map = WebUtils.getParametersStartingWith(request, "s_");
        LayerData<Order> orderLayerData = new LayerData<>();
        EntityWrapper<Order> orderEntityWrapper = new EntityWrapper<>();
        if(!map.isEmpty()){
            String keys = (String) map.get("key");
            if(StringUtils.isNotBlank(keys)) {
                orderEntityWrapper.like("order_no", keys);
            }
        }
        Page<Order> orderPage = orderService.selectPage(new Page<>(page,limit),orderEntityWrapper);
        orderLayerData.setCount(orderPage.getTotal());
        orderLayerData.setData(orderPage.getRecords());
        return  orderLayerData;
    }

    @GetMapping("add")
    public String add(Model model){
        return "business/order/add";
    }

    @RequiresPermissions("biz:order:add")
    @PostMapping("add")
    @ResponseBody
    @SysLog("保存新增订单数据")
    public RestResponse add(@RequestBody Order order){
        if(StringUtils.isBlank(order.getOrderNo())){
            return RestResponse.failure("订单编号不能为空");
        }
        // 设置是否删除，下单时间、订单状态、上下文id
        order.setDelFlag(Boolean.FALSE);
        order.setPlaceOrderTime(new Date());
        order.setStatus(1);
        order.setContextId(UUIDUtil.randomUUID());
        orderService.saveOrder(order);
        if(order.getId() == null || order.getId() == 0){
            return RestResponse.failure("保存订单信息出错");
        }
        return RestResponse.success();
    }

    @GetMapping("edit")
    public String edit(Long id,Model model){
        Order order = orderService.findOrderById(id);

        model.addAttribute("order",order);
        return "business/order/edit";
    }

    @RequiresPermissions("biz:order:edit")
    @PostMapping("edit")
    @ResponseBody
    @SysLog("保存系统订单编辑数据")
    public RestResponse edit(@RequestBody Order order){
        if(order.getId() == 0 || order.getId() == null){
            return RestResponse.failure("订单ID不能为空");
        }
        if(StringUtils.isBlank(order.getOrderNo())){
            return RestResponse.failure("订单编号不能为空");
        }

        orderService.updateOrder(order);
        return RestResponse.success();
    }

    @RequiresPermissions("biz:order:delete")
    @PostMapping("delete")
    @ResponseBody
    @SysLog("删除系统订单数据(单个)")
    public RestResponse delete(@RequestParam(value = "id",required = false)Long id){
        if(id == null || id == 0 || id == 1){
            return RestResponse.failure("参数错误");
        }
        Order order = orderService.findOrderById(id);
        if(order == null){
            return RestResponse.failure("订单不存在");
        }
        orderService.deleteOrder(order);
        return RestResponse.success();
    }

    @RequiresPermissions("biz:order:delete")
    @PostMapping("deleteSome")
    @ResponseBody
    @SysLog("删除系统订单数据(多个)")
    public RestResponse deleteSome(@RequestBody List<Order> orders){
        if(orders == null || orders.size()==0){
            return RestResponse.failure("请选择需要删除的订单");
        }
        for (Order o : orders){
            orderService.deleteOrder(o);
        }
        return RestResponse.success();
    }


    @GetMapping("detail")
    public String getOrder(Long id, Model model){
        Order order = orderService.findOrderById(id);
        model.addAttribute("order",order);
        return "business/order/detail";
    }


}

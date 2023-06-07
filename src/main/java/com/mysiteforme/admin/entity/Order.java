package com.mysiteforme.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysiteforme.admin.base.DataEntity;
import com.mysiteforme.admin.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 */
@Data
@TableName("biz_order")
public class Order extends DataEntity<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@TableField("order_no")
	private String orderNo;

	/**
	 * ott订单编号
	 */
	@TableField("ott_order_no")
	private String ottOrderNo;

    /**
     * 下单时间
     */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "place_order_time")
	private Date placeOrderTime;

	/**
	 * 激活账号
	 */
	@TableField(value = "account")
	private String account;

	/**
	 * 产品编码
	 */
	@TableField(value = "product_no")
	private String productNo;

	/**
	 * 订单金额
	 */
	@TableField(value = "price")
	private BigDecimal price;

	/**
	 * 订单状态 1. 草稿、2. 微店已下单、3. 微店已支付、4. 已同步至爱奇艺、5. 爱奇艺已激活、-1.激活失败
	 * 11. 订单重复、 12. 产品信息不存在 13. 激活账号为空
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 状态文本
	 */
	@TableField(exist = false)
	private String statusText;

	public String getStatusText() {
		return OrderStatus.getName(this.status);
	}

    /**
     * 上下文id
     */
	@TableField(value = "context_id")
	private String contextId;

	/**
	 * 请求内容
	 */
	@TableField(value = "request_content")
	private String requestContent;

	/**
	public void setPlaceOrderTime(Date placeOrderTime){
		this.placeOrderTime = placeOrderTime;
	}
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getPlaceOrderTime() {
		return placeOrderTime;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return account;
	}

	public void setPrice(BigDecimal price){
		this.price = price;
	}

	public BigDecimal getPrice(){
		return price;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}

	public void setContextId(String contextId){
		this.contextId = contextId;
	}

	public String getContextId(){
		return contextId;
	}

	public void setRequestContent(String requestContent){
		this.requestContent = requestContent;
	}

	public String getRequestContent(){
		return requestContent;
	}
*/

}

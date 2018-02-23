package com.rs.wxmgr.action.entity;

import java.util.HashMap;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 封装tableview发送的请求
 * @author sun_zeming
 *
 */
public class TXTableRequest {

	/**
	 * tableview一页的行数
	 */
	private Integer pagesize;
	/**
	 * tableview当前的页码
	 */
	private Integer pageindex;
	/**
	 * tableview的排序项
	 */
	private String orderitem;
	/**
	 * tableview的排序方式(asc/desc)
	 */
	private String order;
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getPageindex() {
		return pageindex;
	}
	public void setPageindex(Integer pageindex) {
		this.pageindex = pageindex;
	}
	public String getOrderitem() {
		return orderitem;
	}
	public void setOrderitem(String orderitem) {
		this.orderitem = orderitem;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * 通过tableview发送的请求创建mybatis的分页插件对象
	 * @return
	 */
	public PageBounds createPageBounds() {
		PageBounds pageBounds = new PageBounds(pageindex,pagesize);
		if(order != null && orderitem != null) {
			pageBounds.setOrders(Order.formString(orderitem+"."+order));		
		}
		return pageBounds;
	}
	@Override
	public String toString() {
		return "TXPageRequest [pagesize=" + pagesize + ", pageindex=" + pageindex + ", orderitem="
				+ orderitem + ", order=" + order + "]";
	}
	public static TXTableRequest GetFromRequest(HashMap<String,Object> data) {
		TXTableRequest tableRequest = new TXTableRequest();
		if(data.get("pagesize")!=null) {
			tableRequest.setPagesize(Integer.valueOf(data.get("pagesize").toString()));
		}
		if(data.get("pageindex")!=null) {
			tableRequest.setPageindex(Integer.valueOf(data.get("pageindex").toString()));
		}
		if(data.get("orderitem")!=null) {
			tableRequest.setOrderitem(data.get("orderitem").toString());
		}
		if(data.get("order")!=null) {
			tableRequest.setOrder(data.get("order").toString());
		}
		return tableRequest;
	}
	
}

package com.project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.Controllor.DeliveryManControllor;
import com.project.Controllor.DistanceControllor;
import com.project.Controllor.OfferControllor;
import com.project.Controllor.OrderControllor;
import com.project.Entity.Auditable;
import com.project.Entity.Cart;
import com.project.Entity.DeliveryMan;
import com.project.Entity.Orders;
import com.project.Repository.CartRepository;
import com.project.Response.APIResponse;

@Service
public class CartService extends Auditable{
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private OrderControllor orderControllor;
	@Autowired
	private DistanceControllor distanceControllor;
	@Autowired
	private DeliveryManControllor deliveryManControllor;

	@Autowired
	private OfferControllor offerControllor;
	
	private Orders order = new Orders();
	DeliveryMan deliveryMan= new DeliveryMan();

	public void calculate(Cart cart) {
		String userName="Arivu";
		String userId="1";
		double gst=0.003;
		double subtotal=cart.getSubTotal();	
		double deliveryCharge=distanceControllor.getChargeByLocation(cart.getFinalLocation());
		deliveryMan = deliveryManControllor.getDetails(cart.getFinalLocation());
		double price=subtotal*gst;
		double total=price+deliveryCharge+subtotal;
		String grd=offerControllor.grade(total);
		double value=offerControllor.value(grd);
		total=total - (total*value)/100;
		
		order.setCreditedOn(getCreditedOn());
		order.setUpdatedOn(getUpdatedOn());
		order.setPrice(total);
		order.setDeliveryCharge(deliveryCharge);
		order.setDeliveryManId(deliveryMan.getDeliveryManId());
		order.setDeliveryPhoneNo(deliveryMan.getDeliveryManPhoneNo());
		order.setPaymentMode(order.getPaymentMode());
		order.setOrderStatus(order.getOrderStatus());
		order.setUserId(userId);
		order.setUserName(userName);
		order.setStatus(order.getStatus());
		orderControllor.addOrder(order);	
	}
	
	public APIResponse addCart(Cart detail) {
		APIResponse apiResponse=new APIResponse();
		apiResponse.setData(cartRepository.save(detail));
		System.out.println(detail.getSubTotal());
		calculate(detail);
		System.out.println("after calculating");
		return apiResponse;	
	}
}
/*return total;		
System.out.println(total);
order.setPrice(total);
order.setDeliveryCharge(50);
System.out.println("before saving order repository");
//orderService.saveOrder(order,total);
orderRepository.save(order );*/
package com.driver;

import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {



    private HashMap<String ,Order> orderHm;
    private HashMap<String ,DeliveryPartner> deliveryPartnerHm;
    private HashMap<String ,String> orderPartnerHm;
    private HashMap<String , List<String>> partnerOrdersHm;

    public OrderRepository(){
        this.orderHm = new HashMap<>();
        this.deliveryPartnerHm = new HashMap<>();
        this.orderPartnerHm = new HashMap<>();
        this.partnerOrdersHm = new HashMap<>();
    }

    public void addOrder(Order order){
        orderHm.put(order.getId(),order);
    }

    public void addPartner(String partnerId){
        deliveryPartnerHm.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        if(orderHm.containsKey(orderId) && deliveryPartnerHm.containsKey(partnerId)){
            orderPartnerHm.put(orderId,partnerId);
        }

        List<String> currentOrders = new ArrayList<>();

        if(partnerOrdersHm.containsKey(partnerId)){
            currentOrders = partnerOrdersHm.get(partnerId);
        }

        currentOrders.add(orderId);
        partnerOrdersHm.put(partnerId,currentOrders);

        //no of orders of partner
        DeliveryPartner deliveryPartner = deliveryPartnerHm.get(partnerId);
        deliveryPartner.setNumberOfOrders(currentOrders.size());
    }

    public Order getOrderById(String orderId){
        return orderHm.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerHm.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return partnerOrdersHm.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return partnerOrdersHm.get(partnerId);
    }

    public List<String> getAllOrders(){
        List<String> allOrders = new ArrayList<>();
        for(String order : orderHm.keySet()){
            allOrders.add(order);
        }
        return allOrders;
    }

    public int getCountOfUnassignedOrders(){
        return orderHm.size() - orderPartnerHm.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId){
        int count = 0;
        List<String> orders = partnerOrdersHm.get(partnerId);

        for(String orderId : orders){
            int deliveryTime =  orderHm.get(orderId).getDeliveryTime();
            if(deliveryTime > time){
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int lastTime = 0;
        List<String> orders = partnerOrdersHm.get(partnerId);
        for(String orderId : orders){
            int currentTime = orderHm.get(orderId).getDeliveryTime();
            lastTime = Math.max(lastTime,currentTime);
        }
        return lastTime;
    }

    public void deletePartnerById(String partnerId){
        deliveryPartnerHm.remove(partnerId);

        List<String> ordersList = partnerOrdersHm.get(partnerId);
        partnerOrdersHm.remove(partnerId);

        for(String order : ordersList){
            orderPartnerHm.remove(order);
        }
    }

    public void deleteOrderById(String orderId){
        orderHm.remove(orderId);

        String partnerId = orderPartnerHm.get(orderId);
        orderPartnerHm.remove(orderId);

        partnerOrdersHm.get(partnerId).remove(orderId);

        deliveryPartnerHm.get(partnerId).setNumberOfOrders(partnerOrdersHm.get(partnerId).size());


    }
}

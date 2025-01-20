/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Order_Item;
import entity.Order_Status;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ravindu lakmina
 */
@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        HttpSession httpSession = request.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String city_id = requestJsonObject.get("city_id").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String postal_code = requestJsonObject.get("postal_code").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();
        
        if (httpSession.getAttribute("user") != null) {
            
            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            
            User user = (User) criteria1.uniqueResult();
            
            if (isCurrentAddress) {
 
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);
                
                if (criteria2.list().isEmpty()) {
                 
                    responseJsonObject.addProperty("message", "Current address not found. Please create a new address.");
                    
                } else {
                    
                    Address address = (Address) criteria2.list().get(0);
 
                    saveOrders(session, transaction, user, address, responseJsonObject);
                    
                }
                
            } else { 

                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill first name");
                    
                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill last name");
                    
                } else if (!Validation.isInteger(city_id)) {
                    responseJsonObject.addProperty("message", "Invalid City");
                    
                } else { 
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(city_id)));
                    
                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid City Selected");
                    } else { 

                        City city = (City) criteria3.list().get(0);
                        
                        if (address1.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill address line 1");
                            
                        } else if (address2.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill address line 2");
                            
                        } else if (postal_code.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill postal code");
                            
                        } else if (postal_code.length() != 5) {
                            responseJsonObject.addProperty("message", "Invalid postal code");
                            
                        } else if (!Validation.isInteger(postal_code)) {
                            responseJsonObject.addProperty("message", "Invalid postal code");
                            
                        } else if (mobile.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill mobile");
                            
                        } else if (!Validation.isMobileNumberValid(mobile)) {
                            responseJsonObject.addProperty("message", "Invalid mobile number");
                            
                        } else { 

                            Address address = new Address();
                            address.setFirst_name(first_name);
                            address.setCity(city);
                            address.setLast_name(last_name);
                            address.setLine1(address1);
                            address.setLine2(address2);
                            address.setMobile(mobile);
                            address.setPostal_code(postal_code);
                            address.setUser(user);
                            
                            session.save(address);
 
                            saveOrders(session, transaction, user, address, responseJsonObject);
                        }
                    }
                }
                
            }
            
        } else { 
            responseJsonObject.addProperty("message", "User not signed in");
        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
        
    }
    
    public void saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {
 
        try {
            entity.Orders orders = new entity.Orders();
            orders.setAddress(address);
            orders.setDatetime(new Date());
            orders.setUser(user);
            
            int orders_id = (int) session.save(orders);
 
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            
            Order_Status order_Status = (Order_Status) session.get(Order_Status.class, 4);
 
            double amount = 0;
            String items = "";
            for (Cart cartItem : cartList) {

            
                amount += cartItem.getQty() * cartItem.getProduct().getPrice();
                
                if (address.getCity().getId() == 1) {
                    
                    amount += 350;
                    
                } else {
                    amount += 500;
                }
 
                items += cartItem.getProduct().getTital() + " x" + cartItem.getQty() + " ";
                
                Product product = cartItem.getProduct();
                
                Order_Item order_Item = new Order_Item();
                order_Item.setOrders(orders);
                order_Item.setOrder_status(order_Status);
                order_Item.setProduct(product);
                order_Item.setQty(cartItem.getQty());
                session.save(order_Item);
 
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product);
 
                session.delete(cartItem);
            }
            
            transaction.commit(); 
            
            String merchand_id = "1228929";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "NDAyNzg2MDE2OTMxOTU4ODc3MjgxMDE3Njk4NTczMTIzNTQ4MTI0OQ==";
            String merchantSecretMd5Hash = PayHere.generateMD5(merchantSecret);
             
            
            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchand_id);
            
            System.out.println("----done3---");
            
            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "");
            
            System.out.println("----done4---");
            
            payhere.addProperty("first_name", user.getFirst_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", "");
            
            payhere.addProperty("address", "");
            payhere.addProperty("city", "");
            payhere.addProperty("country", "");
            payhere.addProperty("order_id", String.valueOf(orders_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formatedAmount);
            payhere.addProperty("sandbox", true);
 
            String md5Hash = PayHere.generateMD5(merchand_id + orders_id + formatedAmount + currency + merchantSecretMd5Hash);
            payhere.addProperty("hash", md5Hash);
            
            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));
            
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Checkout Completed");
            
            System.out.println(payhere);
            
            System.out.println("done1");
            
        } catch (Exception e) {
            transaction.rollback();
        }
        
        System.out.println("done2");
        
    }

}

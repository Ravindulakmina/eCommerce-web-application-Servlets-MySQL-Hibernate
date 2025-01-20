 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import model.HibernateUtil;
import org.hibernate.Criteria; 
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ravindu lakmina
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        Response_DTO response_DTO = new Response_DTO();
        
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);
        
        if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please Enter Your Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter Your Password");
            
        } else {
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteria1.add(Restrictions.eq("password", user_DTO.getPassword()));
            
            if (!criteria1.list().isEmpty()) {
                
                User user = (User) criteria1.list().get(0);
                if (!user.getVerification_code().equals("Verified")) {
                    //not verified
                    request.getSession().setAttribute("email", user_DTO.getEmail());
                    response_DTO.setContent("Unverified");
                    
                } else {
                    //verified

                    user_DTO.setFirst_name(user.getFirst_name());
                    user_DTO.setLast_name(user.getLast_name());
                    user_DTO.setPassword(null);
                    request.getSession().setAttribute("user", user_DTO);

                    //transfer session cart to db cart
                    if (request.getSession().getAttribute("sessionCart") != null) {
                        
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");
                        
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();
                        
                        if (dbCart.isEmpty()) {
                            //db cart is empty
                            //add all session cart items to db cart
                            for (Cart_DTO cart_DTO : sessionCart) {
                                
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }
                            
                        } else {
                            //found items in db cart

                            for (Cart_DTO cart_DTO : sessionCart) {
                                
                                boolean isFoundInDBCart = false;
                                for (Cart cart : dbCart) {
                                    
                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        //same item found in session and DB cart
                                        isFoundInDBCart = true;
                                        
                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
                                            //quantity available
                                            
                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            session.update(cart);
                                            
                                        } else {
                                            //quantity not available
                                            //set max available qty to cart
                                            cart.setQty(cart.getProduct().getQty());
                                            session.update(cart);
                                            
                                        } 
                                    }
                                }
                                
                                if (!isFoundInDBCart) {
                                    //not found in db cart
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct());
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                    
                                }
                            }
                            
                        }
                        
                        request.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    }
                    
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign In Success");
                }
                
            } else {
                response_DTO.setContent("Invalid Details! Please try again");
            }
            
        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

}

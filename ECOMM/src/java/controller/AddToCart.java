package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            String id = request.getParameter("id");
            String qty = request.getParameter("qty");

            if (!Validation.isInteger(id)) {
                response_DTO.setContent("Product now found");

            } else if (!Validation.isInteger(qty)) {
                response_DTO.setContent("Invalid Quantity");

            } else {

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                if (productQty <= 0) {

                    response_DTO.setContent("Quantity must greater than 0");

                } else {

                    Product product = (Product) session.get(Product.class, productId);

                    if (product != null) {
                        //product found
                        
                        if (request.getSession().getAttribute("user") != null || request.getSession().getAttribute("seller") != null) {
                            //DB Cart

                            User_DTO user_DTO = null;

                            if (request.getSession().getAttribute("user") != null) {

                                user_DTO = (User_DTO) request.getSession().getAttribute("user");

                            } else {

                                user_DTO = (User_DTO) request.getSession().getAttribute("seller");
                            }

                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));

                            User user = (User) criteria1.uniqueResult();

                            //check db cart
                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {
                                //item not found in cart

                                if (productQty <= product.getQty()) {

                                    Cart cart = new Cart();
                                    cart.setProduct(product);
                                    cart.setQty(productQty);
                                    cart.setUser(user);

                                    session.save(cart);
                                    transaction.commit();

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Product Added to Cart");

                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }

                            } else {
                                //item already found in cart
                                Cart cartItem = (Cart) criteria2.uniqueResult();

                                if (cartItem.getQty() + productQty <= product.getQty()) {

                                    cartItem.setQty(cartItem.getQty() + productQty);
                                    session.update(cartItem);
                                    transaction.commit();

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart item Updated");

                                } else {
                                    response_DTO.setContent("Quantity not available");

                                }
                            }

                        } else {
                            //Session Cart

                            HttpSession httpSession = request.getSession();

                            if (httpSession.getAttribute("sessionCart") != null) {

                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                                Cart_DTO foundCart_DTO = null;

                                for (Cart_DTO cart_DTO : sessionCart) {

                                    if (cart_DTO.getProduct().getId() == product.getId()) {
                                        foundCart_DTO = cart_DTO;
                                        break;
                                    }
                                }

                                if (foundCart_DTO != null) {
                                    //product found

                                    if (foundCart_DTO.getQty() + productQty <= product.getQty()) {

                                        foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);

                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Cart item updated");

                                    } else {
                                        response_DTO.setContent("Quantity not available");
                                    }

                                } else {
                                    //product not found

                                    if (productQty <= product.getQty()) {
                                        //add to sesion cart

                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setProduct(product);
                                        cart_DTO.setQty(productQty);
                                        sessionCart.add(cart_DTO);

                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Product Added to Cart");

                                    } else {
                                        response_DTO.setContent("Quantity not available");

                                    }

                                }

                            } else {
                                //Session not found
                                if (productQty <= product.getQty()) {

                                    ArrayList<Cart_DTO> sessionCart = new ArrayList();

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Product Added to Cart");

                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }
                            }
                        }
                        
                    } else {
                        response_DTO.setContent("Product not found");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setContent("Unable to process your request.");
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}

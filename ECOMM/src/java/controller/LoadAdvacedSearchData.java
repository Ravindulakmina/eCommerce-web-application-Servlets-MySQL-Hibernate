/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Ravindu lakmina
 */
@WebServlet(name = "LoadAdvacedSearchData", urlPatterns = {"/LoadAdvacedSearchData"})
public class LoadAdvacedSearchData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            Gson gson = new Gson(); 
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", false);
            
            Session session = HibernateUtil.getSessionFactory().openSession();
             
            //get products
            Criteria criteria1 = session.createCriteria(Product.class);
            criteria1.addOrder(Order.desc("id"));
            criteria1.setMaxResults(10);
            
            List<Product> productList = criteria1.list();
            for (Product product : productList) {
                product.setUser(null);
            } 
            
            
            jsonObject.add("productList", gson.toJsonTree(productList));
            jsonObject.addProperty("success", true);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(jsonObject));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

}

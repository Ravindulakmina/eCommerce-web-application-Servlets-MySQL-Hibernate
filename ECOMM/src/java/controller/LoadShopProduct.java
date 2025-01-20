/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Color;
import entity.Product;
import entity.Size;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;


@WebServlet(name = "LoadShopProduct", urlPatterns = {"/LoadShopProduct"})
public class LoadShopProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("success", false);

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //Main code
        //Ses Category List from DB
        Criteria criterial = session.createCriteria(Category.class); 
        List<Category> categoryList = criterial.list(); 
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        
        //Get Color List from DB
        Criteria criteria2 = session.createCriteria(Color.class); 
        List<Color> colorList = criteria2.list(); 
        jsonObject.add("colorlist", gson.toJsonTree(colorList));

        //Get Storage List from DB
        Criteria criteria3 = session.createCriteria(Size.class); 
        List<Size> sizeList = criteria3.list();

        jsonObject.add("sizelist", gson.toJsonTree(sizeList));

        //Ges Product List from DB
        Criteria criteria4 = session.createCriteria(Product.class);

        //Get lasetses products
        criteria4.addOrder(Order.desc("id"));
        jsonObject.addProperty("allProductCount", criteria4.list().size());

        //set product range
        criteria4.setFirstResult(0);
        criteria4.setMaxResults(6);

        List<Product> productList = criteria4.list();

        //Remove User from Product
        for (Product product : productList) {
            product.setUser(null);
        }
         
        jsonObject.add("productList", gson.toJsonTree(productList));
        jsonObject.addProperty("success", true);

        //Main code
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
    }

}

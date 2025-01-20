/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ravindu lakmina
 */
@WebServlet(name = "searchAllProducts", urlPatterns = {"/searchAllProducts"})
public class searchAllProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", true);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Product.class);

        if (requestJsonObject.has("category")) {

            String category_Id = requestJsonObject.get("category").getAsString();

            Criteria criteria2 = session.createCriteria(Category.class);
            criteria2.add(Restrictions.eq("id", Integer.parseInt(category_Id)));
            Category category = (Category) criteria2.uniqueResult();

            Criteria criteria3 = session.createCriteria(Brand.class);
            criteria3.add(Restrictions.eq("category", category));
            List<Brand> brandList = criteria3.list();

            //criteria1.add(Restrictions.eq("category", category));
            if (category != null) {
                criteria1.add(Restrictions.in("brand", brandList));
            }    

        }
//        
//        if(requestJsonObject.has("brand")){ 
//        
//            String brand_Id = requestJsonObject.get("brand").getAsString();
//            
//            Criteria criteria4 = session.createCriteria(Brand.class);
//            criteria4.add(Restrictions.eq("id", Integer.parseInt(brand_Id)));
//            Brand  brand = (Brand) criteria4.uniqueResult();
//            
//            criteria1.add(Restrictions.eq("brand", brand)); 
//        }
// 

        if (requestJsonObject.has("color")) {

            String color_Id = requestJsonObject.get("color").getAsString();

            Criteria criteria4 = session.createCriteria(Color.class);
            criteria4.add(Restrictions.eq("id", Integer.parseInt(color_Id)));
            Color color = (Color) criteria4.uniqueResult();

            if (color != null) {
                criteria1.add(Restrictions.eq("color", color));
            }    

        }

        if (requestJsonObject.has("size")) {

            String size_size = requestJsonObject.get("size").getAsString();

            Criteria criteria5 = session.createCriteria(Size.class);
            criteria5.add(Restrictions.eq("id", Integer.parseInt(size_size)));
            Size size2 = (Size) criteria5.uniqueResult();

            if (size2 != null) {
                criteria1.add(Restrictions.eq("size", size2));
            }    
        }

        double price_range_start = requestJsonObject.get("price_range_start").getAsDouble();
        double price_range_end = requestJsonObject.get("price_range_end").getAsDouble();

        criteria1.add(Restrictions.ge("price", price_range_start));
        criteria1.add(Restrictions.le("price", price_range_end));
 
        responseJsonObject.addProperty("allProductCount", criteria1.list().size());
 
        int firstResult = requestJsonObject.get("firstResult").getAsInt();

        criteria1.setFirstResult(firstResult);
        criteria1.setMaxResults(6);

        List<Product> productList = criteria1.list();
 
        for (Product product : productList) {
            product.setUser(null);
        }

        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("productList", gson.toJsonTree(productList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }

}

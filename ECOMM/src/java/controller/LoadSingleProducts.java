 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
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
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ravindu lakmina
 */
@WebServlet(name = "LoadSingleProducts", urlPatterns = {"/LoadSingleProducts"})
public class LoadSingleProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
       
        try {
            String productId = request.getParameter("id");

            if (Validation.isInteger(productId)) {

                Product product = (Product) session.get(Product.class, Integer.parseInt(productId));
                product.getUser().setPassword(null);
                product.getUser().setVerification_code(null);
                product.getUser().setEmail(null);

                Criteria criteria1 = session.createCriteria(Brand.class);
                criteria1.add(Restrictions.eq("category", product.getBrand().getCategory()));

                List<Brand> brandList = criteria1.list();

                Criteria criteria2 = session.createCriteria(Product.class);
                criteria2.add(Restrictions.in("brand", brandList));
                criteria2.add(Restrictions.ne("id", Integer.parseInt(productId)));
                criteria2.setMaxResults(6);

                List<Product> productList = criteria2.list();

                for (Product product1 : productList) {
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerification_code(null);
                    product1.getUser().setEmail(null);
                }
                
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product", gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));
            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

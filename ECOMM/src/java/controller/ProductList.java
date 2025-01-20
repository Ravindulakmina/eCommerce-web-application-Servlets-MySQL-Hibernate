/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Brand;
import entity.Category;
import entity.Color;
import entity.Product;
import entity.Product_Status;
import entity.Size;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductList", urlPatterns = {"/ProductList"})
public class ProductList extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String categoryId = request.getParameter("categoryId");
        String brandId = request.getParameter("brandId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String sizeId = request.getParameter("sizeId");
        String colorId = request.getParameter("colorId"); 
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");

        Part image1 = request.getPart("image1"); 

        Session session = HibernateUtil.getSessionFactory().openSession();
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        if (!Validation.isInteger(categoryId)) {
            response_DTO.setContent("Invalid Category");

        } else if (!Validation.isInteger(brandId)) {
            response_DTO.setContent("Invalid Brand");

        } else if (title.isEmpty()) {
            response_DTO.setContent("Please fill Title");

        } else if (description.isEmpty()) {
            response_DTO.setContent("Please fill Description");

        } else if (!Validation.isInteger(sizeId)) {
            response_DTO.setContent("Invalid Size");

        } else if (!Validation.isInteger(colorId)) {
            response_DTO.setContent("Invalid Color");

        } else if (price.isEmpty()) {
            response_DTO.setContent("Please fill Price");

        } else if (!Validation.isDouble(price)) {
            response_DTO.setContent("Invalid Price");

        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price must be greater than 0.00");

        } else if (quantity.isEmpty()) {
            response_DTO.setContent("Please Fill Quantity");

        } else if (!Validation.isInteger(quantity)) {
            response_DTO.setContent("Invalid Quantity");

        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be greater than 0");

        } else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload Image 1"); 
        } else {

            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please select a valid Category");
            } else {

                Brand brand = (Brand) session.get(Brand.class, Integer.parseInt(brandId));

                if (brand == null) {
                    response_DTO.setContent("Please select a valid Brand");

                } else {

                    Size size = (Size) session.get(Size.class, Integer.parseInt(sizeId));

                    if (size == null) {
                        response_DTO.setContent("Please select a valid Size");
                    } else {

                        Color color = (Color) session.get(Color.class, Integer.parseInt(colorId));

                        if (color == null) {
                            response_DTO.setContent("Please select a valid Color");
                        } else {
                            
                            Product product = new Product();
                            product.setColor(color);
                            product.setDatetime(new Date());
                            product.setDescription(description);
                            product.setBrand(brand);
                            product.setPrice(Double.parseDouble(price));

                            Product_Status product_status = (Product_Status) session.load(Product_Status.class, 1);
                            product.setProduct_status(product_status);

                            product.setQty(Integer.parseInt(quantity));
                            product.setSize(size);
                            product.setTital(title);

                            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));

                            User user = (User) criteria1.uniqueResult();

                            product.setUser(user);

                            int pid = (int) session.save(product);
                            session.beginTransaction().commit();

                            String applicatonPath = request.getServletContext().getRealPath("");
                            String newApplicationPath = applicatonPath.replace("build" + File.separator + "web", "web");

                            File folder = new File(newApplicationPath + "//product-images//" + pid); 
                            folder.mkdir();

                            File file1 = new File(folder, "image1.png");
                            InputStream inputStream1 = image1.getInputStream();
                            Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING); 

                            response_DTO.setSuccess(true);
                            response_DTO.setContent("New Product Added");
                        }
                    }

                }

            }

        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        session.close();

    }

}

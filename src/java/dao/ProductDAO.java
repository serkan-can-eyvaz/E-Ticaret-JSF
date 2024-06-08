/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Category;
import entity.Product;
import entity.Seller;
import entity.Store;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Demirr
 */
@Local
@Stateless
public class ProductDAO extends BaseDAO<Product> implements Serializable {

    public ProductDAO() {
        super(Product.class);
    }

    public List<Product> listele(int cp, int epp) {
        List<Product> productList;
        try {
            // String jpql = ;
            TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
            query.setFirstResult((cp - 1) * epp);
            query.setMaxResults(epp);
            productList = query.getResultList();
            System.out.println("product listeleme işlemi başarılı");
        } catch (Exception e) {
            System.out.println("Error while reading product list: " + e.getMessage());
            productList = new ArrayList<>();
        }
        return productList;
    }

    public int Count() {
        int count = 0;
        try {
            Query query = em.createQuery("select COUNT(product_id) AS toplam from product");
            count = ((Long) query.getSingleResult()).intValue();
        } catch (Exception e) {
            System.out.println("ürün sayısı hesaplanmadı " + e.getMessage());
        }
        return count;
    }

    public List<Category> getProductCategories(Long productid) {
        List<Category> categoryList = null;
        try {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c WHERE c.id IN "
                    + "(SELECT pc.category_id FROM product_category pc WHERE pc.product_id = :productid)",
                    Category.class);
            query.setParameter("productid", productid);
            categoryList = query.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return categoryList;
    }
    
    public List<Product> getProductListByCategoryId(Long categoryId) {
        List<Product> productList = null;
        try {
            TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product WHERE category_id = :categoryId", 
                Product.class);
            query.setParameter("categoryId", categoryId);
            productList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error while reading product list: " + e.getMessage());
        }
        return productList;
    }

}
    
    
    /*
     public List<Product> listele(int cp, int epp) {
       List<Product> productList = new ArrayList<>();
        /* Category category = null;
        Store store = null;
        try {
            String sql = String.format("select * from Product LIMIT %d OFFSET %d", epp, (cp) * epp);
            PreparedStatement pst = this.getConnect().prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setStock(rs.getInt("stock"));
                product.setDetail(rs.getString("detail"));

                product.setCategories(this.getProductCategories(rs.getLong("id")));
                product.setStore(store);

                product.setPrice(rs.getInt("price"));
                product.setCreatedDate(rs.getTimestamp("createddate"));
                product.setLastModifiedDate(rs.getTimestamp("lastmodifieddate"));

                productList.add(product);

                System.out.println("product listeleme işlemei başarılı");
            }

            pst.close();
        } catch (SQLException e) {

            System.out.println("Error while reading product list: " + e.getMessage());
        }
        return productList;
    }
    
}
/*
    
    private StoreDAO storeDAO;

    @Override
    public void create(Product product) {

        try {
            // PreparedStatement oluştur
            Statement st = this.getConnect().createStatement();

            String query = "INSERT INTO product (name, stock, detail, price, storeid) "
                    + "VALUES ('" + product.getName() + "', "
                    + product.getStock() + ", '"
                    + product.getDetail() + "', "
                    + product.getPrice() + ", "
                    + 1
                    + ")"; 

            st.executeUpdate(query);

            ResultSet rs = st.executeQuery("select max(id) as mid from product");
            rs.next();
            int product_id = rs.getInt("mid");

            for (Category category : product.getCategories()) {
                query = "insert into product_category (productid,categoryid) values(" + product_id + "," + category.getId() + ")";
                st.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println("Error while creating product: " + e.getMessage());
        }
    }

    @Override
    public void update(Product entity) {

        try {
            Statement st = this.getConnect().createStatement();
            String query = "update product set "
                    + "name = '" + entity.getName() + "', "
                    + "stock = '" + entity.getStock() + "', "
                    + "detail = '" + entity.getDetail() + "', "
                    + "price = '" + entity.getPrice() + "', "
                    + "lastmodifieddate = '" + new Timestamp(System.currentTimeMillis()) + "' "
                    + "where id = '" + entity.getId() + "'";
            st.executeUpdate(query);

            st.executeUpdate("delete from product_category where productid=" + entity.getId());

            for (Category category : entity.getCategories()) {
                query = "insert into product_category (productid,categoryid) values(" + entity.getId() + "," + category.getId() + ")";
                st.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println("Error while creating product: " + e.getMessage());

        }

    }

    @Override
    public void delete(Product entity) {
        try {
            Statement st = this.getConnect().createStatement();
            st.executeUpdate("delete from product_category where productid=" + entity.getId());

            String query = "delete from product where id=" + entity.getId();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            System.out.println("Error while creating product: " + e.getMessage());

        }

    }

    @Override
    public List<Product> readList() {
        List<Product> productList = new ArrayList<>();
        Store store = null;
        try {
            PreparedStatement pst = this.getConnect().prepareStatement("SELECT * FROM Product");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setStock(rs.getInt("stock"));
                product.setDetail(rs.getString("detail"));

                product.setCategories(this.getProductCategories(rs.getLong("id")));
                product.setStore(store);

                product.setPrice(rs.getInt("price"));
                product.setCreatedDate(rs.getTimestamp("createddate"));
                product.setLastModifiedDate(rs.getTimestamp("lastmodifieddate"));

                productList.add(product);

                System.out.println("product listeleme işlemei başarılı");
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {

            System.out.println("Error while reading product list: " + e.getMessage());
        }
        return productList;
    }

    public List<Category> getProductCategories(Long productid) {
        List<Category> categoryList = new ArrayList<>();

        try {
            Statement st = this.getConnect().createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product_category WHERE categoryid IN (SELECT categoryid FROM product_category WHERE productid = " + productid + ")");

            while (rs.next()) {
                categoryList.add(new Category(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("createddate"),
                        rs.getTimestamp("lastmodifieddate")));
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return categoryList;
    }

    public Product getEntityById(Long productId) {

        Product product = null;
        Category category = null;
        Store store = null;

        try {
            Statement st = this.getConnect().createStatement();

            ResultSet rs = st.executeQuery("select * from product where id = " + productId);

            rs.next();

            store = getStoreDAO().getEntityById(rs.getLong("storeid"));

            product = new Product(
                    rs.getLong("id"),
                    rs.getTimestamp("createddate"),
                    rs.getTimestamp("lastmodifieddate"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getString("detail"),
                    this.getProductCategories(productId),
                    rs.getInt("price"),
                    store
            );
            st.close();
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return product;

    }

    public StoreDAO getStoreDAO() {
        if (this.storeDAO == null) {
            storeDAO = new StoreDAO();
        }
        return storeDAO;
    }

    public List<Product> getProductListByCategoryId(Long categoryId) {
        List<Product> productList = new ArrayList<>();
        Store store = null;
        try {
            PreparedStatement pst = this.getConnect().prepareStatement("SELECT * FROM Product where categoryid=" + categoryId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setStock(rs.getInt("stock"));
                product.setDetail(rs.getString("detail"));

                product.setCategories(this.getProductCategories(rs.getLong("id")));
                product.setStore(store);

                product.setPrice(rs.getInt("price"));
                product.setCreatedDate(rs.getTimestamp("createddate"));
                product.setLastModifiedDate(rs.getTimestamp("lastmodifieddate"));

                productList.add(product);

                System.out.println("product listeleme işlemei başarılı");
            }

            pst.close();
        } catch (SQLException e) {

            System.out.println("Error while reading product list: " + e.getMessage());
        }
        return productList;
    }

    public List<Product> listele(int cp, int epp) {
        List<Product> productList = new ArrayList<>();
        Category category = null;
        Store store = null;
        try {
            String sql = String.format("select * from Product LIMIT %d OFFSET %d", epp, (cp) * epp);
            PreparedStatement pst = this.getConnect().prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setStock(rs.getInt("stock"));
                product.setDetail(rs.getString("detail"));

                product.setCategories(this.getProductCategories(rs.getLong("id")));
                product.setStore(store);

                product.setPrice(rs.getInt("price"));
                product.setCreatedDate(rs.getTimestamp("createddate"));
                product.setLastModifiedDate(rs.getTimestamp("lastmodifieddate"));

                productList.add(product);

                System.out.println("product listeleme işlemei başarılı");
            }

            pst.close();
        } catch (SQLException e) {

            System.out.println("Error while reading product list: " + e.getMessage());
        }
        return productList;
    }

}
*/

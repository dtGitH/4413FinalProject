package factory;

import dao.ProductDAO;
import dao.UserDAO;
import dao.OrderDAO;
import dao.ShoppingCartDAO;

public class DAOFactory {

    // Factory method to create and return ProductDAO instance
    public static ProductDAO getProductDAO() {
        return new ProductDAO();
    }

    // Factory method to create and return UserDAO instance
    public static UserDAO getUserDAO() {
        return new UserDAO();
    }

    // Factory method to create and return OrderDAO instance
    public static OrderDAO getOrderDAO() {
        return new OrderDAO();
    }

    // Factory method to create and return ShoppingCartDAO instance
    public static ShoppingCartDAO getShoppingCartDAO() {
        return new ShoppingCartDAO();
    }
}

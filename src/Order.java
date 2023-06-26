import products.Product;

import java.util.List;

public class Order {
    private User user;
    private List<Product> products;

    private Order() {
    }

    public static Order createOrder(User user, List<Product> products) {
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        return order;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", products=" + products +
                '}';
    }
}

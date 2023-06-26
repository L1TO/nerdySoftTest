import products.Product;
import products.ProductFactory;
import products.RealProduct;
import products.VirtualProductCodeManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //TODO Create User class with method createUser
        // User class fields: name, age;
        // Notice that we can only create user with createUser method without using constructor or builder
        User user1 = User.createUser("Alice", 32);
        User user2 = User.createUser("Bob", 19);
        User user3 = User.createUser("Charlie", 20);
        User user4 = User.createUser("John", 27);

        //TODO Create factory that can create a product for a specific type: Real or Virtual
        // Product class fields: name, price
        // Product Real class additional fields: size, weight
        // Product Virtual class additional fields: code, expiration date
        Product realProduct1 = ProductFactory.createRealProduct("Product A", 20.50, 10, 25);
        Product realProduct2 = ProductFactory.createRealProduct("Product B", 50, 6, 17);

        Product virtualProduct1 = ProductFactory.createVirtualProduct("Product C", 100, "xxx", LocalDate.of(2023, 5, 12));
        Product virtualProduct2 = ProductFactory.createVirtualProduct("Product D", 81.25, "yyy", LocalDate.of(2024, 6, 20));

        //TODO Create Order class with method createOrder
        // Order class fields: User, List<Price>
        // Notice that we can only create order with createOrder method without using constructor or builder
        List<Order> orders = new ArrayList<>() {{
            add(Order.createOrder(user1, List.of(realProduct1, virtualProduct1, virtualProduct2)));
            add(Order.createOrder(user2, List.of(realProduct1, realProduct2)));
            add(Order.createOrder(user3, List.of(realProduct1, virtualProduct2)));
            add(Order.createOrder(user4, List.of(virtualProduct1, virtualProduct2, realProduct1, realProduct2)));
        }};

        //TODO 1). Create singleton class which will check the code is used already or not
        // Singleton class should have the possibility to mark code as used and check if code used
        // Example:
        // singletonClass.useCode("xxx")
        // boolean isCodeUsed = virtualProductCodeManager.isCodeUsed("xxx") --> true;
        // boolean isCodeUsed = virtualProductCodeManager.isCodeUsed("yyy") --> false;
        System.out.println("1. Create singleton class VirtualProductCodeManager \n");
        VirtualProductCodeManager virtualProductCodeManager = VirtualProductCodeManager.getInstance();
        virtualProductCodeManager.useCode("xxx");
        var isUsed = virtualProductCodeManager.isCodeUsed("yyy");
        System.out.println("Is code used: " + isUsed + "\n");

        //TODO 2). Create a functionality to get the most expensive ordered product
        Product mostExpensive = getMostExpensiveProduct(orders);
        System.out.println("2. Most expensive product: " + mostExpensive + "\n");

        //TODO 3). Create a functionality to get the most popular product(product bought by most users) among users
        Product mostPopular = getMostPopularProduct(orders);
        System.out.println("3. Most popular product: " + mostPopular + "\n");

        //TODO 4). Create a functionality to get average age of users who bought realProduct2
        double averageAge = calculateAverageAge(realProduct2, orders);
        System.out.println("4. Average age is: " + averageAge + "\n");

        //TODO 5). Create a functionality to return map with products as keys and a list of users
        // who ordered each product as values
        Map<Product, List<User>> productUserMap = getProductUserMap(orders);
        System.out.println("5. Map with products as keys and list of users as value \n");
        productUserMap.forEach((key, value) -> System.out.println("key: " + key + " " + "value: " + value + "\n"));

        //TODO 6). Create a functionality to sort/group entities:
        // a) Sort Products by price
        // b) Sort Orders by user age in descending order
        List<Product> productsByPrice = sortProductsByPrice(List.of(realProduct1, realProduct2, virtualProduct1, virtualProduct2));
        System.out.println("6. a) List of products sorted by price: " + productsByPrice + "\n");
        List<Order> ordersByUserAgeDesc = sortOrdersByUserAgeDesc(orders);
        System.out.println("6. b) List of orders sorted by user agge in descending order: " + ordersByUserAgeDesc + "\n");

        //TODO 7). Calculate the total weight of each order
        Map<Order, Integer> result = calculateWeightOfEachOrder(orders);
        System.out.println("7. Calculate the total weight of each order \n");
        result.forEach((key, value) -> System.out.println("order: " + key + " " + "total weight: " +  value + "\n"));
    }

    private static Product getMostExpensiveProduct(List<Order> orders) {
        Product mostExpensiveProduct = orders.get(0).getProducts().get(0);
        for (Order order : orders) {
            for (Product product : order.getProducts()) {
                if (product.getPrice() > mostExpensiveProduct.getPrice()) {
                    mostExpensiveProduct = product;
                }
            }
        }
        return mostExpensiveProduct;
    }


    private static Product getMostPopularProduct(List<Order> orders) {
        //counting unique values
        Map<Product, Integer> result = new HashMap<>();
        for (Order order : orders) {
            for (Product product : order.getProducts()) {
                result.put(product, result.getOrDefault(product, 0) + 1);
            }
        }

        int maxCount = 0;
        Product mostPopularProduct = null;

        for (Map.Entry<Product, Integer> entry : result.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularProduct = entry.getKey();
            }
        }
        return mostPopularProduct;
    }

    private static double calculateAverageAge(Product product, List<Order> orders) {
        int countOfPeaople = 0;
        int countOfYears = 0;
        for (Order order : orders) {
            for (Product p : order.getProducts()) {
                if (p.equals(product)) {
                    countOfYears += order.getUser().getAge();
                    countOfPeaople++;
                }
            }
        }
        return (double) countOfYears / countOfPeaople;
    }

    private static Map<Product, List<User>> getProductUserMap(List<Order> orders) {
        Map<Product, List<User>> result = new HashMap<>();

        List<Product> products = new ArrayList<>();
        orders.forEach(order -> products.addAll(order.getProducts()));
        List<Product> uniqProducts = products.stream().distinct().collect(Collectors.toList());

        List<User> users;
        for (int i = 0; i < uniqProducts.size(); i++) {
            users = new ArrayList<>();
            for (Order order : orders) {
                for (Product product : order.getProducts()) {
                    if (uniqProducts.get(i).equals(product)) {
                        users.add(order.getUser());
                    }
                }
            }
            result.put(uniqProducts.get(i), users);
        }
        return result;
    }

    private static List<Product> sortProductsByPrice(List<Product> products) {
        List<Product> sortedList = new ArrayList<>(products);
        Collections.sort(sortedList, Comparator.comparing(Product::getPrice));
        return sortedList;
    }

    private static List<Order> sortOrdersByUserAgeDesc(List<Order> orders) {
        List<Order> sortedList = new ArrayList<>(orders);
        Collections.sort(sortedList, Comparator.comparing(order -> order.getUser().getAge(), Comparator.reverseOrder()));
        return sortedList;
    }

    private static Map<Order, Integer> calculateWeightOfEachOrder(List<Order> orders) {
        Map<Order, Integer> result = new HashMap<>();
        int count;
        for (Order order : orders) {
            count = 0;
            for (Product product : order.getProducts()) {
                if (product instanceof RealProduct){
                    count += ((RealProduct) product).getWeight();
                }
            }
            result.put(order, count);
        }
        return result;
    }
}
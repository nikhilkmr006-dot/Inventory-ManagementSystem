import java.io.*;
import java.util.*;

// ====================== ENTITY CLASSES ==========================

class Product implements Serializable {
    int productId;
    String name;
    String description;
    double price;
    int quantity;
    int reorderLevel;

    public Product(int productId, String name, String description, double price, int quantity, int reorderLevel) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String toString() {
        return productId + " | " + name + " | Qty: " + quantity + " | Price: " + price;
    }
}

class Supplier implements Serializable {
    int supplierId;
    String name;
    String contact;

    Supplier(int id, String name, String contact) {
        this.supplierId = id;
        this.name = name;
        this.contact = contact;
    }

    public String toString() {
        return supplierId + " | " + name + " | " + contact;
    }
}

class Customer implements Serializable {
    int customerId;
    String name;
    String contact;

    Customer(int id, String name, String contact) {
        this.customerId = id;
        this.name = name;
        this.contact = contact;
    }

    public String toString() {
        return customerId + " | " + name + " | " + contact;
    }
}

class Order implements Serializable {
    int orderId;
    int productId;
    int customerId;
    int quantity;
    double total;

    Order(int oid, int pid, int cid, int qty, double total) {
        this.orderId = oid;
        this.productId = pid;
        this.customerId = cid;
        this.quantity = qty;
        this.total = total;
    }

    public String toString() {
        return "Order: " + orderId + " | PID: " + productId + " | CID: " + customerId +
               " | Qty: " + quantity + " | Total: " + total;
    }
}


// ====================== MAIN SYSTEM ===========================

public class InventoryManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Supplier> suppliers = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();

    // ---------- FILE HANDLING ----------
    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (ArrayList<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    static <T> void save(String filename, ArrayList<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (Exception e) {
            System.out.println("Error saving " + filename);
        }
    }

    // ====================== PRODUCT FEATURES ==========================

    static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Description: ");
            String desc = sc.nextLine();

            System.out.print("Enter Price: ");
            double price = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Reorder Level: ");
            int r = Integer.parseInt(sc.nextLine());

            products.add(new Product(id, name, desc, price, qty, r));
            save("products.dat", products);
            System.out.println("✅ Product added successfully!");

        } catch (Exception e) {
            System.out.println("❌ Invalid Input!");
        }
    }

    static void showProducts() {
        System.out.println("\n===== PRODUCT LIST =====");
        for (Product p : products) System.out.println(p);
    }

    // ====================== SUPPLIER FEATURES ==========================

    static void addSupplier() {
        try {
            System.out.print("Enter Supplier ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Supplier Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Contact: ");
            String contact = sc.nextLine();

            suppliers.add(new Supplier(id, name, contact));
            save("suppliers.dat", suppliers);
            System.out.println("✅ Supplier added!");

        } catch (Exception e) {
            System.out.println("❌ Invalid Input!");
        }
    }

    static void showSuppliers() {
        System.out.println("===== SUPPLIER LIST =====");
        for (Supplier s : suppliers) System.out.println(s);
    }

    // ====================== CUSTOMER FEATURES ==========================

    static void addCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Customer Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Contact: ");
            String contact = sc.nextLine();

            customers.add(new Customer(id, name, contact));
            save("customers.dat", customers);
            System.out.println("✅ Customer added!");

        } catch (Exception e) {
            System.out.println("❌ Invalid Input!");
        }
    }

    static void showCustomers() {
        System.out.println("===== CUSTOMER LIST =====");
        for (Customer c : customers) System.out.println(c);
    }

    // ====================== ORDER SYSTEM ==========================

    static void placeOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            Product p = null;
            for (Product pr : products)
                if (pr.productId == pid) p = pr;

            if (p == null) {
                System.out.println("❌ Product not found!");
                return;
            }

            if (p.quantity < qty) {
                System.out.println("❌ Not enough stock!");
                return;
            }

            double total = qty * p.price;
            p.quantity -= qty;

            Order o = new Order(oid, pid, cid, qty, total);
            orders.add(o);

            save("products.dat", products);
            save("orders.dat", orders);

            System.out.println("✅ Order placed successfully!");

        } catch (Exception e) {
            System.out.println("❌ Invalid Input!");
        }
    }

    static void showOrders() {
        System.out.println("===== ORDER LIST =====");
        for (Order o : orders) System.out.println(o);
    }

    // ====================== REPORTS ==========================

    static void lowStockReport() {
        System.out.println("\n===== LOW STOCK REPORT =====");
        for (Product p : products)
            if (p.quantity <= p.reorderLevel)
                System.out.println("⚠ Low Stock → " + p);
    }

    static void stockReport() {
        System.out.println("\n===== STOCK REPORT =====");
        for (Product p : products) System.out.println(p);
    }

    static void salesReport() {
        System.out.println("\n===== SALES REPORT =====");
        for (Order o : orders) System.out.println(o);
    }

    // ====================== MAIN MENU ==========================

    public static void main(String[] args) {

        products = load("products.dat");
        suppliers = load("suppliers.dat");
        customers = load("customers.dat");
        orders = load("orders.dat");

        int choice = -1;

        do {
            try {
                System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Add Supplier");
                System.out.println("4. View Suppliers");
                System.out.println("5. Add Customer");
                System.out.println("6. View Customers");
                System.out.println("7. Place Order");
                System.out.println("8. View Orders");
                System.out.println("9. Stock Report");
                System.out.println("10. Low Stock Report");
                System.out.println("11. Sales Report");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                choice = Integer.parseInt(sc.nextLine());

            } catch (Exception e) {
                System.out.println("❌ Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1: addProduct(); break;
                case 2: showProducts(); break;
                case 3: addSupplier(); break;
                case 4: showSuppliers(); break;
                case 5: addCustomer(); break;
                case 6: showCustomers(); break;
                case 7: placeOrder(); break;
                case 8: showOrders(); break;
                case 9: stockReport(); break;
                case 10: lowStockReport(); break;
                case 11: salesReport(); break;
                case 0: System.out.println("Exiting..."); break;
                default: System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }
}

package software.data;

import software.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDB {

    public Customer[] getAllCustomers() {
        String sql = "SELECT * FROM Customer";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers.toArray(new Customer[0]);
    }

    public Customer getCustomerById(String customerID) {
        String sql = "SELECT * FROM Customer WHERE customerId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customer VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerID());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmail());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, email = ? WHERE customerId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getCustomerID());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String customerID) {
        String sql = "DELETE FROM Customer WHERE customerId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerID);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws Exception {
        String customerID = rs.getString("customerId");
        String name = rs.getString("name");
        String email = rs.getString("email");

        return new Customer(customerID, name, email);
    }
}
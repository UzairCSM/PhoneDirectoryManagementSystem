import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
class Contact {
    private String name;
    private String phoneNumber;
    private String email;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + email;
    }
}

class PhoneDirectory {
    private Connection connection;

    public PhoneDirectory() {
        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact_database", "root", "7599");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContact(Contact contact) {
        try {
            // Prepare the SQL statement
            String sql = "INSERT INTO contacts (name, phoneNumber, email) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setString(3, contact.getEmail());

            // Execute the statement
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Contact searchContact(String name) {
        try {
            // Prepare the SQL statement
            String sql = "SELECT * FROM contacts WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);

            // Execute the statement and retrieve the result
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                return new Contact(name, phoneNumber, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateContact(String name, String phoneNumber, String email) {
        try {
            // Prepare the SQL statement
            String sql = "UPDATE contacts SET phoneNumber = ?, email = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, phoneNumber);
            statement.setString(2, email);
            statement.setString(3, name);

            // Execute the statement
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact(String name) {
        try {
            // Prepare the SQL statement
            String sql = "DELETE FROM contacts WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);

            // Execute the statement
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllContacts() {
        try {
            // Prepare the SQL statement
            String sql = "SELECT * FROM contacts";
            Statement statement = connection.createStatement();

            // Execute the statement and retrieve the result
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                Contact contact = new Contact(name, phoneNumber, email);
                System.out.println(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class PhoneDirectoryManagementSystem {
    public static void main(String[] args) {
        PhoneDirectory phoneDirectory = new PhoneDirectory();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nPhone Directory Management System");
            System.out.println("1. Add Contact");
            System.out.println("2. Search Contact");
            System.out.println("3. Update Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Display All Contacts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter contact name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter phone number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Enter email address: ");
                    String email = scanner.nextLine();
                    Contact contact = new Contact(name, phoneNumber, email);
                    phoneDirectory.addContact(contact);
                    System.out.println("Contact added successfully.");
                    break;
                case 2:
                    System.out.print("Enter contact name to search: ");
                    String searchName = scanner.nextLine();
                    Contact searchedContact = phoneDirectory.searchContact(searchName);
                    if (searchedContact != null) {
                        System.out.println("Contact found:");
                        System.out.println(searchedContact);
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter contact name to update: ");
                    String updateName = scanner.nextLine();
                    Contact updateContact = phoneDirectory.searchContact(updateName);
                    if (updateContact != null) {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNumber = scanner.nextLine();
                        System.out.print("Enter new email address: ");
                        String newEmail = scanner.nextLine();
                        phoneDirectory.updateContact(updateName, newPhoneNumber, newEmail);
                        System.out.println("Contact updated successfully.");
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter contact name to delete: ");
                    String deleteName = scanner.nextLine();
                    phoneDirectory.deleteContact(deleteName);
                    System.out.println("Contact deleted successfully.");
                    break;
                case 5:
                    System.out.println("All Contacts:");
                    phoneDirectory.displayAllContacts();
                    break;
                case 6:
                    System.out.println("Exiting the program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
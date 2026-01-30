package frontend;

import backend.CatalogService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CatalogService service = new CatalogService();

        while (true) {
            System.out.println("\n--- Catalog Menu ---");
            System.out.println("1. View Items");
            System.out.println("2. Add Items");
            System.out.println("3. Edit Items");
            System.out.println("4. Exit");
            System.out.println("Please enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    service.showItems();
                    break;

                case "2":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Description: ");
                    String desc = scanner.nextLine();

                    service.addItem(name, desc);
                    break;

                case "3":
                    System.out.print("Enter ID: ");
                    String ID = scanner.nextLine();

                    System.out.print("New Name: ");
                    String newName = scanner.nextLine();

                    System.out.print("New Description: ");
                    String newDesc = scanner.nextLine();

                    service.editItem(ID, newName, newDesc);
                    break;

                case "4":
                    System.out.println("GoodBye!");
                    return;

                default:
                    System.out.println("Invalid. Please choose again");
            }
        }
    }
}
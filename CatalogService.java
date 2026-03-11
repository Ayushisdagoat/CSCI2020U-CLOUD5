package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogService {

    private List<Item> items = new ArrayList<>();

    public void showItems() {
        if (items.isEmpty()) {
            System.out.println("No items in catalog.");
            return;
        }

        System.out.println("\n--- Catalog Items ---");
        for (Item item : items) {
            System.out.println("ID: " + item.getId());
            System.out.println("Name: " + item.getName());
            System.out.println("Description: " + item.getDescription());
            System.out.println("--------------------");
        }
    }

    public void addItem(String name, String description) {
        if (!isValid(name, description)) {
            System.out.println("Error: Name and description cannot be empty.");
            return;
        }

        String id = UUID.randomUUID().toString();
        items.add(new Item(id, name, description));
        System.out.println("Item added successfully.");
    }

    public void editItem(String id, String newName, String newDescription) {
        if (!isValid(newName, newDescription)) {
            System.out.println("Error: Name and description cannot be empty.");
            return;
        }

        for (Item item : items) {
            if (item.getId().equals(id)) {
                item.setName(newName);
                item.setDescription(newDescription);
                System.out.println("Item updated successfully.");
                return;
            }
        }

        System.out.println("Error: Item with given ID not found.");
    }

    private boolean isValid(String name, String description) {
        return name != null && !name.trim().isEmpty()
                && description != null && !description.trim().isEmpty();
    }
}

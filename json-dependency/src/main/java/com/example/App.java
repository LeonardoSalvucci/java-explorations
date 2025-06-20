package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class App {
    private static final String JSON_FILENAME = "person.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<Person> people = new ArrayList<Person>();

    private static void showMenu() {
        System.out.println("1. Add Person");
        System.out.println("2. List People");
        System.out.println("3. Save to JSON");
        System.out.println("4. Load from JSON");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // Clear the invalid input
        }
        return scanner.nextInt();
    }

    private static void addPerson(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter age: ");
        int age = getIntInput(scanner);
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        Person person = new Person(name, age, email);
        people.add(person);
        System.out.println("✅ Person added: " + person);
    }

    private static void displayPeople() {
        if (people.isEmpty()) {
            System.out.println("No people added yet.");
        } else {
            System.out.println("List of People:");
            for (Person person : people) {
                System.out.println(person);
            }
        }
    }

    private static void savePeopleToFile() {
        try {
            objectMapper.writeValue(new File(JSON_FILENAME), people);
            System.out.println("💾 People saved to " + JSON_FILENAME);

            String jsonString = objectMapper.writeValueAsString(people);
            System.out.println("JSON String: ");
            System.out.println(jsonString);
        } catch(IOException e) {
            System.err.println("❌ Error saving to JSON: " + e.getMessage());
        }
    }

    private static void loadPeopleFromFile() {
        try {
            File file = new File(JSON_FILENAME);
            if (!file.exists()) {
                System.out.println("❌ JSON file not found: " + JSON_FILENAME + ". Starting fresh!");
                people = new ArrayList<Person>();
                return;
            }

            TypeFactory typeFactory = objectMapper.getTypeFactory();
            people = objectMapper.readValue(file, typeFactory.constructCollectionType(List.class, Person.class));
            System.out.println("📥  Loaded " + people.size() + " people from " + JSON_FILENAME);
        } catch (IOException e) {
            System.err.println("❌ Error loading from JSON: " + e.getMessage());
            people = new ArrayList<Person>();
        }
    }

    public static void main(String[] args) {
        System.out.println("🔧 Jackson JSON Dependency Example");
        System.out.println("==================================");
        
        // Load existing data
        loadPeopleFromFile();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            showMenu();
            int choice = getIntInput(scanner);
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    addPerson(scanner);
                    break;
                case 2:
                    displayPeople();
                    break;
                case 3:
                    savePeopleToFile();
                    break;
                case 4:
                    loadPeopleFromFile();
                    break;
                case 5:
                    System.out.println("👋 Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}

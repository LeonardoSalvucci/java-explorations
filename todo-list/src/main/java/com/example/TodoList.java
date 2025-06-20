package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoList {
  private List<Task> tasks;
  private int nextId;
  private Scanner scanner;

  public TodoList() {
    this.tasks = new ArrayList<>();
    this.nextId = 1;
    this.scanner = new Scanner(System.in);
  }

  // Add a new task
  public void addTask(String description) {
    Task newTask = new Task(nextId++, description, false);
    tasks.add(newTask);
    System.out.println("✅ Task added: " + description);
  }

  // Display all tasks
  public void displayTasks() {
    if (tasks.isEmpty()) {
      System.out.println("📋 No tasks found. Your todo list is empty!");
      return;
    }

    System.out.println("\n📋 Your Todo List:");
    System.out.println("-------------------");
    for (Task task : tasks) {
      System.out.println(task);
    }
    System.out.println("");
  }

  // Find task by ID
  public Task findTaskById(int id) {
    for (Task task : tasks) {
      if (task.getId() == id) {
        return task;
      }
    }
    return null;
  }

  // Edit a task description
  public void editTask(int id, String description) {
    Task task = findTaskById(id);
    if (task != null) {
      task.setDescription(description);
      System.out.println("✅ Task updated: " + task);
    } else {
      System.out.println("❌ Task not found with ID: " + id);
    }
  }

  // Toggle task
  public void toggleTaskStatus(int id) {
    Task task = findTaskById(id);
    if(task != null) {
      task.setDone(!task.isDone());
      String status = task.isDone() ? "✅" : "❌";
      System.out.println("🔄 Task status updated: [" + id + "] " + status + " " + task.getDescription());
    } else {
      System.out.println("❌ Task not found with ID: " + id);
    }
  }

  // Remove a task
  public void deleteTask(int id) {
    Task task = findTaskById(id);
    if (task != null) {
      tasks.remove(task);
      System.out.println("🗑️ Task deleted: " + task);
    } else {
      System.out.println("❌ Task not found with ID: " + id);
    }
  }

  // Display menu
  private void displayMenu() {
      System.out.println("\n=== TODO LIST MENU ===");
      System.out.println("1. Add Task");
      System.out.println("2. View All Tasks");
      System.out.println("3. Edit Task");
      System.out.println("4. Toggle Task Status");
      System.out.println("5. Delete Task");
      System.out.println("6. Exit");
      System.out.print("Choose an option (1-6): ");
  }

  // validate user input
  private int getIntInput() {
    while(!scanner.hasNextInt()) {
      System.out.print("Please enter a valid number: ");
      scanner.next();
    }
    return scanner.nextInt();
  }

  public void run() {
    System.out.println("🎯 Welcome to Java Todo List!");
    System.out.println("============================");

    while(true) {
      displayMenu();
      int choice = getIntInput();
      scanner.nextLine();

      switch(choice) {
        case 1:
          System.out.println("Enter task description: ");
          String description = scanner.nextLine();
          if (!description.trim().isEmpty()) {
            addTask(description.trim());
          } else {
            System.out.println("❌ Task description cannot be empty!");
          }
          break;
        
        case 2:
          displayTasks();
          break;

        case 3:
          System.out.print("Enter task ID to edit: ");
          int editId = getIntInput();
          scanner.nextLine();
          System.out.println("Enter the new Description: ");
          String newDescription = scanner.nextLine();
          if (!newDescription.trim().isEmpty()) {
            editTask(editId, newDescription.trim());
          } else {
            System.out.println("❌ Task description cannot be empty!");
          }
          break;

        case 4:
          System.out.print("Enter task ID to toggle status: ");
          int toggleId = getIntInput();
          toggleTaskStatus(toggleId);
          break;

        case 5:
          System.out.print("Enter task ID to delete: ");
          int deleteId = getIntInput();
          deleteTask(deleteId);
          break;

        case 6:
          System.out.println("👋 Goodbye! Thanks for using the Todo List.");
          scanner.close();
          return; // to actually exit the program

        default:
          System.out.println("❌ Invalid choice! Please select a valid option.");
          break;
      }
    }
  }

  public static void main(String[] args) {
    TodoList todoList = new TodoList();
    todoList.run();
  }
}

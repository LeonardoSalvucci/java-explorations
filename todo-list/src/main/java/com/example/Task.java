package com.example;

public class Task {
  private int id;
  private String description;
  private boolean isDone;

  public Task(int id, String description, boolean isDone) {
    this.id = id;
    this.description = description;
    this.isDone = isDone;
  }

  // Getters
  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDone() {
    return isDone;
  }

  // Setters
  public void setDescription(String description) {
    this.description = description;
  }

  public void setDone(boolean done) {
    this.isDone = done;
  }

  @Override
  public String toString() {
    String status = isDone ? "✅": "❌";
    return String.format("[%d] %s %s", id, status, description);
  }
}

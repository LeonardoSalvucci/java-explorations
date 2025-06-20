package com.example;

public class Person {
  public String name;
  public Integer age;

  public Person() {}
  public Person(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return this.name.toString() + ": age " + this.age.toString();
  }
}

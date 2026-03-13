package app;

import database.Database;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program started");
        Database.initializeDatabase();

        // Database.addStudent("AI001", "Bolivar", "Applied Informatics");
        // Database.addStudent("AI002", "James", "Economic Mathematic");
        // Database.addStudent("AI003", "Anna", "Applied Informatics");
        Database.addStudent("AI004", "Paul", "Applied Informatics");
        Database.listStudents();
    }
}
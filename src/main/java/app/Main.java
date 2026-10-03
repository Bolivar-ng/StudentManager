package app;

import java.util.Scanner;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.sql.SQLException;
import service.StudentService;
import database.Database;
import model.Student;
import model.Grade;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();

        try {
            Database.initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Fatal error: could not initialize database — " + e.getMessage());
            return;
        }

        int choice;
        do {
            System.out.println();
            System.out.println("===== Student Manager =====");
            System.out.println("1 - Add student");
            System.out.println("2 - List students");
            System.out.println("3 - Update student program");
            System.out.println("4 - Delete student");
            System.out.println("5 - Add grade");
            System.out.println("6 - List grades");
            System.out.println("7 - Calculate average");
            System.out.println("0 - Exit");

            choice = readInt(sc, "Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter matricule: ");
                        String matricule = sc.nextLine().trim();
                        System.out.print("Enter name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Enter program: ");
                        String program = sc.nextLine().trim();

                        boolean added = studentService.addStudent(matricule, name, program);
                        System.out.println(added ? "Student added successfully!" : "Could not add student.");
                    }

                    case 2 -> {
                        List<Student> students = studentService.listStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter matricule of the student to update: ");
                        String matricule = sc.nextLine().trim();
                        System.out.print("Enter new program: ");
                        String newProgram = sc.nextLine().trim();

                        boolean updated = studentService.updateStudentProgram(matricule, newProgram);
                        System.out.println(updated ? "Student updated successfully!" : "No student found with this matricule.");
                    }

                    case 4 -> {
                        System.out.print("Enter matricule of the student to delete: ");
                        String matricule = sc.nextLine().trim();

                        boolean deleted = studentService.deleteStudent(matricule);
                        System.out.println(deleted ? "Student deleted successfully!" : "No student found with this matricule.");
                    }

                    case 5 -> {
                        System.out.print("Enter matricule: ");
                        String matricule = sc.nextLine().trim();
                        System.out.print("Enter module: ");
                        String module = sc.nextLine().trim();
                        double grade = readDouble(sc, "Enter grade (0–20): ");

                        boolean added = studentService.addGrade(matricule, module, grade);
                        System.out.println(added ? "Grade added successfully!" : "No student found with this matricule.");
                    }

                    case 6 -> {
                        System.out.print("Enter matricule: ");
                        String matricule = sc.nextLine().trim();

                        List<Grade> grades = studentService.listGrades(matricule);
                        if (grades.isEmpty()) {
                            System.out.println("No grades found for this student.");
                        } else {
                            Optional<Student> student = studentService.findStudentByMatricule(matricule);
                            String studentName = student.map(Student::getName).orElse(matricule);
                            grades.forEach(g -> System.out.println(studentName + " | " + g));
                        }
                    }

                    case 7 -> {
                        System.out.print("Enter matricule: ");
                        String matricule = sc.nextLine().trim();

                        OptionalDouble avg = studentService.calculateAverage(matricule);
                        if (avg.isPresent()) {
                            System.out.printf("Average grade: %.2f%n", avg.getAsDouble());
                        } else {
                            System.out.println("No grades found for this student.");
                        }
                    }

                    case 0 -> System.out.println("Exiting program...");

                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
package app;

import java.util.Scanner;
import service.StudentService;
import database.Database;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();

        Database.initializeDatabase();

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

            // readInt statt sc.nextInt() — kein Crash mehr
            choice = readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter matricule: ");
                    String matricule = sc.nextLine().trim();

                    System.out.print("Enter name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter program: ");
                    String program = sc.nextLine().trim();

                    studentService.addStudent(matricule, name, program);
                    break;

                case 2:
                    studentService.listStudents();
                    break;

                case 3:
                    System.out.print("Enter matricule of the student to update: ");
                    String updateMatricule = sc.nextLine().trim();

                    System.out.print("Enter new program: ");
                    String newProgram = sc.nextLine().trim();

                    studentService.updateStudentProgram(updateMatricule, newProgram);
                    break;

                case 4:
                    System.out.print("Enter matricule of the student to delete: ");
                    String deleteMatricule = sc.nextLine().trim();

                    studentService.deleteStudent(deleteMatricule);
                    break;

                case 5:
                    System.out.print("Enter matricule: ");
                    String gradeMatricule = sc.nextLine().trim();

                    System.out.print("Enter module: ");
                    String module = sc.nextLine().trim();

                    // readDouble statt sc.nextDouble() — kein Crash mehr
                    double grade = readDouble(sc, "Enter grade (0–20): ");

                    studentService.addGrade(gradeMatricule, module, grade);
                    break;

                case 6:
                    System.out.print("Enter matricule: ");
                    String listGradesMatricule = sc.nextLine().trim();

                    studentService.listGrades(listGradesMatricule);
                    break;

                case 7:
                    System.out.print("Enter matricule: ");
                    String averageMatricule = sc.nextLine().trim();

                    studentService.calculateAverage(averageMatricule);
                    break;

                case 0:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);

        sc.close();
    }

    // --- Helper methods ---

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
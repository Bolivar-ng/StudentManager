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
            System.out.print("Choose an option: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter matricule: ");
                    String matricule = sc.nextLine();

                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter program: ");
                    String program = sc.nextLine();

                    studentService.addStudent(matricule, name, program);
                    break;

                case 2:
                    studentService.listStudents();
                    break;

                case 3:
                    System.out.print("Enter matricule of the student to update: ");
                    String updateMatricule = sc.nextLine();

                    System.out.print("Enter new program: ");
                    String newProgram = sc.nextLine();

                    studentService.updateStudentProgram(updateMatricule, newProgram);
                    break;

                case 4:
                    System.out.print("Enter matricule of the student to delete: ");
                    String deleteMatricule = sc.nextLine();

                    studentService.deleteStudent(deleteMatricule);
                    break;

                case 5:
                    System.out.print("Enter matricule: ");
                    String gradeMatricule = sc.nextLine();

                    System.out.print("Enter module: ");
                    String module = sc.nextLine();

                    System.out.print("Enter grade: ");
                    double grade = sc.nextDouble();
                    sc.nextLine();

                    studentService.addGrade(gradeMatricule, module, grade);
                    break;

                case 6:
                    System.out.print("Enter matricule: ");
                    String listGradesMatricule = sc.nextLine();

                    studentService.listGrades(listGradesMatricule);
                    break;

                case 7:
                    System.out.print("Enter matricule: ");
                    String averageMatricule = sc.nextLine();

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
}
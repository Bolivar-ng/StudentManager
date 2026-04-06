package service;

import database.Database;
import model.Student;

public class StudentService {

    public void addStudent(String matricule, String name, String program) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        if (program == null || program.trim().isEmpty()) {
            System.out.println("Program cannot be empty.");
            return;
        }

        Database.addStudent(matricule.trim(), name.trim(), program.trim());
    }

    public void listStudents() {
        Database.listStudents();
    }

    public void updateStudentProgram(String matricule, String newProgram) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        if (newProgram == null || newProgram.trim().isEmpty()) {
            System.out.println("New program cannot be empty.");
            return;
        }

        Database.updateStudentProgram(matricule.trim(), newProgram.trim());
    }

    public void deleteStudent(String matricule) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        Database.deleteStudent(matricule.trim());
    }
    public void addGrade(String matricule, String module, double grade) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        if (module == null || module.trim().isEmpty()) {
            System.out.println("Module cannot be empty.");
            return;
        }

        if (grade < 0 || grade > 20) {
            System.out.println("Grade must be between 0 and 20.");
            return;
        }

        Database.addGrade(matricule.trim(), module.trim(), grade);
    }

    public void listGrades(String matricule) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        Database.listGrades(matricule.trim());
    }

    public void calculateAverage(String matricule) {

        if (matricule == null || matricule.trim().isEmpty()) {
            System.out.println("Matricule cannot be empty.");
            return;
        }

        Database.calculateAverage(matricule.trim());
    }
}
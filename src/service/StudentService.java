package service;

import database.Database;
import model.Student;

public class StudentService {

    public void addStudent(String matricule, String name, String program) {
        Database.addStudent(matricule, name, program);
    }

    public void listStudents() {
        Database.listStudents();
    }

    public void updateStudentProgram(String matricule, String newProgram) {
        Database.updateStudentProgram(matricule, newProgram);
    }

    public void deleteStudent(String matricule) {
        Database.deleteStudent(matricule);
    }
}
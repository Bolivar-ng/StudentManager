package service;

import database.Database;
import model.Student;
import model.Grade;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class StudentService {

    public boolean addStudent(String matricule, String name, String program) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        requireNonBlank(name, "Name");
        requireNonBlank(program, "Program");
        try {
            return Database.addStudent(matricule.trim(), name.trim(), program.trim());
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                throw new IllegalArgumentException("Matricule already exists: " + matricule);
            }
            throw e;
        }
    }

    public List<Student> listStudents() throws SQLException {
        return Database.findAllStudents();
    }

    public Optional<Student> findStudentByMatricule(String matricule) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        return Database.findStudentByMatricule(matricule.trim());
    }

    public boolean updateStudentProgram(String matricule, String newProgram) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        requireNonBlank(newProgram, "New program");
        return Database.updateStudentProgram(matricule.trim(), newProgram.trim());
    }

    public boolean deleteStudent(String matricule) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        return Database.deleteStudent(matricule.trim());
    }

    public boolean addGrade(String matricule, String module, double grade) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        requireNonBlank(module, "Module");
        if (grade < 0 || grade > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20.");
        }
        return Database.addGrade(matricule.trim(), module.trim(), grade);
    }

    public List<Grade> listGrades(String matricule) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        return Database.findGradesByMatricule(matricule.trim());
    }

    public OptionalDouble calculateAverage(String matricule) throws SQLException {
        requireNonBlank(matricule, "Matricule");
        return Database.calculateAverage(matricule.trim());
    }

    private void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}
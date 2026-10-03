package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import model.Student;
import model.Grade;

public class Database {

    private static final String URL = "jdbc:sqlite:studentmanager.db";

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement pragma = conn.createStatement()) {
            pragma.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void initializeDatabase() throws SQLException {
        String createStudentsTable =
                "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "matricule TEXT NOT NULL UNIQUE, " +
                "name TEXT NOT NULL, " +
                "program TEXT NOT NULL" +
                ");";

        String createGradesTable =
                "CREATE TABLE IF NOT EXISTS grades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "module TEXT NOT NULL, " +
                "grade REAL NOT NULL, " +
                "FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createGradesTable);
        }
    }

    public static boolean addStudent(String matricule, String name, String program) throws SQLException {
        String sql = "INSERT INTO students(matricule, name, program) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            pstmt.setString(2, name);
            pstmt.setString(3, program);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static List<Student> findAllStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        List<Student> result = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Student(
                    rs.getInt("id"),
                    rs.getString("matricule"),
                    rs.getString("name"),
                    rs.getString("program")));
            }
        }
        return result;
    }

    public static Optional<Student> findStudentByMatricule(String matricule) throws SQLException {
        String sql = "SELECT * FROM students WHERE matricule = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Student(
                        rs.getInt("id"), rs.getString("matricule"),
                        rs.getString("name"), rs.getString("program")));
                }
                return Optional.empty();
            }
        }
    }

    public static boolean deleteStudent(String matricule) throws SQLException {
        String sql = "DELETE FROM students WHERE matricule = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean updateStudentProgram(String matricule, String newProgram) throws SQLException {
        String sql = "UPDATE students SET program = ? WHERE matricule = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newProgram);
            pstmt.setString(2, matricule);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean addGrade(String matricule, String module, double grade) throws SQLException {
        String sql = "INSERT INTO grades(student_id, module, grade) " +
                     "SELECT id, ?, ? FROM students WHERE matricule = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, module);
            pstmt.setDouble(2, grade);
            pstmt.setString(3, matricule);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static List<Grade> findGradesByMatricule(String matricule) throws SQLException {
        String sql = "SELECT grades.id, grades.student_id, grades.module, grades.grade " +
                     "FROM students JOIN grades ON students.id = grades.student_id " +
                     "WHERE students.matricule = ?";
        List<Grade> result = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new Grade(
                        rs.getInt("id"), rs.getInt("student_id"),
                        rs.getString("module"), rs.getDouble("grade")));
                }
            }
        }
        return result;
    }

    public static OptionalDouble calculateAverage(String matricule) throws SQLException {
        String sql = "SELECT AVG(grades.grade) AS average_grade " +
                     "FROM students JOIN grades ON students.id = grades.student_id " +
                     "WHERE students.matricule = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("average_grade");
                    return rs.wasNull() ? OptionalDouble.empty() : OptionalDouble.of(avg);
                }
                return OptionalDouble.empty();
            }
        }
    }
}
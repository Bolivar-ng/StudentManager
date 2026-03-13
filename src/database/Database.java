package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Database {

    private static final String URL = "jdbc:sqlite:studentmanager.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
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
                "FOREIGN KEY(student_id) REFERENCES students(id)" +
                ");";

        Connection conn = connect();

        if (conn == null) {
            System.out.println("Database connection failed.");
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(createStudentsTable);
            stmt.execute(createGradesTable);
            System.out.println("Tables created successfully!");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void addStudent(String matricule, String name, String program) {
        String sql = "INSERT INTO students(matricule, name, program) VALUES(?, ?, ?)";

        Connection conn = connect();

        if (conn == null) {
            System.out.println("Cannot add student: database connection failed.");
            return;
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matricule);
            pstmt.setString(2, name);
            pstmt.setString(3, program);

            pstmt.executeUpdate();
            System.out.println("Student added successfully!");

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }

    public static void listStudents() {
        String sql = "SELECT * FROM students";

        Connection conn = connect();

        if (conn == null) {
            System.out.println("Database connection failed.");
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("matricule") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("program")
                );
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
    }
    public static void deleteStudent(String matricule) {

        String sql = "DELETE FROM students WHERE matricule = ?";

        Connection conn = connect();

        if (conn == null) {
            System.out.println("Database connection failed.");
            return;
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matricule);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with this matricule.");
            }

            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
    public static void updateStudentProgram(String matricule, String newProgram) {

        String sql = "UPDATE students SET program = ? WHERE matricule = ?";

        Connection conn = connect();

        if (conn == null) {
            System.out.println("Database connection failed.");
            return;
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newProgram);
            pstmt.setString(2, matricule);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No student found with this matricule.");
            }

            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }
}
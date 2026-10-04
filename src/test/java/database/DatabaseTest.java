package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Student;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static final String TEST_DB = "test-studentmanager.db";

    @BeforeEach
    void setUp() throws SQLException {
        Database.setUrlForTesting("jdbc:sqlite:" + TEST_DB);
        Database.initializeDatabase();
    }

    @AfterEach
    void tearDown() {
        new File(TEST_DB).delete();
    }

    @Test
    void addStudent_shouldReturnTrue_whenDataIsValid() throws SQLException {
        boolean result = Database.addStudent("S001", "Amina Diallo", "Informatik");
        assertTrue(result);
    }

    @Test
    void addStudent_shouldFail_whenMatriculeAlreadyExists() throws SQLException {
        Database.addStudent("S002", "Lukas Meier", "Informatik");

        assertThrows(SQLException.class, () ->
            Database.addStudent("S002", "Other Name", "Other Program")
        );
    }

    @Test
    void findAllStudents_shouldReturnEmptyList_whenNoStudents() throws SQLException {
        List<Student> students = Database.findAllStudents();
        assertTrue(students.isEmpty());
    }

    @Test
    void findAllStudents_shouldReturnStudent_afterAdding() throws SQLException {
        Database.addStudent("S003", "Fatou Ndiaye", "Informatik");
        List<Student> students = Database.findAllStudents();
        assertEquals(1, students.size());
        assertEquals("S003", students.get(0).getMatricule());
    }

    @Test
    void findStudentByMatricule_shouldReturnEmpty_whenNotFound() throws SQLException {
        Optional<Student> result = Database.findStudentByMatricule("DOES_NOT_EXIST");
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteStudent_shouldCascadeDeleteGrades() throws SQLException {
        Database.addStudent("S004", "Jonas Weber", "Elektrotechnik");
        Database.addGrade("S004", "Mathematik 2", 2.0);

        boolean deleted = Database.deleteStudent("S004");

        assertTrue(deleted);
        assertTrue(Database.findGradesByMatricule("S004").isEmpty());
    }
}
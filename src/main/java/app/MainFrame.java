package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import database.Database;
import service.StudentService;
import model.Student;
import model.Grade;

public class MainFrame extends JFrame {

    private final StudentService studentService = new StudentService();
    private DefaultTableModel tableModel;
    private JTable table;

    public MainFrame() {
        setTitle("Student Manager");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        String[] columns = {"Matricule", "Name", "Program"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(this::onAddStudent);
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(this::onDeleteStudent);
        buttonPanel.add(deleteButton);

        JButton updateButton = new JButton("Update Program");
        updateButton.addActionListener(this::onUpdateProgram);
        buttonPanel.add(updateButton);

        JButton gradesButton = new JButton("Grades");
        gradesButton.addActionListener(this::onOpenGrades);
        buttonPanel.add(gradesButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadStudents());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try {
            List<Student> students = studentService.listStudents();
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getMatricule(), s.getName(), s.getProgram()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedMatricule() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) tableModel.getValueAt(row, 0);
    }

    private void onAddStudent(ActionEvent e) {
        JTextField matriculeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField programField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Matricule:"));
        panel.add(matriculeField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Program:"));
        panel.add(programField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                studentService.addStudent(matriculeField.getText(), nameField.getText(), programField.getText());
                loadStudents();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDeleteStudent(ActionEvent e) {
        String matricule = getSelectedMatricule();
        if (matricule == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student " + matricule + "? This will also delete their grades.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentService.deleteStudent(matricule);
                loadStudents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onUpdateProgram(ActionEvent e) {
        String matricule = getSelectedMatricule();
        if (matricule == null) return;

        int row = table.getSelectedRow();
        String currentProgram = (String) tableModel.getValueAt(row, 2);

        String newProgram = JOptionPane.showInputDialog(this,
                "New program for " + matricule + ":", currentProgram);

        if (newProgram != null) {
            try {
                studentService.updateStudentProgram(matricule, newProgram);
                loadStudents();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onOpenGrades(ActionEvent e) {
        String matricule = getSelectedMatricule();
        if (matricule == null) return;

        new GradesDialog(this, studentService, matricule).setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Database.initializeDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Could not initialize database: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

import service.StudentService;
import model.Grade;

public class GradesDialog extends JDialog {

    private final StudentService studentService;
    private final String matricule;
    private DefaultTableModel tableModel;
    private JLabel averageLabel;

    public GradesDialog(Frame owner, StudentService studentService, String matricule) {
        super(owner, "Grades — " + matricule, true);
        this.studentService = studentService;
        this.matricule = matricule;

        setSize(450, 350);
        setLocationRelativeTo(owner);

        initComponents();
        loadGrades();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        String[] columns = {"Module", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        averageLabel = new JLabel("Average: —");
        topPanel.add(averageLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        JButton addGradeButton = new JButton("Add Grade");
        addGradeButton.addActionListener(e -> onAddGrade());
        buttonPanel.add(addGradeButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGrades() {
        tableModel.setRowCount(0);
        try {
            List<Grade> grades = studentService.listGrades(matricule);
            for (Grade g : grades) {
                tableModel.addRow(new Object[]{g.getModule(), String.format("%.2f", g.getGrade())});
            }

            OptionalDouble avg = studentService.calculateAverage(matricule);
            averageLabel.setText(avg.isPresent()
                    ? String.format("Average: %.2f", avg.getAsDouble())
                    : "Average: no grades yet");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading grades: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddGrade() {
        JTextField moduleField = new JTextField();
        JTextField gradeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Module:"));
        panel.add(moduleField);
        panel.add(new JLabel("Grade (1.0-5.0):"));
        panel.add(gradeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Grade",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double grade = Double.parseDouble(gradeField.getText().trim());
                studentService.addGrade(matricule, moduleField.getText(), grade);
                loadGrades();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Grade must be a valid number.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
package model;

public class Grade {

    private final int id;
    private final int studentId;
    private final String module;
    private final double grade;

    public Grade(int id, int studentId, String module, double grade) {
        if (module == null || module.trim().isEmpty()) {
            throw new IllegalArgumentException("Module cannot be empty.");
        }
        if (grade < 0 || grade > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20.");
        }
        this.id = id;
        this.studentId = studentId;
        this.module = module;
        this.grade = grade;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public String getModule() { return module; }
    public double getGrade() { return grade; }

    @Override
    public String toString() {
        return String.format("Module: %s | Grade: %.2f", module, grade);
    }
}
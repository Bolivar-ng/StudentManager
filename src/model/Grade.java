package model;

public class Grade {

    private int id;
    private int studentId;
    private String module;
    private double grade;

    public Grade(int id, int studentId, String module, double grade) {
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
        return "Module: " + module + " | Grade: " + grade;
    }
}
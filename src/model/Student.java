package model;

public class Student {

    private int id;
    private String matricule;
    private String name;
    private String program;

    public Student(int id, String matricule, String name, String program) {
        this.id = id;
        this.matricule = matricule;
        this.name = name;
        this.program = program;
    }

    public Student(String matricule, String name, String program) {
        this.matricule = matricule;
        this.name = name;
        this.program = program;
    }

    public int getId() {
        return id;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return id + " | " + matricule + " | " + name + " | " + program;
    }
}
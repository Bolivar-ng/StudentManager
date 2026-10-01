package model;

public class Student {

    private final int id;
    private final String matricule;
    private final String name;
    private String program;

    public Student(int id, String matricule, String name, String program) {
        if (matricule == null || matricule.trim().isEmpty()) {
            throw new IllegalArgumentException("Matricule cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.id = id;
        this.matricule = matricule;
        this.name = name;
        setProgram(program);
    }

    public Student(String matricule, String name, String program) {
        this(0, matricule, name, program);
    }

    public int getId() { return id; }
    public String getMatricule() { return matricule; }
    public String getName() { return name; }
    public String getProgram() { return program; }

    public void setProgram(String program) {
        if (program == null || program.trim().isEmpty()) {
            throw new IllegalArgumentException("Program cannot be empty.");
        }
        this.program = program.trim();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s", matricule, name, program);
    }
}
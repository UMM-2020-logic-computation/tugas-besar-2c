public class User {
    private String nim;
    private String name;
    private String major;
    private String grade;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String major, String grade) {
        this.setName(name);
        this.setMajor(major);
        this.setGrade(grade);
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void clearUser() {
        this.setNim(null);
        this.setName(null);
        this.setMajor(null);
        this.setGrade(null);
    }
}

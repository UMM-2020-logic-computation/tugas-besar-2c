public class Tasks {
    private String title;
    private String deadline;
    private String status;

    public Tasks(String title, String deadline, String status) {
        this.setTitle(title);
        this.setStatus(status);
        this.setDeadline(deadline);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

public class Tasks {
    private String title;
    private String deadline;
    private String status;
    private String id;

    public Tasks() {
        // Default constructor required for calls to DataSnapshot.getValue(Tasks.class)
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

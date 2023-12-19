package taskmanagement.tasks;

public interface TaskView {
    String getId();
    String getTitle();
    String getDescription();
    TaskStatus getStatus();
    String getAuthor();
    String getAssignee();
    int getTotalComments();
}

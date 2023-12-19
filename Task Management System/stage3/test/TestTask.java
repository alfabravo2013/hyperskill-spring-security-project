public class TestTask {
    private final String title;
    private final String description;
    private String status;
    private String author;

    private TestTask(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static TestTask task1() {
        return new TestTask("title 1", "description 1");
    }

    public static TestTask task2() {
        return new TestTask("title 2", "description 2");
    }

    public static TestTask task3() {
        return new TestTask("title 3", "description 3");
    }

    public TestTask withTitle(String title) {
        var copy = new TestTask(title, this.description);
        copy.setStatus(this.status);
        copy.setAuthor(this.author);
        return copy;
    }

    public TestTask withDescription(String description) {
        var copy = new TestTask(this.title, description);
        copy.setStatus(this.status);
        copy.setAuthor(this.author);
        return copy;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

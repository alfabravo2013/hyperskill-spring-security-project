package taskmanagement.tasks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TasksRestController {
    @GetMapping("/api/tasks")
    public void getTasks() {

    }
}

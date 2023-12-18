package taskmanagement.tasks;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TasksRestController {
    private final TaskService taskService;

    public TasksRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/tasks")
    public TaskDto createTask(@Valid @RequestBody TaskCreateRequest request) {
        return mapTaskToTaskDto(taskService.createTask(request));
    }

    @GetMapping("/api/tasks")
    public List<TaskDto> getTasks(@RequestParam(required = false) String author) {
        List<Task> tasks;
        if (author == null) {
            tasks = taskService.findAll();
        } else {
            tasks = taskService.findAllByAuthor(author);
        }

        return tasks.stream().map(this::mapTaskToTaskDto).toList();
    }

    private TaskDto mapTaskToTaskDto(Task task) {
        return new TaskDto(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getAuthor().getEmail()
        );
    }
}

package taskmanagement.tasks;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<TaskViewDto> getTasks(@RequestParam(required = false) String author,
                                  @RequestParam(required = false) String assignee) {
        Map<String, String> filters = new HashMap<>(2);
        if (author != null) {
            filters.put("author", author);
        }
        if (assignee != null) {
            filters.put("assignee", assignee);
        }

        List<TaskView> tasks = taskService.findTaskViews(filters);

        return tasks.stream().map(this::mapTaskViewToTaskViewDto).toList();
    }

    @PutMapping("/api/tasks/{taskId}/assign")
    public TaskDto updateAssignee(@PathVariable String taskId,
                                  @Valid @RequestBody TaskAssigneeUpdateRequest request) {
        var task = taskService.updateAssignee(taskId, request);
        return mapTaskToTaskDto(task);
    }

    @PutMapping("/api/tasks/{taskId}/status")
    public TaskDto updateStatus(@PathVariable String taskId,
                                @Valid @RequestBody TaskStatusUpdateRequest request) {
        var task = taskService.updateStatus(taskId, request);
        return mapTaskToTaskDto(task);
    }

    private TaskDto mapTaskToTaskDto(Task task) {
        return new TaskDto(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getAuthor().getEmail(),
                task.getAssignee() == null ? "none" : task.getAssignee().getEmail()
        );
    }

    private TaskViewDto mapTaskViewToTaskViewDto(TaskView taskView) {
        return new TaskViewDto(
                taskView.getId(),
                taskView.getTitle(),
                taskView.getDescription(),
                taskView.getStatus().name(),
                taskView.getAuthor(),
                taskView.getAssignee() == null ? "none" : taskView.getAssignee(),
                taskView.getTotalComments()
        );
    }
}

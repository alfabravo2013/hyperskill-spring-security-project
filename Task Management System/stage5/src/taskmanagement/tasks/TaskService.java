package taskmanagement.tasks;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanagement.account.AccountService;
import taskmanagement.common.ApiException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TaskService {
    private final AccountService accountService;
    private final TaskRepository taskRepository;

    public TaskService(AccountService accountService, TaskRepository taskRepository) {
        this.accountService = accountService;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(TaskCreateRequest request) {
        var author = accountService.getCurrentUser();
        var task = new Task(
                request.title(),
                request.description(),
                TaskStatus.CREATED,
                author
        );

        return taskRepository.save(task);
    }

    public List<TaskView> findTaskViews(Map<String, String> filters) {
        String authorEmail = filters.get("author");
        String assigneeEmail = filters.get("assignee");
        return taskRepository.findAllByViewsAuthorEmail(authorEmail, assigneeEmail);
    }

    @Transactional
    public Task updateAssignee(String taskId, TaskAssigneeUpdateRequest request) {
        var task = findTaskById(taskId);

        var user = accountService.getCurrentUser();
        if (!Objects.equals(task.getAuthor().getId(), user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        var email = request.assignee();
        if ("none".equals(email)) {
            task.setAssignee(null);
        } else {
            var account = accountService.findAccountByEmail(request.assignee().toLowerCase())
                    .orElseThrow(() -> new ApiException(404, "Requested '%s' account not found".formatted(email)));
            task.setAssignee(account);
        }
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatus(String taskId, TaskStatusUpdateRequest request) {
        var task = findTaskById(taskId);

        var status = TaskStatus.valueOf(request.status());

        var currentUserId = accountService.getCurrentUser().getId();
        var taskAuthorId = task.getAuthor().getId();
        var taskAssigneeId = task.getAssignee() == null ? null : task.getAssignee().getId();

        if (!Objects.equals(currentUserId, taskAuthorId) && !Objects.equals(currentUserId, taskAssigneeId)) {
            throw new AccessDeniedException("Access denied");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task findTaskById(String taskId) {
        try {
            var id = Long.parseLong(taskId);
            return taskRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Requested '%s' task not found".formatted(taskId)));
        } catch (NumberFormatException e) {
            throw new ApiException(404, "Requested task '%s' not found".formatted(taskId));
        }
    }
}

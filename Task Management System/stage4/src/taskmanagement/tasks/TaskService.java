package taskmanagement.tasks;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
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

    public List<Task> findTasks(Map<String, String> filters) {
        var sort = Sort.by("createdAt").descending();

        if (filters.isEmpty()) {
            return taskRepository.findAll(sort);
        }

        var probe = new Task();

        var authorEmail = filters.get("author");
        if (authorEmail != null) {
            var author = accountService.findAccountByEmail(authorEmail).orElse(null);
            if (author == null) {
                return List.of();
            }
            probe.setAuthor(author);
        }

        var assigneeEmail = filters.get("assignee");
        if (assigneeEmail != null) {
            var assignee = accountService.findAccountByEmail(assigneeEmail).orElse(null);
            if (assignee == null) {
                return List.of();
            }
            probe.setAssignee(assignee);
        }

        var example = Example.of(probe);
        return taskRepository.findAll(example, sort);
    }

    @Transactional
    public Task updateAssignee(String taskId, TaskAssigneeUpdateRequest request) {
        try {
            var id = Long.parseLong(taskId);
            var task = taskRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Requested '%s' task not found".formatted(taskId)));

            var user = accountService.getCurrentUser();
            if (!Objects.equals(task.getAuthor().getId(), user.getId())) {
                throw new AccessDeniedException("Access denied");
            }

            var email =  request.assignee();
            if ("none".equals(email)) {
                task.setAssignee(null);
            } else {
                var account = accountService.findAccountByEmail(request.assignee().toLowerCase())
                        .orElseThrow(() -> new ApiException(404, "Requested '%s' account not found".formatted(email)));
                task.setAssignee(account);
            }
            return taskRepository.save(task);
        } catch (NumberFormatException e) {
            throw new ApiException(404, "Requested task '%s' not found".formatted(taskId));
        }
    }

    @Transactional
    public Task updateStatus(String taskId, TaskStatusUpdateRequest request) {
        try {
            var id = Long.parseLong(taskId);
            var task = taskRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, "Requested '%s' task not found".formatted(taskId)));

            var status = TaskStatus.valueOf(request.status());

            var currentUserId = accountService.getCurrentUser().getId();
            var taskAuthorId = task.getAuthor().getId();
            var taskAssigneeId = task.getAssignee() == null ? null : task.getAssignee().getId();

            if (!Objects.equals(currentUserId, taskAuthorId) && !Objects.equals(currentUserId, taskAssigneeId)) {
                throw new AccessDeniedException("Access denied");
            }

            task.setStatus(status);
            return taskRepository.save(task);
        } catch (NumberFormatException e) {
            throw new ApiException(404, "Requested task '%s' not found".formatted(taskId));
        }
    }
}

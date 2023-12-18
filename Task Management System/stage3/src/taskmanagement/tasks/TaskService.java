package taskmanagement.tasks;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import taskmanagement.account.AccountService;

import java.util.List;

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

    public List<Task> findAllByAuthor(String email) {
        return taskRepository.findAllByAuthorEmail(email.toLowerCase());
    }

    public List<Task> findAll() {
        var sort = Sort.by("createdAt").descending();
        return taskRepository.findAll(sort);
    }
}

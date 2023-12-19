package taskmanagement.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanagement.account.AccountService;
import taskmanagement.tasks.TaskService;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AccountService accountService;
    private final TaskService taskService;

    public CommentService(CommentRepository commentRepository,
                          AccountService accountService,
                          TaskService taskService) {
        this.commentRepository = commentRepository;
        this.accountService = accountService;
        this.taskService = taskService;
    }

    @Transactional
    public void createComment(String taskId, CommentCreateRequest request) {
        var task = taskService.findTaskById(taskId);
        var author = accountService.getCurrentUser();
        var comment = new Comment(request.text(), author, task);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByTaskId(String taskId) {
        var task = taskService.findTaskById(taskId);
        return commentRepository.findAllByTaskOrderByCreatedAtDesc(task);
    }
}

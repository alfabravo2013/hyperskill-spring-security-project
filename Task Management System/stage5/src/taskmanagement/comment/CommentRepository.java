package taskmanagement.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.tasks.Task;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskOrderByCreatedAtDesc(Task task);
}

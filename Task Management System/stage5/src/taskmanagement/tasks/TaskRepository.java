package taskmanagement.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
            SELECT t
            FROM Task t
            WHERE t.author.email = :email
            ORDER BY t.createdAt DESC
            """)
    List<Task> findAllByAuthorEmail(@Param("email") String email);

    @Query(value = """
            SELECT
                t.id AS id,
                t.title AS title,
                t.description AS description,
                t.status AS status,
                au.email AS author,
                an.email AS assignee,
                COUNT(c.id) AS totalComments
            FROM task AS t
            LEFT JOIN comment AS c ON t.id = c.task_id
            LEFT JOIN account AS au ON au.id = t.author_id
            LEFT JOIN account AS an ON an.id = t.assignee_id
            WHERE (:author IS NULL AND au.email IS NOT NULL) OR (:author IS NOT NULL AND :author = au.email)
            GROUP BY t.id, t.title, t.description, t.status, au.email, an.email, t.created_at
            ORDER BY t.created_at DESC
            """, nativeQuery = true)
    List<TaskView> findAllByViewsAuthorEmail(@Param("author") String author);
}

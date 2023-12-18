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
}

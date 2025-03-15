package com.xalts.io.multi.user.approval.repository;

import com.xalts.io.multi.user.approval.model.Comment;
import com.xalts.io.multi.user.approval.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByTaskOrderByCreatedAtDesc(Task task);
}
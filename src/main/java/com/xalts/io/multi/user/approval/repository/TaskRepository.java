package com.xalts.io.multi.user.approval.repository;

import com.xalts.io.multi.user.approval.model.Task;
import com.xalts.io.multi.user.approval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByCreator(User creator);

	@Query("SELECT t FROM Task t JOIN t.approvals a WHERE a.approver.id = :approverId AND a.status = 'PENDING'")
	List<Task> findTasksPendingApprovalByUser(Long approverId);
}
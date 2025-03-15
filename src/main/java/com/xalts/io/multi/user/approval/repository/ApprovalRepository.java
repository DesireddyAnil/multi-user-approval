package com.xalts.io.multi.user.approval.repository;

import com.xalts.io.multi.user.approval.model.Approval;
import com.xalts.io.multi.user.approval.model.Task;
import com.xalts.io.multi.user.approval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
	List<Approval> findByTask(Task task);
	Optional<Approval> findByTaskAndApprover(Task task, User approver);
}
package com.xalts.io.multi.user.approval.service;

import com.xalts.io.multi.user.approval.dto.ApprovalRequest;
import com.xalts.io.multi.user.approval.dto.CommentRequest;
import com.xalts.io.multi.user.approval.dto.TaskRequest;
import com.xalts.io.multi.user.approval.model.Approval;
import com.xalts.io.multi.user.approval.model.Comment;
import com.xalts.io.multi.user.approval.model.Task;
import com.xalts.io.multi.user.approval.model.User;
import com.xalts.io.multi.user.approval.repository.ApprovalRepository;
import com.xalts.io.multi.user.approval.repository.CommentRepository;
import com.xalts.io.multi.user.approval.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final ApprovalRepository approvalRepository;
	private final CommentRepository commentRepository;
	private final UserService userService;
	private final NotificationService notificationService;

	@Autowired
	public TaskService(
		TaskRepository taskRepository,
		ApprovalRepository approvalRepository,
		CommentRepository commentRepository,
		UserService userService,
		NotificationService notificationService) {
		this.taskRepository = taskRepository;
		this.approvalRepository = approvalRepository;
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.notificationService = notificationService;
	}

	@Transactional
	public Task createTask(TaskRequest request, User creator) {
		// Validate approvers
		if (request.getApproverIds().size() != 3) {
			throw new IllegalArgumentException("Exactly 3 approvers are required");
		}

		Task task = new Task();
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setCreator(creator);

		Task savedTask = taskRepository.save(task);

		// Add approvers
		for (Long approverId : request.getApproverIds()) {
			User approver = userService.getUserById(approverId);

			Approval approval = new Approval();
			approval.setTask(savedTask);
			approval.setApprover(approver);
			approvalRepository.save(approval);

			// Notify approver
			notificationService.notifyNewTask(approver, savedTask);
		}

		return savedTask;
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public List<Task> getTasksByCreator(User creator) {
		return taskRepository.findByCreator(creator);
	}

	public List<Task> getTasksPendingApproval(User approver) {
		return taskRepository.findTasksPendingApprovalByUser(approver.getId());
	}

	@Transactional
	public Approval approveTask(Long taskId, User approver, ApprovalRequest request) {
		Task task = getTaskById(taskId);

		Approval approval = approvalRepository.findByTaskAndApprover(task, approver)
			.orElseThrow(() -> new IllegalArgumentException("You are not assigned as an approver for this task"));

		if (!"PENDING".equals(approval.getStatus())) {
			throw new IllegalArgumentException("You have already processed this approval");
		}

		approval.setStatus("APPROVED");
		approval.setComment(request.getComment());
		approvalRepository.save(approval);
		notificationService.notifyApproval(task.getCreator(), task, approver, "approved");
		updateTaskStatus(task);

		return approval;
	}

	@Transactional
	public Approval rejectTask(Long taskId, User approver, ApprovalRequest request) {
		Task task = getTaskById(taskId);

		Approval approval = approvalRepository.findByTaskAndApprover(task, approver)
			.orElseThrow(() -> new IllegalArgumentException("You are not assigned as an approver for this task"));

		if (!"PENDING".equals(approval.getStatus())) {
			throw new IllegalArgumentException("You have already processed this approval");
		}

		approval.setStatus("REJECTED");
		approval.setComment(request.getComment());
		approvalRepository.save(approval);

		// Update task status
		task.setStatus("REJECTED");
		taskRepository.save(task);

		// Notify task creator
		notificationService.notifyApproval(task.getCreator(), task, approver, "rejected");

		return approval;
	}

	@Transactional
	public void updateTaskStatus(Task task) {
		List<Approval> approvals = approvalRepository.findByTask(task);

		long approvedCount = approvals.stream()
			.filter(a -> "APPROVED".equals(a.getStatus()))
			.count();

		if (approvedCount >= 3) {
			task.setStatus("APPROVED");
			task.setUpdatedAt(LocalDateTime.now());
			taskRepository.save(task);

			// Notify all parties
			notificationService.notifyTaskCompleted(task);
		}
	}


	@Transactional
	public Comment addComment(Long taskId, User user, CommentRequest request) {
		Task task = getTaskById(taskId);

		Comment comment = new Comment();
		comment.setTask(task);
		comment.setUser(user);
		comment.setContent(request.getContent());

		return commentRepository.save(comment);
	}

	public List<Comment> getCommentsByTask(Long taskId) {
		Task task = getTaskById(taskId);
		return commentRepository.findByTaskOrderByCreatedAtDesc(task);
	}
}
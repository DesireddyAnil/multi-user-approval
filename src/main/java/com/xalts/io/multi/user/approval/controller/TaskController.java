package com.xalts.io.multi.user.approval.controller;

import com.xalts.io.multi.user.approval.dto.ApprovalRequest;
import com.xalts.io.multi.user.approval.dto.CommentRequest;
import com.xalts.io.multi.user.approval.dto.TaskRequest;
import com.xalts.io.multi.user.approval.dto.TaskResponse;
import com.xalts.io.multi.user.approval.model.Approval;
import com.xalts.io.multi.user.approval.model.Comment;
import com.xalts.io.multi.user.approval.model.Task;
import com.xalts.io.multi.user.approval.model.User;
import com.xalts.io.multi.user.approval.service.TaskService;
import com.xalts.io.multi.user.approval.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private final UserService userService;

	@Autowired
	public TaskController(TaskService taskService, UserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(
		@RequestBody TaskRequest request,
		Authentication authentication) {

		User creator = userService.getUserByEmail(authentication.getName());
		Task task = taskService.createTask(request, creator);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(TaskResponse.fromTask(task));
	}

	@GetMapping
	public ResponseEntity<List<TaskResponse>> getAllTasks() {
		List<Task> tasks = taskService.getAllTasks();
		List<TaskResponse> response = tasks.stream()
			.map(TaskResponse::fromTask)
			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
		Task task = taskService.getTaskById(id);
		return ResponseEntity.ok(TaskResponse.fromTask(task));
	}

	@GetMapping("/my-tasks")
	public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication) {
		User user = userService.getUserByEmail(authentication.getName());
		List<Task> tasks = taskService.getTasksByCreator(user);

		List<TaskResponse> response = tasks.stream()
			.map(TaskResponse::fromTask)
			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/pending-approval")
	public ResponseEntity<List<TaskResponse>> getTasksPendingMyApproval(Authentication authentication) {
		User user = userService.getUserByEmail(authentication.getName());
		List<Task> tasks = taskService.getTasksPendingApproval(user);

		List<TaskResponse> response = tasks.stream()
			.map(TaskResponse::fromTask)
			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/approve")
	public ResponseEntity<TaskResponse> approveTask(
		@PathVariable Long id,
		@RequestBody ApprovalRequest request,
		Authentication authentication) {

		User approver = userService.getUserByEmail(authentication.getName());
		Approval approval = taskService.approveTask(id, approver, request);
		Task task = approval.getTask();

		return ResponseEntity.ok(TaskResponse.fromTask(task));
	}

	@PostMapping("/{id}/reject")
	public ResponseEntity<TaskResponse> rejectTask(
		@PathVariable Long id,
		@RequestBody ApprovalRequest request,
		Authentication authentication) {

		User approver = userService.getUserByEmail(authentication.getName());
		Approval approval = taskService.rejectTask(id, approver, request);
		Task task = approval.getTask();

		return ResponseEntity.ok(TaskResponse.fromTask(task));
	}
}
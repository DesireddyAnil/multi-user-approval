package com.xalts.io.multi.user.approval.controller;

import com.xalts.io.multi.user.approval.dto.CommentRequest;
import com.xalts.io.multi.user.approval.model.Comment;
import com.xalts.io.multi.user.approval.model.User;
import com.xalts.io.multi.user.approval.service.TaskService;
import com.xalts.io.multi.user.approval.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private final TaskService taskService;
	private final UserService userService;

	@Autowired
	public CommentController(TaskService taskService, UserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	@PostMapping("/task/{taskId}")
	public ResponseEntity<Comment> addComment(
		@PathVariable Long taskId,
		@RequestBody CommentRequest request,
		Authentication authentication) {

		User user = userService.getUserByEmail(authentication.getName());
		Comment comment = taskService.addComment(taskId, user, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(comment);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<Comment>> getCommentsByTask(@PathVariable Long taskId) {
		List<Comment> comments = taskService.getCommentsByTask(taskId);
		return ResponseEntity.ok(comments);
	}
}
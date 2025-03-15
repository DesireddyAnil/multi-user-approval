package com.xalts.io.multi.user.approval.service;

import com.xalts.io.multi.user.approval.model.Task;
import com.xalts.io.multi.user.approval.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	public void notifyNewTask(User approver, Task task) {
		logger.info("NOTIFICATION EMAIL: To: {}, Subject: New Task Requires Your Approval", approver.getEmail());
		logger.info("Task: {} (ID: {})", task.getTitle(), task.getId());
		logger.info("Created by: {}", task.getCreator().getName());
		logger.info("Please review and provide your approval or rejection.");
	}

	public void notifyApproval(User recipient, Task task, User approver, String action) {
		logger.info("NOTIFICATION EMAIL: To: {}, Subject: Task {} by {}",
			recipient.getEmail(), action, approver.getName());
		logger.info("Task: {} (ID: {})", task.getTitle(), task.getId());
		logger.info("Action taken by: {}", approver.getName());
		logger.info("Action: {}", action);
	}

	public void notifyTaskCompleted(Task task) {
		logger.info("NOTIFICATION EMAIL: To: {}, Subject: Task Approval Completed",
			task.getCreator().getEmail());
		logger.info("Task: {} (ID: {})", task.getTitle(), task.getId());
		logger.info("Status: {}", task.getStatus());
		logger.info("All required approvals have been received.");
	}
}
package com.xalts.io.multi.user.approval.dto;

import com.xalts.io.multi.user.approval.model.Approval;
import com.xalts.io.multi.user.approval.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
	private Long id;
	private String title;
	private String description;
	private String status;
	private UserDto creator;
	private List<ApprovalDto> approvals;
	private LocalDateTime createdAt;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserDto {
		private Long id;
		private String name;
		private String email;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ApprovalDto {
		private Long id;
		private UserDto approver;
		private String status;
		private String comment;
	}

	public static TaskResponse fromTask(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setStatus(task.getStatus());
		response.setCreatedAt(task.getCreatedAt());

		UserDto creatorDto = new UserDto(
			task.getCreator().getId(),
			task.getCreator().getName(),
			task.getCreator().getEmail()
		);
		response.setCreator(creatorDto);

		List<ApprovalDto> approvalDtos = task.getApprovals().stream()
			.map(approval -> {
				UserDto approverDto = new UserDto(
					approval.getApprover().getId(),
					approval.getApprover().getName(),
					approval.getApprover().getEmail()
				);
				return new ApprovalDto(
					approval.getId(),
					approverDto,
					approval.getStatus(),
					approval.getComment()
				);
			})
			.collect(Collectors.toList());
		response.setApprovals(approvalDtos);

		return response;
	}
}
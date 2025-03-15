package com.xalts.io.multi.user.approval.dto;

import lombok.Data;
import java.util.List;

@Data
public class TaskRequest {
	private String title;
	private String description;
	private List<Long> approverIds;
}
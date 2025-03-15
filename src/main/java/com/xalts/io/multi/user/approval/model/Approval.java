package com.xalts.io.multi.user.approval.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "approvals")
@Data
@NoArgsConstructor
public class Approval {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonBackReference
	private Task task;

	@ManyToOne
	private User approver;

	private String status = "PENDING";

	private String comment;

	private LocalDateTime createdAt = LocalDateTime.now();
}
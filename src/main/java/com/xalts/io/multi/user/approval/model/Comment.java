package com.xalts.io.multi.user.approval.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Task task;

	@ManyToOne
	private User user;

	private String content;

	private LocalDateTime createdAt = LocalDateTime.now();
}
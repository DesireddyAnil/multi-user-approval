package com.xalts.io.multi.user.approval.dto;

import lombok.Data;

@Data
public class AuthRequest {
	private String name;
	private String email;
	private String password;
}
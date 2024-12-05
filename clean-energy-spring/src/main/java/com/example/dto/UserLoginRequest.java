package com.example.dto;

public class UserLoginRequest {

	private String userEmail;
	private String userPassword;
	private String userName;
	
	
	
	public UserLoginRequest() {
		super();
	}

	public UserLoginRequest(String userEmail, String userPassword) {
		super();
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}
	
	public UserLoginRequest(String userEmail,String userPassword,String userName) {
		super();
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userName =userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserLoginRequest [userEmail=" + userEmail + ", userPassword=" + userPassword + ", userName=" + userName
				+ "]";
	}

    
	
	
}

package com.example.demo.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class MyUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username")
	private String userName;
	private String password;
	private String name;
	@Column(name = "rolename")
	private String roleName;
}

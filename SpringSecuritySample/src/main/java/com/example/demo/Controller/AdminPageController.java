package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

	@GetMapping("/adminPage")
//	@PreAuthorize("hasRole('ROLE_ADMIN')") // (1) ROLE_ADMINのユーザのみアクセスを許可
	public String adminPage() {
		return "adminPage";
	}
}

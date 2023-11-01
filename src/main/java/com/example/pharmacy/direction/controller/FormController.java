package com.example.pharmacy.direction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FormController {

	@GetMapping("/")
	public String main() {
		return "main";
	}
}

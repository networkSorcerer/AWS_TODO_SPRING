package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;

@RestController
@RequestMapping("test")//리소스
public class TestController {
	
	@GetMapping
	public String testController() {
		return "hello World!";
	}
	
	@GetMapping("/testGetMapping")
	public String testControllerWithPath() {
		return "Hello World! testGetMapping";
	}
	
	 @GetMapping("/{id}")
	    public String testControllerWithPathVariables(@PathVariable("id") Integer id) {//이름을 명시적으로 밝히도록 스프링부트 3.2부터 변경
	        return "Hello World! ID " + (id != null ? id : "not provided");
    }

    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(name = "id", defaultValue = "0") int id) {
        return "Hello World! ID " + id;
    }
    
    @GetMapping("/testRequetBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
    	return "Hello World! ID" + testRequestBodyDTO.getId() + "Message : " + testRequestBodyDTO.getMessage();
     }
    
    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody() {
    	List<String> list = new ArrayList<>();
    	list.add("Hello World! I'm ResponseDTO");
    	ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    	return response;
    }
    
    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity() {
    	List<String> list =new ArrayList<>();
    	list.add("Hello World? I'm ResponseEntity. And you got 400!");
    	ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    	return ResponseEntity.badRequest().body(response);
    }
}

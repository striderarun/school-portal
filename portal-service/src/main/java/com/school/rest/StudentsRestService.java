package com.school.rest;

import com.school.beans.StudentBean;
import com.school.logging.Loggable;
import com.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/services/student")
public class StudentsRestService {

	@Autowired
	private StudentService studentService;
	
	@Loggable
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<StudentBean> getStudents() {
		return studentService.getStudents();
	}
	
}

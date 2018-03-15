package com.school.service.impl;

import com.school.beans.StudentBean;
import com.school.domain.Student;
import com.school.logging.Loggable;
import com.school.repository.StudentsRepository;
import com.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class StudentsServiceImpl implements StudentService {

    @Resource
    private StudentsRepository studentsRepository;

    @Autowired
    private BeanMapper mapper;

    @Loggable
    @Override
    public List<StudentBean> getStudents() {
        List<Student> students = studentsRepository.findAll();
        return mapper.mapIterable(students, StudentBean.class);
    }



}

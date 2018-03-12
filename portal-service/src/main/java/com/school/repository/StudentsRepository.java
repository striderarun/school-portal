package com.school.repository;

import com.school.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentsRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {


}

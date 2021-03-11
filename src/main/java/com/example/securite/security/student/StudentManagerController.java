package com.example.securite.security.student;

import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpCookie;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("manager/api/v1/students")
public class StudentManagerController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith"));

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINISTRATION')")
    public List<Student> getAllStudent(HttpServletRequest servletRequest){

        Cookie[] cookies = servletRequest.getCookies();
        if(cookies != null){

            for (Cookie c:
                    cookies) {
                System.out.println(c.getName()+ " = " + c.getValue());
            }
        }
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('course:write')")
    public void registerNewStudent(@RequestBody Student student){



        System.out.println("POST METHOD");
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId")
    @PreAuthorize("hasAuthority('course:write')")
    public void deleteStudent(@PathVariable("studentId") Integer theId){
        System.out.println(theId);
    }

    @PutMapping( path = "{studentId")
    @PreAuthorize("hasAuthority('course:write')")
    public void updateStudent(@PathVariable("studentId")Integer theId,
                              @RequestBody Student student){
        System.out.println(student + " was update with id:" + theId);
    }
}

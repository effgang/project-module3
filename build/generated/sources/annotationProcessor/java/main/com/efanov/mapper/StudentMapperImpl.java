package com.efanov.mapper;

import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;
import com.efanov.model.Student;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-13T18:52:23+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student mapToModel(StudentRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Student student = new Student();

        student.setName( dto.getName() );
        student.setSurname( dto.getSurname() );
        student.setPhoneNumber( dto.getPhoneNumber() );
        student.setBirthday( dto.getBirthday() );

        return student;
    }

    @Override
    public StudentResponse mapToResponse(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentResponse.StudentResponseBuilder studentResponse = StudentResponse.builder();

        studentResponse.id( student.getId() );
        studentResponse.name( student.getName() );
        studentResponse.surname( student.getSurname() );
        studentResponse.birthday( student.getBirthday() );

        return studentResponse.build();
    }
}

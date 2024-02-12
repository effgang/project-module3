package com.efanov.mapper;

import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;
import com.efanov.model.Student;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T18:01:37+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.1 (Oracle Corporation)"
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
        student.setGroupNumber( dto.getGroupNumber() );

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
        studentResponse.groupNumber( student.getGroupNumber() );

        return studentResponse.build();
    }
}

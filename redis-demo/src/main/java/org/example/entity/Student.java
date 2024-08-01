package org.example.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Huang Z.Y.
 */
@Data
@ToString
public class Student implements Serializable, Comparable<Student> {
    private Long id;

    private String name;

    private Integer age;

    @Override
    public int compareTo(Student o) {
        return this.age.compareTo(o.age);
    }
}

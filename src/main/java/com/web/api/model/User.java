package com.web.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Setter
@Getter
//@EqualsAndHashCode
//@RequiredArgsConstructor
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstname;

    private String lastname;

    @Column
    private String otherNames;

    @Column(nullable = false, length = 5, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean deleted;

}

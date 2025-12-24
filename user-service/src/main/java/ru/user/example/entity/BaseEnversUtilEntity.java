package ru.user.example.entity;

import jakarta.persistence.*;


@Entity
//@RevisionEntity
@Table(name = "revinfo", schema = "person_history")
public class BaseEnversUtilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @RevisionNumber
    @Column(name = "rev")
    private long rev;

//    @RevisionTimestamp
    @Column(name = "revtmstmp")
    private long revtmstmp;

}
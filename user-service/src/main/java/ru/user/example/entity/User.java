package ru.user.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;
import java.util.UUID;

//@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "person")
public class User extends BaseEntity {
    @NotNull
    @Size(max = 64)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(max = 128)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Size(max = 64)
    @Column(name = "email", nullable = false, length = 64, unique = true)
    private String email;

    @NotNull
    @Size(max = 64)
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @NotNull
    @Size(max = 64)
    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @NotNull
    @Column(name = "tasks", nullable = false)
    private Set<UUID> tasks;
}
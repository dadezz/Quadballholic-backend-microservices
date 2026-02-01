package com.quadballholic.backend.userService.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import com.quadballholic.backend.userService.enums.EnumUserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = now() WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityUser extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable=false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable=false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private EnumUserStatus status = EnumUserStatus.WAITING_EMAIL_CONFIRMATION;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<EntityRole> role = new HashSet<>();

    @PreRemove
    public void onDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.status = EnumUserStatus.DELETED;
    }

    public EntityUser(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public void addUserRole(EntityRole role){
        this.role.add(role);
    }

}
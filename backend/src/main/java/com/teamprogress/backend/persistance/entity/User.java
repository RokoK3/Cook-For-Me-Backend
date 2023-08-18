package com.teamprogress.backend.persistance.entity;

import com.teamprogress.backend.persistance.entity.converter.AccountStatusConverter;
import com.teamprogress.backend.persistance.entity.converter.LocationConverter;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.persistance.entity.model.Location;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 1024)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String lastname;

    @Column(nullable = false)
    @Convert(converter = LocationConverter.class)
    private Location location;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(nullable = false)
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status;

    @Column(length = 38, nullable = false)
    private String verificationCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<UserOrder> orders;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User)o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

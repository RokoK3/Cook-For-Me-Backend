package com.teamprogress.backend.persistance.entity;

import com.teamprogress.backend.persistance.entity.converter.AccountStatusConverter;
import com.teamprogress.backend.persistance.entity.converter.CuisineConverter;
import com.teamprogress.backend.persistance.entity.converter.LocationConverter;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.persistance.entity.model.Cuisine;
import com.teamprogress.backend.persistance.entity.model.Location;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cook {

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

    @Column(nullable = false)
    private BigDecimal rating;

    @Column(nullable = false)
    @Convert(converter = CuisineConverter.class)
    private Cuisine cuisine;

    @Column(nullable = false)
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status;

    @Column(length = 38, nullable = false)
    private String verificationCode;

    @OneToMany(mappedBy = "cook", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Dish> dishes = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cook cook = (Cook)o;
        return id != null && Objects.equals(id, cook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


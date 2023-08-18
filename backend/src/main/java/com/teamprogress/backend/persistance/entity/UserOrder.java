package com.teamprogress.backend.persistance.entity;

import com.teamprogress.backend.persistance.entity.converter.OrderStatusConverter;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_order")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    @ToString.Exclude
    Set<OrderRating> orderRating;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserOrder userOrder = (UserOrder)o;
        return id != null && Objects.equals(id, userOrder.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

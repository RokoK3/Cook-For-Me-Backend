package com.teamprogress.backend.persistance.entity;

import com.teamprogress.backend.persistance.entity.converter.DayOfWeekConverter;
import com.teamprogress.backend.persistance.entity.converter.IngredientConverter;
import com.teamprogress.backend.persistance.entity.model.Ingredient;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cook_id", referencedColumnName = "id")
    private Cook cook;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 200)
    private String image;

    @Column
    private Integer minOrders;

    @Column(nullable = false)
    private Integer maxOrders;

    @Column(nullable = false)
    private Integer preparationTime;

    @Convert(converter = IngredientConverter.class)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Ingredient.class)
    @CollectionTable(name = "ingredient_list", joinColumns = @JoinColumn(name = "dish_id", referencedColumnName = "id"))
    @Column(name = "ingredient")
    private Set<Ingredient> ingredients = new HashSet<>();

    @Convert(converter = DayOfWeekConverter.class, attributeName = "key")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dish_day", joinColumns = @JoinColumn(name = "dish_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "dayOfWeek")
    private Map<DayOfWeek, DishDay> dishInfoPerDay = new HashMap<>();

    @OneToMany(mappedBy = "dish", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<OrderRating> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dish dish = (Dish)o;
        return id != null && Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

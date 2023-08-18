package com.teamprogress.backend.persistance.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.sql.Time;

@Data
@Embeddable
public class DishDay {

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

}

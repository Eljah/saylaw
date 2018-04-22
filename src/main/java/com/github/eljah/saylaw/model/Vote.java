package com.github.eljah.saylaw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
public class Vote {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}

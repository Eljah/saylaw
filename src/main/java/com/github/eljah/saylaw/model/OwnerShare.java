package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OwnerShare {
    @Id
    @GeneratedValue
    private Long id;

    private int shareNominator;
    private int shareDenominator;
    private double shareValue;

    @OneToOne
    private Owner owner;

    @ManyToOne
    private Share share;

    private Boolean active;

    @Embedded
    private ExtractOfRegistry extractOfRegistry;

}

package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.solr.core.mapping.Indexed;

import javax.persistence.*;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Share {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Indexed
    private String name;

    private int shareNominator;
    private int shareDenominator;
    private float shareValue;

    @Indexed
    private Float area;
    private ShareType type;

    @ManyToOne
    private Owner owner;

    @Embedded
    private ExtractOfRegistry extractOfRegistry;


    public enum ShareType
    {
        RESIDENTAL,
        NONRESIDENTAL
    }

}


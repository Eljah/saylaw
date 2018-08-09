package com.github.eljah.saylaw.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.solr.core.mapping.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
@ToString(exclude = {"ownerShare", "shareVotes"})
@Slf4j
public class Share {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Indexed
    @Access(AccessType.PROPERTY)
    private String name;

    @Column(unique = true)
    public void setName(String name) {
        //log.info("Setter Name is called");
        this.name = name;
        try {
            this.setNumber(number = name != null ? Integer.parseInt(name.replaceAll("[^0-9]", "")) : null);
        }
        catch (java.lang.NumberFormatException e)
        {
            this.setNumber(0);
        }
    }



    @Access(AccessType.PROPERTY)
    private Integer number;

    public void setNumber(Integer number) {
        if (number == null) {
        } else {
            log.trace(number + " number passed to store");
            this.number = number;
        }
    }

    private long shareNominator;
    private long shareDenominator;
    private double shareValue;

    @Indexed
    private Double area;
    private ShareType type;

    private int floor;

    @OneToMany(mappedBy = "share")
    private List<OwnerShare> ownerShare = new ArrayList<>();

    public enum ShareType {
        RESIDENTAL,
        NONRESIDENTAL
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "share")
    private List<ShareVote> shareVotes;

    private Boolean active;

}


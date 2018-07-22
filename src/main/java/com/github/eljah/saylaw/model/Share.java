package com.github.eljah.saylaw.model;

import lombok.*;
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
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Share {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Indexed
    @Column(unique = true)
    private String name;

    private int shareNominator;
    private int shareDenominator;
    private double shareValue;

    @Indexed
    private Double area;
    private ShareType type;

    private int floor;

    @OneToMany(mappedBy = "share")
    private List<OwnerShare> ownerShare=new ArrayList<>();

    public enum ShareType
    {
        RESIDENTAL,
        NONRESIDENTAL
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "share")
    private List<ShareVote> shareVotes;


}


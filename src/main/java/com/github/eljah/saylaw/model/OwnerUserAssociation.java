package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OwnerUserAssociation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Owner owner;
    @ManyToOne
    private User user;
    private Boolean active;
    private Date establishedDate;
    @Version
    private Long version;
}

package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<OwnerUserAssociation> ownerUserAssociations;

    private String name;
    private String hashedPassword;
    private String socialNetworkProvider;
}

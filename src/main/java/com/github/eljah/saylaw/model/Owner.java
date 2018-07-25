package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue
    private Long id;

    //@NotEmpty
    //@NotNull
    private String firstName;
    private String secondName;
    @NotEmpty
    @NotNull
    private String lastName;

    @OneToMany(mappedBy = "owner")
    private List<OwnerUserAssociation> ownerUserAssociations;

    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER)
    private OwnerShare ownerShare;

}

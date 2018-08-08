package com.github.eljah.saylaw.model;

import lombok.*;
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
@ToString//(exclude = "ownerShare")
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

    @Version
    private Long version;

}

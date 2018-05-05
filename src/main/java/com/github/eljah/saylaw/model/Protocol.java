package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eljah32 on 5/5/2018.
 */

@Setter
@Getter
@NoArgsConstructor
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ParentClass")
@DiscriminatorColumn(name = "PROTOCOL_TYPE")
@Table(name="protocols")
@Entity
public abstract class Protocol {
    @Id
    @TableGenerator(name = "PROTOCOL_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PROTOCOL_GEN")
    private Long id;

    @Lob
    private byte[] file;
}

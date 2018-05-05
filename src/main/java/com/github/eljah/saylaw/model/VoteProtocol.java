package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by eljah32 on 4/22/2018.
 */
@Entity
@DiscriminatorValue(value = "vote")
public class VoteProtocol extends Protocol{
    //@Id
    //Long id;

}

package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@DiscriminatorValue(value = "share_vote")
public class ShareVoteProtocol extends Protocol{
    //@Id
    //Long id;
}

package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by eljah32 on 4/23/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class OwnerShareVoteQuestion {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private VoteQuestion voteQuestion;
    @ManyToOne
    private OwnerShare ownerShare;
    private boolean agree;

    @ManyToOne
    private OwnerShareVote ownerShareVote;
}

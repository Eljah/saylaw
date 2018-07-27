package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Created by eljah32 on 7/24/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
//@ToString
public class OwnerShareVote {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "ownerShareVote")
    private List<OwnerShareVoteQuestion> ownerShareVoteQuestion;
    @ManyToOne
    private OwnerShare ownerShare;
    @ManyToOne
    private ShareVote shareVote;

    private int shareNominator;
    private int shareDenominator;
    private double shareValue;
    private long shareNominatorCommon;
    private long shareDenominatorCommon;
    private double shareValueCommon;


}

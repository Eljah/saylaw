package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eljah32 on 7/24/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class VoteQuestionResult {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private VoteResult voteResult;

    boolean pro;

    long denominator;
    long nomintorPro;
    long nomintorContra;
    double valuePro;
    double valueContra;

    @OneToOne
    VoteQuestionProtocol voteQuestionProtocol;
}

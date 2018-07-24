package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.acl.*;
import java.util.Date;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "vote")
    List<ShareVote> shareVotes;

    @OneToMany(mappedBy = "vote")
    List<VoteQuestion> voteQuestions;

    @Temporal(TemporalType.DATE)
    Date voteInitiated;

    @Temporal(TemporalType.DATE)
    Date voteOnsite;

    @Temporal(TemporalType.DATE)
    Date voteValidThrough;

    @OneToOne
    Owner initiator;

    VoteStatus status;

    public enum VoteStatus
    {
        PREPARED,
        INITATED,
        FINALIZED,
        NOTICE_SERVED,
        ON_SITE_RESULTS_OBTAINED,
        MAILBOX_RESULTS_OBTAINED
    }
}

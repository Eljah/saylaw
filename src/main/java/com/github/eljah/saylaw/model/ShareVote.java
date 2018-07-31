package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ShareVote {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Share share;
    @ManyToOne
    private Vote vote;

    @Embedded
    private ExtractOfRegistry extractOfRegistry;

    @Temporal(TemporalType.DATE)
    Date voteAquired;

    @OneToOne
    @Lazy
    private ShareVoteProtocol protocol;

    private boolean noticeServed;

    private boolean onSiteVote;

    @OneToMany(mappedBy = "shareVote")
    private List<OwnerShareVote> ownerShareVotes;

    private long shareNominator;
    private long shareDenominator;
    private double shareValue;

}

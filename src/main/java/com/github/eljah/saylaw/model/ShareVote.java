package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by eljah32 on 4/22/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ShareVote {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Share share;
    @ManyToOne
    private Vote vote;

    @Embedded
    private ExtractOfRegistry extractOfRegistry;

    @Temporal(TemporalType.DATE)
    Date voteAquired;

    @OneToOne
    private ShareVoteProtocol protocol;

    private boolean noticeServed;

    private boolean onSiteVote;

}

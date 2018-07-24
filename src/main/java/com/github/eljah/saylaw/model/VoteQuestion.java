package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by eljah32 on 4/23/2018.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class VoteQuestion {
    @Id
    @GeneratedValue
    private Long id;
    private String text;

    @ManyToOne
    Vote vote;

    @OneToMany(mappedBy = "voteQuestion")
    List<OwnerShareVoteQuestion> ownerShareVoteQuestions;
}

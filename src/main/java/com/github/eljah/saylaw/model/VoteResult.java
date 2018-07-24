package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eljah32 on 4/22/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class VoteResult {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Vote vote;

    @OneToMany(mappedBy = "voteResult")
    List<VoteQuestionResult> voteQuestionResults;

}

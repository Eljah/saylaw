package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by eljah32 on 4/23/2018.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class VoteQuestionProtocol {
    @Id
    @GeneratedValue
    private Long id;

}

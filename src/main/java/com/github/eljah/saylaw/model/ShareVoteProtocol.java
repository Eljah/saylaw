package com.github.eljah.saylaw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by eljah32 on 4/22/2018.
 */

@Setter
@Getter
@NoArgsConstructor
public class ShareVoteProtocol {
    @Id
    @GeneratedValue
    private Long id;

}

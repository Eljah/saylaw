package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.VoteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by eljah32 on 7/24/2018.
 */
public interface VoteQuestionRepository extends JpaRepository<VoteQuestion, Long> {
}

package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by eljah32 on 7/24/2018.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByActiveIsTrue();
    List<Vote> findByActiveIsFalse();
}

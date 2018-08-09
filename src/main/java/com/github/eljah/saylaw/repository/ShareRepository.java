package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findByActiveIsTrue();
    List<Share> findByActiveIsFalse();
    List<Share> findAllByOrderByNumberAscNameAsc();
    List<Share> findAllByOrderByNumberAsc();
    List<Share> findAllByOrderByNameAsc();
}

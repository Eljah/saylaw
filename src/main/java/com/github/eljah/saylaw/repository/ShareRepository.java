package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
}

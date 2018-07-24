package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.OwnerShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by eljah32 on 4/25/2018.
 */
public interface OwnerShareRepository extends JpaRepository<OwnerShare, Long> {
    List<OwnerShare> findByActiveIsTrue();
    List<OwnerShare> findByActiveIsFalse();
}

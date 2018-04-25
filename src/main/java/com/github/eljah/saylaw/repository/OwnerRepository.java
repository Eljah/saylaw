package com.github.eljah.saylaw.repository;

import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by eljah32 on 4/25/2018.
 */
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}

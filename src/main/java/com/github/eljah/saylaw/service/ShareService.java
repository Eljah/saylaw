package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;

import java.util.List;
import java.util.Set;

/**
 * Created by eljah32 on 4/25/2018.
 */
public interface ShareService {
    void save(Share share);
    void saveWithInternals(Share share);
    void saveInactiveWithInternals(Share share);
    void saveActiveWithInternals(Share share);
    void createShares(Set<Share> shares);
    void createOwnerShares(List<OwnerShare> ownerShares);
    void createOwnerShares(List<OwnerShare> ownerShares, Share share);
    void calculateShareValues();
    void calculateOwnerShareValues();
    List<Share> showShares();
    Share get(Long id);
}

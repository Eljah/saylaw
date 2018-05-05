package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;

import java.util.List;
import java.util.Set;

/**
 * Created by eljah32 on 4/25/2018.
 */
public interface ShareService {
    void createShares(Set<Share> shares);
    void createOwnerShares(List<OwnerShare> ownerShares);
}
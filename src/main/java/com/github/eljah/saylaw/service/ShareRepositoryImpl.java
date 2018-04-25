package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;
import com.github.eljah.saylaw.repository.OwnerRepository;
import com.github.eljah.saylaw.repository.OwnerShareRepository;
import com.github.eljah.saylaw.repository.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Service
public class ShareRepositoryImpl implements ShareService {
    @Autowired
    ShareRepository shareRepository;

    @Autowired
    OwnerRepository ownerRepository;


    @Autowired
    OwnerShareRepository ownerShareRepository;

    @Override
    public void createShares(Set<Share> shares) {
        for (Share share: shares)
        {
            shareRepository.save(share);
        }
    }

    @Override
    public void createOwnerShares(List<OwnerShare> ownerShares) {
        for (OwnerShare ownerShare: ownerShares)
        {
            ownerShareRepository.save(ownerShare);
            ownerRepository.save(ownerShare.getOwner());

        }
    }
}

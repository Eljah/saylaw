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
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareRepository shareRepository;

    @Autowired
    OwnerRepository ownerRepository;


    @Autowired
    OwnerShareRepository ownerShareRepository;

    @Override
    public void createShares(Set<Share> shares) {
        List<Share> preexisting = shareRepository.findAll();
        for (Share share : shares) {
            if (!preexisting.contains(share)) {
                shareRepository.save(share);
            } else {
                for (Share preexistinForUpdate: preexisting)
                {
                    if (preexistinForUpdate.equals(share))
                    {
                        preexistinForUpdate=share.toBuilder().id(preexistinForUpdate.getId()).version(preexistinForUpdate.getVersion()).build();
                        //shareRepository.save(preexistinForUpdate);
                        List <OwnerShare> ownerShareList=share.getOwnerShare();
                                for (OwnerShare ownerShare: ownerShareList)
                                {
                                    ownerShare.setShare(preexistinForUpdate);
                                }
                    }
                }
                shareRepository.flush();
            }
        }
    }

    @Override
    public void createOwnerShares(List<OwnerShare> ownerShares) {
        for (OwnerShare ownerShare : ownerShares) {
            ownerShareRepository.save(ownerShare);
            ownerRepository.save(ownerShare.getOwner());

        }
    }
}

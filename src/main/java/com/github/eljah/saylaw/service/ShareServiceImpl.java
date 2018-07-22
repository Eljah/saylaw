package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.DenominatorFind;
import com.github.eljah.saylaw.model.FloatNominatorDenominator;
import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;
import com.github.eljah.saylaw.repository.OwnerRepository;
import com.github.eljah.saylaw.repository.OwnerShareRepository;
import com.github.eljah.saylaw.repository.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
                        //preexistinForUpdate=share.toBuilder().id(preexistinForUpdate.getId()).build();
                        List <OwnerShare> ownerShareList=share.getOwnerShare();
                                for (OwnerShare ownerShare: ownerShareList)
                                {
                                    ownerShare.setShare(preexistinForUpdate);
                                }
                        shareRepository.save(preexistinForUpdate);
                    }
                }

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

    @Override
    public void calculateShareValues() {
        List<Share> allShares=shareRepository.findAll();
        DenominatorFind denominatorFind=new DenominatorFind();
        for (Share currentShare: allShares)
        {
            FloatNominatorDenominator currentFloatNominatorDenominator= new FloatNominatorDenominator();
            currentFloatNominatorDenominator.setDoubleValue(currentShare.getArea());
            currentFloatNominatorDenominator.setDtoForBinding(currentShare);
            denominatorFind.allShares.add(currentFloatNominatorDenominator);
        }
        denominatorFind.processByFractional();
        for (FloatNominatorDenominator currentFloatNominatorDenominator: denominatorFind.allShares)
        {
            ((Share)currentFloatNominatorDenominator.getDtoForBinding()).setShareValue(currentFloatNominatorDenominator.getDoubleFractional());
            ((Share)currentFloatNominatorDenominator.getDtoForBinding()).setShareNominator(currentFloatNominatorDenominator.getNominatorValue());
            ((Share)currentFloatNominatorDenominator.getDtoForBinding()).setShareDenominator(currentFloatNominatorDenominator.getDenominatorValue());
        }
        shareRepository.saveAll(allShares);


    }

    @Override
    public void calculateOwnerShareValues() {
        List<OwnerShare> allShares=ownerShareRepository.findAll();
        DenominatorFind denominatorFind=new DenominatorFind();
        HashSet<Integer> denominators=new HashSet<>();

        for (OwnerShare ownerShare: allShares)
        {
            denominators.add(ownerShare.getShareDenominator());
            ownerShare.setShareValueCommon(ownerShare.getShareNominator()*ownerShare.getShare().getShareValue()/ownerShare.getShareDenominator());
            ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominator()*ownerShare.getShare().getShareDenominator());
            ownerShare.setShareNominatorCommon(ownerShare.getShareNominator()*ownerShare.getShare().getShareNominator());
            System.out.println("Denominator:"+ownerShare.getShareDenominator());
        }

//        Set<Integer> rareDenomenators=new HashSet<>();

//        for (Integer denominator: denominators)
//        {
//            for (Integer denominator2: denominators)
//            {
//                if (denominator>denominator2&&(denominator%denominator2)!=0)
//                {
//                    rareDenomenators.add(denominator);
//                }
//            }
//        }

        for (Integer denominator: denominators)
        {
            System.out.println("Denominator:"+denominator);
            System.out.println();
            System.out.println();
            for (OwnerShare ownerShare: allShares)
            {
                System.out.println("OwnerShare:"+ownerShare);
                 if (denominator.equals(ownerShare.getShareDenominator())) {
                     System.out.println("Denominator equals");
                 }
                else
                 {
                     System.out.println("Denominator not equals");
                     ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominatorCommon()*denominator);
                     ownerShare.setShareNominatorCommon(ownerShare.getShareNominatorCommon()*denominator);
                 }
                System.out.println("OwnerShare:"+ownerShare);
            }
        }
        ownerShareRepository.saveAll(allShares);


    }
}

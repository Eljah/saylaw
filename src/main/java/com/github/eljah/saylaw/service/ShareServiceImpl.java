package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.*;
import com.github.eljah.saylaw.repository.OwnerRepository;
import com.github.eljah.saylaw.repository.OwnerShareRepository;
import com.github.eljah.saylaw.repository.ShareRepository;
import com.github.eljah.saylaw.template.AbstractDocxView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Service
@Slf4j
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareRepository shareRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OwnerShareRepository ownerShareRepository;

    @Override
    public List<Share> showShares() {
        return shareRepository.findAllByOrderByNumberAscNameAsc();
        //return shareRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Share get(Long id) {
        return shareRepository.getOne(id);
    }

    @Override
    public void save(Share share) {
        shareRepository.save(share);
    }

    @Override
    @Transactional
    public void saveWithInternals(Share share) {
        log.info(share.toString());
        share.setActive(true);
        List<OwnerShare> ownerShareList = share.getOwnerShare();
        log.info(share.toString());
        shareRepository.save(share);
        for (OwnerShare ownerShare : ownerShareList) {
            ownerShare.setActive(true);
            ownerShare.setShare(share);
            Owner owner = ownerShare.getOwner();
            log.info(owner.toString());
            log.info(ownerShare.toString());
            owner.setOwnerShare(null);
            owner=ownerRepository.save(owner);
            ownerShare.setOwner(owner);
            ownerShareRepository.save(ownerShare);
            //log.info(owner.toString());
            //log.info(ownerShare.toString());
        }
        log.info("Share repository saved:");
        log.info("Now recalculating share values");
        calculateShareValues();
        log.info("Now recalculating owner share values");
        calculateOwnerShareValues();
    }

    @Override
    public void saveInactiveWithInternals(Share share) {
        log.info(share.toString());
        share.setActive(false);
        List<OwnerShare> ownerShareList = share.getOwnerShare();
        log.info(share.toString());
        shareRepository.save(share);
        for (OwnerShare ownerShare : ownerShareList) {
            ownerShare.setActive(false);
            ownerShare.setShare(share);
            Owner owner = ownerShare.getOwner();
            log.info(owner.toString());
            log.info(ownerShare.toString());
            owner.setOwnerShare(null);
            owner=ownerRepository.save(owner);
            ownerShare.setOwner(owner);
            ownerShareRepository.save(ownerShare);
            //log.info(owner.toString());
            //log.info(ownerShare.toString());
        }
        log.info("Share repository saved:");
        log.info("Now recalculating share values");
        calculateShareValues();
        log.info("Now recalculating owner share values");
        calculateOwnerShareValues();
    }

    @Override
    public void saveActiveWithInternals(Share share) {
        log.info(share.toString());
        share.setActive(true);
        List<OwnerShare> ownerShareList = share.getOwnerShare();
        log.info(share.toString());
        shareRepository.save(share);
        for (OwnerShare ownerShare : ownerShareList) {
            ownerShare.setActive(true);
            ownerShare.setShare(share);
            Owner owner = ownerShare.getOwner();
            log.info(owner.toString());
            log.info(ownerShare.toString());
            owner.setOwnerShare(null);
            owner=ownerRepository.save(owner);
            ownerShare.setOwner(owner);
            ownerShareRepository.save(ownerShare);
        }
        log.info("Share repository saved:");
        log.info("Now recalculating share values");
        calculateShareValues();
        log.info("Now recalculating owner share values");
        calculateOwnerShareValues();
    }

    @Override
    public void createShares(Set<Share> shares) {
        List<Share> preexisting = shareRepository.findAll();
        for (Share share : shares) {
            if (!preexisting.contains(share)) {
                shareRepository.save(share);
            } else {
                for (Share preexistinForUpdate : preexisting) {
                    if (preexistinForUpdate.equals(share)) {
                        preexistinForUpdate = share.toBuilder().id(preexistinForUpdate.getId()).version(preexistinForUpdate.getVersion()).build();
                        //preexistinForUpdate=share.toBuilder().id(preexistinForUpdate.getId()).build();
                        List<OwnerShare> ownerShareList = share.getOwnerShare();
                        for (OwnerShare ownerShare : ownerShareList) {
                            ownerShare.setShare(preexistinForUpdate);
                        }
                        shareRepository.save(preexistinForUpdate);
                    }
                }

            }
        }
    }

    @Override
    @Transactional
    public void createOwnerShares(List<OwnerShare> ownerShares) {
        for (OwnerShare ownerShare : ownerShares) {
            ownerShareRepository.save(ownerShare);
            ownerRepository.save(ownerShare.getOwner());
        }
    }

    @Override
    public void createOwnerShares(List<OwnerShare> ownerShares, Share share) {
        for (OwnerShare ownerShare : ownerShares) {
            ownerShare.setActive(true);
            ownerShare.setShare(share);
            //ownerShare.getOwner().setOwnerShare(ownerShare);
            ownerRepository.save(ownerShare.getOwner());
            System.out.println(ownerShare.toString());
            ownerShareRepository.save(ownerShare);
        }
    }


    @Override
    public void calculateShareValues() {
        List<Share> allShares = shareRepository.findByActiveIsTrue();
        DenominatorFind denominatorFind = new DenominatorFind();
        for (Share currentShare : allShares) {
            FloatNominatorDenominator currentFloatNominatorDenominator = new FloatNominatorDenominator();
            currentFloatNominatorDenominator.setDoubleValue(currentShare.getArea());
            currentFloatNominatorDenominator.setDtoForBinding(currentShare);
            denominatorFind.allShares.add(currentFloatNominatorDenominator);
        }
        denominatorFind.processByFractional();
        for (FloatNominatorDenominator currentFloatNominatorDenominator : denominatorFind.allShares) {
            ((Share) currentFloatNominatorDenominator.getDtoForBinding()).setShareValue(currentFloatNominatorDenominator.getDoubleFractional());
            ((Share) currentFloatNominatorDenominator.getDtoForBinding()).setShareNominator(currentFloatNominatorDenominator.getNominatorValue());
            ((Share) currentFloatNominatorDenominator.getDtoForBinding()).setShareDenominator(currentFloatNominatorDenominator.getDenominatorValue());
        }
        shareRepository.saveAll(allShares);

        List<Share> allSharesInactive = shareRepository.findByActiveIsFalse();
        for (Share share : allSharesInactive) {
            share.setShareValue(0);
            share.setShareDenominator(0);
            share.setShareNominator(0);
        }
        shareRepository.saveAll(allSharesInactive);
    }

    @Override
    public void calculateOwnerShareValues() {
        List<OwnerShare> allShares = ownerShareRepository.findByActiveIsTrue();
        DenominatorFind denominatorFind = new DenominatorFind();
        HashSet<Integer> denominators = new HashSet<>();

        for (OwnerShare ownerShare : allShares) {
            ownerShare.setShareValue((double) ownerShare.getShareNominator() / (double) ownerShare.getShareDenominator());
            denominators.add(ownerShare.getShareDenominator());
            ownerShare.setShareValueCommon(ownerShare.getShareNominator() * ownerShare.getShare().getShareValue() / ownerShare.getShareDenominator());
            ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominator() * ownerShare.getShare().getShareDenominator());
            ownerShare.setShareNominatorCommon(ownerShare.getShareNominator() * ownerShare.getShare().getShareNominator());
            System.out.println("Denominator:" + ownerShare.getShareDenominator());
        }

        HashSet<Integer> denominators2 = denominatorFind.processDenominators(denominators);

        //умножение на все существующие делители
        for (Integer denominator : denominators) {
            System.out.println("Denominator:" + denominator);
            System.out.println();
            System.out.println();
            for (OwnerShare ownerShare : allShares) {
                System.out.println("OwnerShare:" + ownerShare);
                ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominatorCommon() * (denominator));
                ownerShare.setShareNominatorCommon(ownerShare.getShareNominatorCommon() * (denominator));
            }
        }

        //сокращение всех общих множителей делителей
        for (Integer denominator : denominators2) {
            System.out.println("Denominator:" + denominator);
            System.out.println();
            System.out.println();
            for (OwnerShare ownerShare : allShares) {
                System.out.println("OwnerShare:" + ownerShare);
                ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominatorCommon() / (denominator));
                ownerShare.setShareNominatorCommon(ownerShare.getShareNominatorCommon() / (denominator));
            }
        }

        //приведение к нормированному виду за счет деления на изначальный делитель
        for (OwnerShare ownerShare : allShares) {
            System.out.println("OwnerShare:" + ownerShare);
            ownerShare.setShareDenominatorCommon(ownerShare.getShareDenominatorCommon() / ownerShare.getShareDenominator());
            ownerShare.setShareNominatorCommon(ownerShare.getShareNominatorCommon() / ownerShare.getShareDenominator());
        }

        ownerShareRepository.saveAll(allShares);

        List<OwnerShare> allSharesInactive = ownerShareRepository.findByActiveIsFalse();

        for (OwnerShare ownerShare : allSharesInactive) {
            ownerShare.setShareValueCommon(0);
            ownerShare.setShareDenominatorCommon(0);
            ownerShare.setShareNominatorCommon(0);
        }
        ownerShareRepository.saveAll(allSharesInactive);
    }
}

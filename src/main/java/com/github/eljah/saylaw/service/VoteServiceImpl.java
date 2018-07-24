package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.*;
import com.github.eljah.saylaw.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eljah32 on 7/24/2018.
 */
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VoteQuestionRepository voteQuestionRepository;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    ShareVoteRepository shareVoteRepository;

    @Autowired
    OwnerShareVoteRepository ownerShareVoteRepository;

    @Autowired
    OwnerShareVoteQuestionRepository ownerShareVoteQuestionRepository;

    @Autowired
    VoteResultRepository voteResultRepository;

    @Autowired
    VoteQuestionResultRepository voteQuestionResultRepository;

    @Override
    @Transactional
    public Vote newVote(Vote vote) {
        vote.setStatus(Vote.VoteStatus.PREPARED);
        voteRepository.save(vote);
        for (VoteQuestion voteQuestion : vote.getVoteQuestions()) {
            voteQuestion.setVote(vote);
        }
        voteQuestionRepository.saveAll(vote.getVoteQuestions());

        return vote;
    }

    @Override
    @Transactional
    public Vote makeVoteProtocol(Vote vote) throws VoteProcessException {
        if (vote.getStatus() == Vote.VoteStatus.PREPARED) {
            List<Share> shareList = shareRepository.findByActiveIsTrue();
            List<ShareVote> shareVotes = new ArrayList<>();
            for (Share share : shareList) {
                ShareVote shareVote = new ShareVote();
                shareVotes.add(shareVote);
                shareVote.setVote(vote);
                shareVote.setShare(share);
                List<OwnerShareVote> ownerShareVotes=new ArrayList<>();
                for (OwnerShare ownerShare: share.getOwnerShare())
                {
                    OwnerShareVote ownerShareVote=new OwnerShareVote();
                    ownerShareVotes.add(ownerShareVote);
                    ownerShareVote.setOwnerShare(ownerShare);
                    ownerShareVote.setShareVote(shareVote);
                    List<OwnerShareVoteQuestion> ownerShareVoteQuestions=new ArrayList<>();
                    for (VoteQuestion voteQuestion: vote.getVoteQuestions())
                    {
                        OwnerShareVoteQuestion ownerShareVoteQuestion=new OwnerShareVoteQuestion();
                        ownerShareVoteQuestion.setOwnerShare(ownerShare);
                        ownerShareVoteQuestion.setVoteQuestion(voteQuestion);
                        ownerShareVoteQuestion.setOwnerShareVote(ownerShareVote);
                        ownerShareVoteQuestions.add(ownerShareVoteQuestion);
                    }
                    ownerShareVoteQuestionRepository.saveAll(ownerShareVoteQuestions);
                    ownerShareVote.setOwnerShareVoteQuestion(ownerShareVoteQuestions);
                }
                ownerShareVoteRepository.saveAll(ownerShareVotes);
                shareVote.setOwnerShareVotes(ownerShareVotes);
            }
            vote.setShareVotes(shareVotes);
            vote.setStatus(Vote.VoteStatus.INITATED);
            shareVoteRepository.saveAll(shareVotes);
            voteRepository.save(vote);
        } else throw new VoteProcessException();

        return vote;
    }

    @Override
    public Vote finalizeVoteProtocol(Vote vote) throws VoteProcessException {
        if (vote.getStatus() == Vote.VoteStatus.INITATED) {
            for (ShareVote shareVote : vote.getShareVotes()) {
                ShareVoteProtocol shareVoteProtocol= new ShareVoteProtocol();
                //generatefile there
                //todo generate ShareVoteProtocols per Share
                shareVote.setProtocol(shareVoteProtocol);
            }
            vote.setStatus(Vote.VoteStatus.FINALIZED);
            voteRepository.save(vote);
            //save sharevote protocols there
        } else throw new VoteProcessException();

        return vote;
    }

    @Override
    public ShareVote noticeServed(ShareVote shareVote) {
        shareVote.setNoticeServed(true);
        shareVoteRepository.save(shareVote);
        return shareVote;
    }

    @Override
    @Transactional
    public Vote allNoticesServed(Vote vote, Date dateNoticed) throws VoteProcessException {
        if (vote.getStatus() == Vote.VoteStatus.FINALIZED) {
            for (ShareVote shareVote : vote.getShareVotes()) {
                if (!shareVote.isNoticeServed()) {
                    shareVote.setNoticeServed(true);
                    shareVote.setVoteAquired(dateNoticed);
                }
            }
            vote.setStatus(Vote.VoteStatus.NOTICE_SERVED);
            shareVoteRepository.saveAll(vote.getShareVotes());
            voteRepository.save(vote);
        } else throw new VoteProcessException();
        return vote;
    }

    @Override
    @Transactional
    public Vote insertVoteResultsBatch(Vote vote) {
        List<ShareVote> shareVotes= vote.getShareVotes();
        for (ShareVote shareVote: shareVotes)
        {
            shareVote.setShareNominator(shareVote.getShare().getShareNominator());
            shareVote.setShareDenominator(shareVote.getShare().getShareDenominator());
            shareVote.setShareValue(shareVote.getShare().getShareValue());
            List<OwnerShareVote> ownerShareVotes= shareVote.getOwnerShareVotes();
            for (OwnerShareVote ownerShareVote: ownerShareVotes)
            {
                ownerShareVote.setShareNominator(ownerShareVote.getOwnerShare().getShareNominator());
                ownerShareVote.setShareDenominator(ownerShareVote.getOwnerShare().getShareDenominator());
                ownerShareVote.setShareValue(ownerShareVote.getOwnerShare().getShareValue());
                ownerShareVote.setShareNominatorCommon(ownerShareVote.getOwnerShare().getShareNominatorCommon());
                ownerShareVote.setShareDenominatorCommon(ownerShareVote.getOwnerShare().getShareDenominatorCommon());
                ownerShareVote.setShareValueCommon(ownerShareVote.getOwnerShare().getShareValueCommon());
                ownerShareVoteQuestionRepository.saveAll(ownerShareVote.getOwnerShareVoteQuestion());
            }
            ownerShareVoteRepository.saveAll(ownerShareVotes);
        }
        shareVoteRepository.saveAll(shareVotes);
        vote.setStatus(Vote.VoteStatus.ON_SITE_RESULTS_OBTAINED);
        voteRepository.save(vote);
        return vote;
    }

    @Override
    @Transactional
    public ShareVote insertVoteResultPerShare(ShareVote shareVote) {
        shareVote.setShareNominator(shareVote.getShare().getShareNominator());
        shareVote.setShareDenominator(shareVote.getShare().getShareDenominator());
        shareVote.setShareValue(shareVote.getShare().getShareValue());
        List<OwnerShareVote> ownerShareVotes= shareVote.getOwnerShareVotes();
        for (OwnerShareVote ownerShareVote: ownerShareVotes)
        {
            ownerShareVote.setShareNominator(ownerShareVote.getOwnerShare().getShareNominator());
            ownerShareVote.setShareDenominator(ownerShareVote.getOwnerShare().getShareDenominator());
            ownerShareVote.setShareValue(ownerShareVote.getOwnerShare().getShareValue());
            ownerShareVote.setShareNominatorCommon(ownerShareVote.getOwnerShare().getShareNominatorCommon());
            ownerShareVote.setShareDenominatorCommon(ownerShareVote.getOwnerShare().getShareDenominatorCommon());
            ownerShareVote.setShareValueCommon(ownerShareVote.getOwnerShare().getShareValueCommon());
            ownerShareVoteQuestionRepository.saveAll(ownerShareVote.getOwnerShareVoteQuestion());
        }
        ownerShareVoteRepository.saveAll(ownerShareVotes);
        shareVoteRepository.save(shareVote);
        return shareVote;
    }

    @Override
    @Transactional
    public Vote completeVoteResultsBatch(Vote vote) {
        //todo generate Vote Protocol
        //todo generate VoteQuestion Protocol
        VoteResult voteResult=new VoteResult();

        List<VoteQuestionResult> voteQuestionResults=new ArrayList<>();
        for (VoteQuestion voteQuestion: vote.getVoteQuestions())
        {
            VoteQuestionResult voteQuestionResult=new VoteQuestionResult();
            voteQuestionResults.add(voteQuestionResult);
            List<OwnerShareVoteQuestion> ownerShareVoteQuestions=voteQuestion.getOwnerShareVoteQuestions();
            for (OwnerShareVoteQuestion ownerShareVoteQuestion: ownerShareVoteQuestions)
            {
                long commonDenominator=ownerShareVoteQuestion.getOwnerShare().getShareDenominatorCommon();
                voteQuestionResult.setDenominator(commonDenominator);
                if (ownerShareVoteQuestion.isAgree())
                {
                    voteQuestionResult.setNomintorPro(voteQuestionResult.getNomintorPro()+ownerShareVoteQuestion.getOwnerShare().getShareNominatorCommon());
                }
                else
                {
                    voteQuestionResult.setNomintorContra(voteQuestionResult.getNomintorContra()+ownerShareVoteQuestion.getOwnerShare().getShareNominatorCommon());
                }
            }
            voteQuestionResult.setPro(voteQuestionResult.getNomintorPro()>voteQuestionResult.getNomintorContra());
            voteQuestionResult.setValuePro((double)voteQuestionResult.getNomintorPro()/(double)voteQuestionResult.getDenominator());
            voteQuestionResult.setValueContra((double)voteQuestionResult.getNomintorContra()/(double)voteQuestionResult.getDenominator());
        }
        voteQuestionResultRepository.saveAll(voteQuestionResults);
        voteResult.setVoteQuestionResults(voteQuestionResults);
        voteResultRepository.save(voteResult);
        vote.setStatus(Vote.VoteStatus.MAILBOX_RESULTS_OBTAINED);
        voteRepository.save(vote);
        return vote;
    }
}

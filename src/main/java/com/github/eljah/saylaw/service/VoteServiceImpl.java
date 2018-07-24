package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.*;
import com.github.eljah.saylaw.repository.ShareRepository;
import com.github.eljah.saylaw.repository.ShareVoteRepository;
import com.github.eljah.saylaw.repository.VoteQuestionRepository;
import com.github.eljah.saylaw.repository.VoteRepository;
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
            vote.setStatus(Vote.VoteStatus.NOTIICE_SERVED);
            shareVoteRepository.saveAll(vote.getShareVotes());
            voteRepository.save(vote);
        } else throw new VoteProcessException();
        return vote;
    }
}

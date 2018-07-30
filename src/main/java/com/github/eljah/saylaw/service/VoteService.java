package com.github.eljah.saylaw.service;

import com.github.eljah.saylaw.model.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by eljah32 on 7/24/2018.
 */
public interface VoteService {
    Vote newVote(Vote vote);

    Vote makeVoteProtocol(Vote vote) throws VoteProcessException;

    Vote finalizeVoteProtocol(Vote vote) throws Exception;

    ShareVote noticeServed(ShareVote shareVote);

    Vote allNoticesServed(Vote vote, Date dateNoticed) throws VoteProcessException;

    Vote insertVoteResultsBatch(Vote vote);

    ShareVote insertVoteResultPerShare(ShareVote shareVote);

    Vote completeVoteResultsBatch(Vote vote) throws VoteProcessException;

    List<Owner> getAllOwners();

    List<Vote> getAllVotes();

    Vote getVoteById(Long id);
}

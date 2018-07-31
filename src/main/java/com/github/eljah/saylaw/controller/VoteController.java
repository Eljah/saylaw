package com.github.eljah.saylaw.controller;

import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Vote;
import com.github.eljah.saylaw.model.VoteProcessException;
import com.github.eljah.saylaw.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by eljah32 on 7/25/2018.
 */
@Controller
@SessionAttributes("voteR")
public class VoteController {

    @Autowired
    VoteService voteService;

    @GetMapping("/addVote")
    @Transactional
    public String addShare(Model model) {

        Vote vote = new Vote();
        model.addAttribute("vote", vote);
        List<Owner> owners = voteService.getAllOwners();
        model.addAttribute("owners", owners);
        return "addVote";
    }

    @PostMapping("/addVote")
    public String addShare(@ModelAttribute Vote vote, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
        }
        voteService.newVote(vote);
        return "redirect:showVotes";
    }

    @GetMapping("/showVotes")
    public String showVotes(Model model) {

        List<Vote> votes = voteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "showVotes";
    }

    @GetMapping("/makeVoteProtocol")
    public String makeVoteProtocol(@RequestParam("voteId") Long voteId, Model model) throws VoteProcessException {
        Vote vote = voteService.getVoteById(voteId);
        voteService.makeVoteProtocol(vote);
        List<Vote> votes = voteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "redirect:showVotes";
    }

    //NB!
    //this is the controller that returns non usual view, non-html
    //it returns docx view and "produces =" parameter is needed, it doesn't work correctly without it
    @GetMapping(path = "/viewVoteProtocol", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public String viewVoteProtocol(@RequestParam("voteId") Long voteId, Model model) throws VoteProcessException {
        Vote vote = voteService.getVoteById(voteId);
        model.addAttribute("vote", vote);
        model.addAttribute("name", vote.getName());
        model.addAttribute("__filename", new Date());
        return "voteProtocol2";
    }

    @GetMapping("/finalizeVoteProtocol")
    public String finalizeVoteProtocol(@RequestParam("voteId") Long voteId, Model model) throws Exception {
        Vote vote = voteService.getVoteById(voteId);
        voteService.finalizeVoteProtocol(vote);
        List<Vote> votes = voteService.getAllVotes();
        model.addAttribute("vote", vote);
        model.addAttribute("name", vote.getName());
        model.addAttribute("votes", votes);
        return "redirect:showVotes";
    }

    @GetMapping("/allNoticesServed")
    public String allNoticesServed(@RequestParam("voteId") Long voteId, Model model) throws VoteProcessException {
        Vote vote = voteService.getVoteById(voteId);
        voteService.allNoticesServed(vote, new Date());
        List<Vote> votes = voteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "redirect:showVotes";
    }

    @GetMapping("/insertVoteResults")
    public String insertVoteResults(@RequestParam("voteId") Long voteId, Model model) {
        Vote vote = voteService.getVoteById(voteId);
        model.addAttribute("voteR", vote);
        return "insertVoteResults";
    }

    @PostMapping("/insertVoteResults")
    public String insertVoteResults(@ModelAttribute Vote vote, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vote",vote);
            return "insertVoteResults";
        } else {
            voteService.insertVoteResultsBatch(vote);
            List<Vote> votes = voteService.getAllVotes();
            model.addAttribute("votes", votes);
            return "redirect:showVotes";
        }
    }

    @GetMapping("/сompleteVoteResults")
    public String сompleteVoteResults(@RequestParam("voteId") Long voteId, Model model) throws VoteProcessException {
        Vote vote = voteService.getVoteById(voteId);
        voteService.completeVoteResultsBatch(vote);
        List<Vote> votes = voteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "redirect:showVotes";
    }

    @GetMapping("/voteDetails")
    public String voteDetails(@RequestParam("voteId") Long voteId, Model model) throws VoteProcessException {
        Vote vote = voteService.getVoteById(voteId);
        model.addAttribute("vote", vote);
        List<Owner> owners = voteService.getAllOwners();
        model.addAttribute("owners", owners);
        return "voteDetails";
    }
}

package com.github.eljah.saylaw.controller;

import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Vote;
import com.github.eljah.saylaw.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by eljah32 on 7/25/2018.
 */
@Controller
public class VoteController {

    @Autowired
    VoteService voteService;

    @GetMapping("/addVote")
    @Transactional
    public String addShare(Model model) {

        Vote vote=new Vote();
        model.addAttribute("vote", vote);
        List<Owner> owners=voteService.getAllOwners();
        //for (Owner owner: owners)
        //{
        //    owner.getOwnerShare();
        //}
        model.addAttribute("owners",owners);
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

        List<Vote> votes=voteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "showVotes";
    }
}

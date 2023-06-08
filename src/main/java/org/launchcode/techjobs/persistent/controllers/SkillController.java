package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public String displaySkills(Model model){
        model.addAttribute("title", "Skills");
        model.addAttribute("skills", skillRepository.findAll());
        return "skills/index";
    }

    @GetMapping("add")
    public String displayAddSkillForm(Model model){
        model.addAttribute(new Skill());
        return "skills/add";
    }

    @PostMapping("add")
    public String processAddSkillForm(@ModelAttribute @Valid Skill newskill,
                                       Errors errors, Model model){
        if(errors.hasErrors()){
            return "skills/add";
        }
        skillRepository.save(newskill);
        return "redirect:";
    }

    @GetMapping("view/{skillId}")
    public String displayViewSkill(Model model, @PathVariable int skillId){
        Optional optSkill = skillRepository.findById(skillId);
        if(optSkill.isPresent()){
            Skill skill = (Skill) optSkill.get();
            Iterable<Job> allJobs = jobRepository.findAll();
            ArrayList<Job> skillJobs = new ArrayList<>();
            for(Job job : allJobs){
                if(job.getSkills().contains(skill)){
                    skillJobs.add(job);
                }
            }
            model.addAttribute("skill", skill);
            model.addAttribute("jobs", skillJobs);
            return "skills/view";
        } else{
            return "redirect:../";
        }
    }
}

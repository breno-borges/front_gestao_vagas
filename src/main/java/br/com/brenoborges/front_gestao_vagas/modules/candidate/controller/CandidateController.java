package br.com.brenoborges.front_gestao_vagas.modules.candidate.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.CreateCandidateDTO;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.ProfileUserDTO;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.Token;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.service.ApplyJobService;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.service.LoginCandidateService;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.service.CreateCandidateService;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.service.FindJobService;
import br.com.brenoborges.front_gestao_vagas.modules.candidate.service.ProfileCandidateService;
import br.com.brenoborges.front_gestao_vagas.utils.FormatErrorMessage;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private LoginCandidateService loginCandidateService;

    @Autowired
    private ProfileCandidateService profileCandidateService;

    @Autowired
    private FindJobService findJobService;

    @Autowired
    private ApplyJobService applyJobService;

    @Autowired
    private CreateCandidateService createCandidateService;

    @GetMapping("/login")
    public String login() {
        return "candidate/login";
    }

    @PostMapping("/signIn")
    public String signIn(RedirectAttributes redirectAttributes, HttpSession session, String username, String password) {
        try {
            Token token = this.loginCandidateService.login(username, password);
            var grants = token.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                    .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", token);

            return "redirect:/candidate/profile";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Usuário ou senha incorretos");
            return "redirect:/candidate/login";
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String profile(Model model) {

        try {
            ProfileUserDTO user = this.profileCandidateService.execute(getToken());

            model.addAttribute("user", user);

            return "candidate/profile";
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String jobs(Model model, String filter) {

        try {
            if (filter != null) {
                var jobs = this.findJobService.execute(getToken(), filter);
                model.addAttribute("jobs", jobs);
            }
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }
        return "candidate/jobs";
    }

    @PostMapping("/jobs/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String applyJob(@RequestParam("jobId") UUID jobId) {

        try {
            this.applyJobService.execute(getToken(), jobId);
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }

        return "redirect:/candidate/jobs";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("candidate", new CreateCandidateDTO());
        return "candidate/create";
    }

    @PostMapping("/create")
    public String save(CreateCandidateDTO candidate, Model model) {

        try {
            this.createCandidateService.execute(candidate);
            return "redirect:/candidate/login";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error_message", FormatErrorMessage.formatErrorMessage(e.getResponseBodyAsString()));
            model.addAttribute("candidate", candidate);
        }

        return "candidate/create";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}

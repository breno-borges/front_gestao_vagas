package br.com.brenoborges.front_gestao_vagas.modules.company.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.brenoborges.front_gestao_vagas.modules.company.dto.Token;
import br.com.brenoborges.front_gestao_vagas.modules.company.dto.CreateCompanyDTO;
import br.com.brenoborges.front_gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.brenoborges.front_gestao_vagas.modules.company.dto.JobDTO;
import br.com.brenoborges.front_gestao_vagas.modules.company.service.LoginCompanyService;
import br.com.brenoborges.front_gestao_vagas.modules.company.service.CreateCompanyService;
import br.com.brenoborges.front_gestao_vagas.modules.company.service.CreateJobUseCase;
import br.com.brenoborges.front_gestao_vagas.modules.company.service.ListAllJobsCompanyService;
import br.com.brenoborges.front_gestao_vagas.utils.FormatErrorMessage;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CreateCompanyService createCompanyService;

    @Autowired
    private LoginCompanyService loginCompanyService;

    @Autowired
    private CreateJobUseCase createJobUseCase;

    @Autowired
    private ListAllJobsCompanyService listAllJobsCompanyService;

    @GetMapping("/login")
    public String login() {
        return "company/login";
    }

    @PostMapping("/signIn")
    public String signIn(RedirectAttributes redirectAttributes, HttpSession session, String username, String password) {
        try {
            Token token = this.loginCompanyService.login(username, password);
            var grants = token.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                    .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", token);

            return "redirect:/company/jobs";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Usuário ou senha incorretos");
            return "redirect:/company/login";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("company", new CreateCompanyDTO());
        return "company/create";
    }

    @PostMapping("/create")
    public String save(Model model, CreateCompanyDTO company) {
        try {
            this.createCompanyService.execute(company);
            return "redirect:/company/login";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error_message", FormatErrorMessage.formatErrorMessage(e.getResponseBodyAsString()));
            model.addAttribute("company", company);
        }

        return "company/create";
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String jobs(Model model) {
        model.addAttribute("job", new CreateJobDTO());
        return "company/jobs";
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String createJobs(Model model, CreateJobDTO job) {
        try {
            this.createJobUseCase.execute(getToken(), job);
            return "redirect:/company/jobs/list";
        } catch (HttpClientErrorException e) {
            return "redirect:/company/login";
        }
    }

    @GetMapping("/jobs/list")
    @PreAuthorize("hasRole('COMPANY')")
    public String list(Model model) {

        try {
            List<JobDTO> listJobs = this.listAllJobsCompanyService.execute(getToken());
            model.addAttribute("jobs", listJobs);
        } catch (HttpClientErrorException e) {
            return "redirect:/company/login";
        }
        return "company/list";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", null);
            return "redirect:/company/login";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Deslogando");
            return "redirect:/company/login";
        }
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}

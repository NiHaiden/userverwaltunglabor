package tech.niklas.userverwaltung.web.presentation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.niklas.userverwaltung.db.AntwortRepository;
import tech.niklas.userverwaltung.db.FrageRepository;
import tech.niklas.userverwaltung.db.UserRepository;
import tech.niklas.userverwaltung.model.Antwort;
import tech.niklas.userverwaltung.model.Antwortmoeglichkeiten;
import tech.niklas.userverwaltung.model.Frage;
import tech.niklas.userverwaltung.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class WebController {

    AntwortRepository antwortRepository;
    FrageRepository frageRepository;
    UserRepository userRepository;

    @Autowired
    public WebController(AntwortRepository antwortRepository, FrageRepository frageRepository, UserRepository userRepository) {
        this.antwortRepository = antwortRepository;
        this.frageRepository = frageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    private String redirectSuccess(Model model, Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/home";
        }

        return showHome(model, auth);
    }

    @GetMapping({"home"})
    public String showHome(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });
        model.addAttribute("user", user);
        model.addAttribute("openQuestions", frageRepository.getUnansweredQuestionsByUser(user, LocalDate.now()));
        return "home";
    }

    @GetMapping("/question/answer/{id}")
    public String answerQuestion(@PathVariable Integer id, Model model, Authentication authentication) {
        Frage frage = frageRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Frage wurde nicht gefunden!");
        });

        model.addAttribute("usermail", authentication.getName());
        model.addAttribute("currentQ", frage);
        model.addAttribute("options", Antwortmoeglichkeiten.values());
        model.addAttribute("answer", new Antwort());

        return "answer_question";
    }

    @GetMapping("admin/home")
    public String adminHome(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });
        model.addAttribute("user", user);
        return "admin-home";
    }

    @GetMapping("admin/newquestion")
    private String newQuestion(Model model, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/home";
        }

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });

        model.addAttribute("user", user);
        model.addAttribute("frage", new Frage());

        return "newquestion";
    }

    @PostMapping("admin/newquestion")
    public String addNewQuestion(@Valid Frage frage, BindingResult bindingResult, Model model, Authentication authentication){

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });

        if(bindingResult.hasErrors()) {
           model.addAttribute("user", user);
           return "newquestion";
       }

        frageRepository.save(frage);
        return "redirect:/admin/home";

    }

    @GetMapping("admin/allquestions")
    private String allQuestionsAdmin(Model model, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/home";
        }
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });
        model.addAttribute("user", user);
        model.addAttribute("fragen", frageRepository.findAll());
        model.addAttribute("fragenRepo", frageRepository);
        model.addAttribute("volleZustimmung", Antwortmoeglichkeiten.VolleZustimmung);
        model.addAttribute("teilweiseZustimmung", Antwortmoeglichkeiten.TeilweiseZustimmung);
        model.addAttribute("keineZustimmung", Antwortmoeglichkeiten.KeineZustimmung);

        return "allquestions";
    }

    @PostMapping("/newanswer")
    public String saveAnswer(@RequestParam(name = "questionID") Integer questionID, @Valid Antwort antwort, BindingResult bindingResult
    , Model model, Authentication authentication){
        Frage frage = frageRepository.findById(questionID).orElseThrow(() -> {
            throw new IllegalArgumentException("Frage wurde nicht gefunden!");
        });



        if(bindingResult.hasErrors()) {
            model.addAttribute("usermail", authentication.getName());
            model.addAttribute("currentQ", frage);
            model.addAttribute("options", Antwortmoeglichkeiten.values());

            return "answer_question";
        }

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("User wurde nicht gefunden!");
        });

        antwort.setFrage(frage);
        antwort.setUser(user);
        antwort.setTimestamp(LocalDateTime.now());

        antwortRepository.save(antwort);
        System.out.println(antwort);
        return "redirect:/home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

}

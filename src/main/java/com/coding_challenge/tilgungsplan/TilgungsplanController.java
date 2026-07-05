package com.coding_challenge.tilgungsplan;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class TilgungsplanController implements WebMvcConfigurer {

    private static final String INPUT_FORM_PAGE = "inputForm";
    private static final String RESULT_PAGE = "result";
    private static final String ERROR_PAGE = "error";


    private TilgungsplanService tillgungsplanService;

    public TilgungsplanController(TilgungsplanService tillgungsplanService) {
        this.tillgungsplanService = tillgungsplanService;
    }

    @GetMapping("/")
    public String inputForm(InputForm inputForm) {
        return INPUT_FORM_PAGE;
    }

    @PostMapping("/")
    public String inputForm(@Valid @ModelAttribute("inputForm") InputForm inputForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inputErrors", bindingResult.getFieldErrors());
                    /*.stream()
                    .map(fieldError -> String.format("Feld: %s, abgelehnter Wert: %s, Nachricht: %s",
                            fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage())).collect(
                            Collectors.joining("; \n"))); */
            return ERROR_PAGE;
        }
        model.addAttribute("tillgungsplanList", tillgungsplanService.calculateTilgung(inputForm));
        return RESULT_PAGE;
    }

    @ExceptionHandler(TilgungsplanServiceException.class)
    public String handleInvoiceScannerServiceException(TilgungsplanServiceException ex, Model model) {
        model.addAttribute("tillgungsplanServiceException", ex.getMessage());
        return ERROR_PAGE;
    }
}


/*
* TODO:
* - add unit und integration tests
* - add docker file and instructions to run the app
* - add documentation in code and in README file
* - change last entry "datum" to "Zinsbindungsende"
* - Add info buttons in input form
* - Change datum format (dd-MM-YYYY)
* - Show numbers in result table with coma and not with .
* - Show numbers in result table with separator (1`000`000) for clarity
* - Code refactoring
* - Add additional checks for edge cases..
* */

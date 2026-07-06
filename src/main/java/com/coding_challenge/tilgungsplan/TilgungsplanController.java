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
    public String inputForm(@Valid InputForm inputForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inputErrors", bindingResult.getFieldErrors());
            return ERROR_PAGE;
        }
        model.addAttribute("tillgungsplanList", tillgungsplanService.calculateTilgung(inputForm));
        return RESULT_PAGE;
    }

    @ExceptionHandler(TilgungsplanServiceException.class)
    public String handleInvoiceScannerServiceException(TilgungsplanServiceException ex, Model model) {
        model.addAttribute("tillgungsplanServiceException", ex.getMessage());
        return ERROR_PAGE;  // TODO: throw some exception
    }
}


/*
* TODO:
* - add unit und integration tests
* - add docker file and instructions to run the app
* - add documentation in code and in README file
* - Add info buttons in input form (with title?)
* - Code refactoring
* - Add additional checks for edge cases..
* - Throw some custom exception..
* DIfference between new BigDecimal and Bigdecimal.valueof() https://raphaeldelio.medium.com/the-difference-between-new-bigdecimal-and-bigdecimal-valueof-f08a4b7ce36d
* */

package com.coding_challenge.tilgungsplan;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class TilgungsplanController implements WebMvcConfigurer {

    private static final String INPUT_FORM_PAGE = "inputForm";
    private static final String RESULT_PAGE = "result";
    private static final String ERROR_PAGE = "error";
    private static final Logger LOGGER = LoggerFactory.getLogger(TilgungsplanService.class);

    private TilgungsplanService tilgungsplanService;

    public TilgungsplanController(TilgungsplanService tilgungsplanService) {
        this.tilgungsplanService = tilgungsplanService;
    }

    @GetMapping("/")
    public String inputForm(InputForm inputForm) {
        return INPUT_FORM_PAGE;
    }

    @PostMapping("/")
    public String inputForm(@Valid
                            InputForm inputForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inputErrors", bindingResult.getFieldErrors());
            return INPUT_FORM_PAGE;
        }
        LOGGER.info("Berechnung vom Tilgungsplan mit folgenden Werten: {}", inputForm);
        model.addAttribute("tilgungsplanList", tilgungsplanService.calculateTilgung(inputForm));
        return RESULT_PAGE;
    }

    @ExceptionHandler(TilgungsplanServiceException.class)
    public String handleInvoiceScannerServiceException(TilgungsplanServiceException ex, Model model) {
        model.addAttribute("tilgungsplanServiceException", ex.getMessage());
        return ERROR_PAGE;  // TODO: throw some exception
    }
}


/*
 * TODO:
 *  - add some more unit test
 * - add documentation in code and in README file, with calculation rules
 * - Code refactoring
 * - Throw some custom exceptions, ..
 * DIfference between new BigDecimal and Bigdecimal.valueof() https://raphaeldelio.medium.com/the-difference-between-new-bigdecimal-and-bigdecimal-valueof-f08a4b7ce36d
 * */

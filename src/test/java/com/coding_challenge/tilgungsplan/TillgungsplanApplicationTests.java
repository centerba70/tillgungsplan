package com.coding_challenge.tilgungsplan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
class TillgungsplanApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("${messages.error.invalid_input}")
	private String invalidInputErrorText;

	@Value("${messages.result.last_element}")
	private String lastElementText;

	@Test
	void loadFirstPage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Tillgungsplan Rechner")));
	}

	@Test
	void loadResultPageWithEmptyFields() throws Exception {
		this.mockMvc.perform(post("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(invalidInputErrorText)));
	}

	@Test
	void loadResultPageWithInvalidElements() throws Exception {
		// Given
		InputForm inputForm = new InputForm(BigDecimal.ZERO, null, null, 0L);
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("darlehensbetrag", String.valueOf(inputForm.darlehensbetrag()));
		multiValueMap.add("zinssatz", String.valueOf(inputForm.zinssatz()));
		multiValueMap.add("anfaenglicheTilgung", String.valueOf(inputForm.anfaenglicheTilgung()));
		multiValueMap.add("zinsbindung", String.valueOf(inputForm.zinsbindung()));

		// Then
		this.mockMvc.perform(post("/").formFields(multiValueMap)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(invalidInputErrorText)));
	}

	@Test
	void loadResultPageWithAllCorrectInputs() throws Exception {
		// Given
		InputForm inputForm = new InputForm(BigDecimal.valueOf(100000), BigDecimal.valueOf(2.5), BigDecimal.valueOf(2), 10L);
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("darlehensbetrag", String.valueOf(inputForm.darlehensbetrag()));
		multiValueMap.add("zinssatz", String.valueOf(inputForm.zinssatz()));
		multiValueMap.add("anfaenglicheTilgung", String.valueOf(inputForm.anfaenglicheTilgung()));
		multiValueMap.add("zinsbindung", String.valueOf(inputForm.zinsbindung()));


		// Then
		this.mockMvc.perform(post("/").formFields(multiValueMap)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(lastElementText)));
	}

}

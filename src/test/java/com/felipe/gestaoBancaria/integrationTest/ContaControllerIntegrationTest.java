package com.felipe.gestaoBancaria.integrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContaControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;


    @Test
    void deveCriarNovaContaComSucesso() throws Exception
    {
        String json = """
            {
              "numero_conta": 999,
              "saldo": 1500.00
            }
            """;

        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero_conta", is(999)))
                .andExpect(jsonPath("$.saldo", is(1500.0)));
    }

    @Test
    void deveRetornarErroAoCriarContaExistente() throws Exception
    {
        String json = """
            {
              "numero_conta": 100,
              "saldo": 500.00
            }
            """;

        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", containsString("Conta já existe")));
    }

    @Test
    void deveRetornarErroDeValidacaoQuandoCamposObrigatoriosForemNulos() throws Exception
    {
        String json = """
            {
              "numero_conta": null,
              "saldo": null
            }
            """;

        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Erro de validação")))
                .andExpect(jsonPath("$.message", containsString("O número da conta é obrigatório.")))
                .andExpect(jsonPath("$.message", containsString("O saldo inicial é obrigatório.")));
    }

    @Test
    void deveRetornarErroDeValidacaoQuandoNumeroContaForNulo() throws Exception
    {
        String json = """
            {
              "numero_conta": null,
              "saldo": 10.00
            }
            """;

        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Erro de validação")))
                .andExpect(jsonPath("$.message", containsString("O número da conta é obrigatório.")));
    }

    @Test
    void deveRetornarErroDeValidacaoQuandoSaldoForNulo() throws Exception
    {
        String json = """
            {
              "numero_conta": 15,
              "saldo": null
            }
            """;

        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Erro de validação")))
                .andExpect(jsonPath("$.message", containsString("O saldo inicial é obrigatório.")));
    }

    @Test
    void deveConsultarContaExistente() throws Exception
    {
        mockMvc.perform(get("/conta")
                        .param("numero_conta", "102"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero_conta", is(102)))
                .andExpect(jsonPath("$.saldo", is(2000.0)));
    }

    @Test
    void deveRetornar404ParaContaInexistente() throws Exception
    {
        mockMvc.perform(get("/conta")
                        .param("numero_conta", "9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Conta não encontrada.")));
    }
}

package com.felipe.gestaoBancaria.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Test
    void deveRealizarTransacaoPOST() throws Exception
    {
        String json = """
            {
              "numero_conta": 100,
              "forma_pagamento": "P",
              "valor": 50.00
            }
            """;

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero_conta", is(100)))
                .andExpect(jsonPath("$.saldo", greaterThanOrEqualTo(0.0)));
    }

    @Test
    void deveListarTodasTransacoesPorConta() throws Exception
    {
        mockMvc.perform(get("/transacao/todas")
                        .param("numero_conta", "102"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].numeroConta", everyItem(is(102))));
    }

    @Test
    void deveListarTransacoesPorFormaPagamento() throws Exception
    {
        mockMvc.perform(get("/transacao/forma-pagamento")
                        .param("numero_conta", "102")
                        .param("forma", "P"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].formaPagamento", everyItem(is("P"))));
    }

    @Test
    void deveListarTransacoesPorValorMaiorQue() throws Exception
    {
        mockMvc.perform(get("/transacao/valor-minimo")
                        .param("numero_conta", "104")
                        .param("valor", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].valor", everyItem(greaterThan(100.0))));
    }

    @Test
    void deveListarTransacoesOrdenadasPorData() throws Exception
    {
        var response = mockMvc.perform(get("/transacao/ordenadas")
                        .param("numero_conta", "104"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var datas = new ObjectMapper()
                .readTree(response)
                .findValuesAsText("data_transacao");

        var datasOrdenadas = new ArrayList<>(datas);
        datasOrdenadas.sort(Comparator.reverseOrder());

        assertThat(datas).isEqualTo(datasOrdenadas);
    }

    @Test
    void deveListarTransacoesPorIntervaloDeDatas() throws Exception
    {
        LocalDateTime inicio = LocalDateTime.now().minusDays(15);
        LocalDateTime fim = LocalDateTime.now();

        mockMvc.perform(get("/transacao/intervalo")
                        .param("numero_conta", "105")
                        .param("inicio", inicio.format(formatter))
                        .param("fim", fim.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void deveRetornarNoContentQuandoNaoExistirTransacao() throws Exception
    {
        mockMvc.perform(get("/transacao/todas")
                        .param("numero_conta", "99999"))
                .andExpect(status().isNoContent());
    }
}

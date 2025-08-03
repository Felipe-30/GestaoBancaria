package com.felipe.gestaoBancaria.unitTest;

import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BigDecimalUtilsUnitTest
{
    @Nested
    class ArredondamentoTest
    {
        @Test
        @DisplayName("Deve arredondar corretamente valores com meio centavo para cima")
        void arredondarMeioCentavoParaCima()
        {
            BigDecimal valor = new BigDecimal("0.005");
            BigDecimal arredondado = BigDecimalUtils.arredondar(valor);

            assertEquals(new BigDecimal("0.01"), arredondado);
        }

        @Test
        @DisplayName("Deve arredondar corretamente valores com meio centavo para baixo")
        void arredondarMeioCentavoParaBaixo()
        {
            BigDecimal valor = new BigDecimal("2.004");
            BigDecimal arredondado = BigDecimalUtils.arredondar(valor);

            assertEquals(new BigDecimal("2.00"), arredondado);
        }

        @Test
        @DisplayName("Deve manter duas casas decimais no resultado final")
        void manterDuasCasasDecimais()
        {
            BigDecimal valor = new BigDecimal("10.336");
            BigDecimal arredondado = BigDecimalUtils.arredondar(valor);

            assertEquals(new BigDecimal("10.34"), arredondado);
        }

        @Test
        @DisplayName("Deve manter valor com duas casas decimais sem alteração")
        void manterValorComDuasCasas()
        {
            BigDecimal valor = new BigDecimal("25.50");
            BigDecimal arredondado = BigDecimalUtils.arredondar(valor);

            assertEquals(valor, arredondado);
        }
    }
}

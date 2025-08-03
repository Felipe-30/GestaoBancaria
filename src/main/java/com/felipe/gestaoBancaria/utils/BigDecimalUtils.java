package com.felipe.gestaoBancaria.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils
{
    public static BigDecimal arredondar(BigDecimal valor)
    {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}


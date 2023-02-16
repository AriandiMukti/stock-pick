package com.amm.springboot.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
// import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Stocks")
public class Stocks implements Serializable {

    @Id
    private int id;
    private String stockName;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal change;
    private long volume;
}

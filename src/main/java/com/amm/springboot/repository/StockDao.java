package com.amm.springboot.repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.amm.springboot.entity.Stocks;
import com.amm.springboot.service.Producer;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Repository
public class StockDao {

    public static final String HASH_KEY = "Stock";
    private static final AtomicInteger counter = new AtomicInteger(1);
    
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    
    @Autowired
	Producer producer;
    
    public Stocks save(Stocks stocks){

        try {
            Stock stockRecord = YahooFinance.get(stocks.getStockName() + ".JK");
            stocks.setStockName(stocks.getStockName().toUpperCase());
            stocks.setId(counter.getAndIncrement());
            stocks.setOpenPrice(stockRecord.getQuote().getOpen());
            stocks.setClosePrice(stockRecord.getQuote().getPrice());
            stocks.setLow(stockRecord.getQuote().getDayLow());
            stocks.setHigh(stockRecord.getQuote().getDayHigh());
            stocks.setChange(stockRecord.getQuote().getChange());
            stocks.setVolume(stockRecord.getQuote().getVolume());
            template.opsForHash().put(HASH_KEY,stocks.getId(), stocks);

        } catch (Exception e) {
            producer.send(stocks.getStockName() + " failed to add, Reason : " + e);
            return stocks;
        }
        producer.send(stocks.getStockName() + " added Successfully");

        return stocks;
    }

    public List<Stocks> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public List<Stocks> orderByPrice(String type){
        List<Stocks> stockList = template.opsForHash().values(HASH_KEY);

        List<Stocks> sortedList;

        if (type.toUpperCase().equals("HIGH")){
            sortedList = stockList.stream()
            .sorted(Comparator.comparing(Stocks::getClosePrice)
            .reversed())
            .collect(Collectors.toList());
        }else{
            sortedList = stockList.stream()
            .sorted(Comparator.comparing(Stocks::getClosePrice))
            .collect(Collectors.toList());
        }
        

        return sortedList;
    }

    public Stocks findProductById(int id){
        return (Stocks) template.opsForHash().get(HASH_KEY,id);
    }


    public String deleteProduct(int id){
        try{

         template.opsForHash().delete(HASH_KEY,id);

        } catch (Exception e) {
            producer.send("deleted failed, Reason : " + e);
            String result = "deleted failed, Reason : " + e;
            return result;
        }
        producer.send(" deleted Successfully");
        return "product removed !!";
    }
}

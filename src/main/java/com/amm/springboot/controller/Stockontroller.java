package com.amm.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amm.springboot.entity.Stocks;
import com.amm.springboot.repository.StockDao;

@RestController
@RequestMapping("/stock")
public class Stockontroller {

    @Autowired
    private StockDao stockDao;


    @PostMapping
    public Stocks save(@RequestBody Stocks stock) {

        return stockDao.save(stock);
    }

    @GetMapping(value = "/order")
    public List<Stocks> orderFromHighestPrice(@RequestParam(name="type", required=false, defaultValue="high") String type ){
        return stockDao.orderByPrice(type);
    }

    @GetMapping
    public List<Stocks> getAllProducts() {
        return stockDao.findAll();
    }

    // @Cacheable(key = "#stockName",value = "Stock", unless = "#result.volume < 1000000")
    @GetMapping("/{id}")
    @Cacheable(key = "#id",value = "Stock")
    public Stocks findProduct(@PathVariable int id) {
        return stockDao.findProductById(id);
    }
    
    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id",value = "Stock")
    public String remove(@PathVariable int id)   {
    	return stockDao.deleteProduct(id);
	}
    
    
}

package com.example_springbatch1.demoDrink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DrinkItemProcessor implements ItemProcessor<Drink, Drink> {

    public DrinkItemProcessor() {
        System.out.println("reaching drinkItemProcessor\n");
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(DrinkItemProcessor.class);

    @Override
    public Drink process(final Drink drink) throws Exception {
        System.out.println("processing the drink list");
        String brand = drink.getBrand().toUpperCase();
        String origin = drink.getOrigin().toUpperCase();
        String characteristics = drink.getCharacteristics().toUpperCase();

        Drink transformedDrink = new Drink(brand, origin, characteristics);
        LOGGER.info("Converting ( {} ) into ( {} )", drink, transformedDrink);

        return transformedDrink;
    }


}

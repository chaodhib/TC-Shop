package com.chaouki.tcshop;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.services.CharacterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TcShopApplicationTests {

    @Autowired
    private CharacterService characterService;

	@Test
	public void contextLoads() {
        List<Character> characters = characterService.findAll();
        characters.forEach(character -> System.out.println(character.getName()));
    }

}

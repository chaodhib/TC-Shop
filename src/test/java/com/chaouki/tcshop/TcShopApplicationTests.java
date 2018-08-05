package com.chaouki.tcshop;

import com.chaouki.tcshop.dao.ItemTemplateDao;
import com.chaouki.tcshop.entities.ItemTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TcShopApplicationTests {

    @Autowired
    private ItemTemplateDao itemTemplateDao;

	@Test
	public void contextLoads() {
        Optional<ItemTemplate> testItem = itemTemplateDao.findById(25);
        Assert.assertThat(testItem.isPresent(), is(true));
    }

}

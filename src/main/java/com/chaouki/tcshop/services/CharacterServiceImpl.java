package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.CharacterClass;
import com.chaouki.tcshop.entities.Gender;
import com.chaouki.tcshop.entities.Race;
import com.chaouki.tcshop.entities.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterDao characterDao;

    @Override
    public List<Character> findByAccount(Integer account) {
        return characterDao.findByAccount(account);
    }

}

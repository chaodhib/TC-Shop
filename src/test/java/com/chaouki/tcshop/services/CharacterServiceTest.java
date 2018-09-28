package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharacterServiceTest {

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private AccountDao accountDao;

    private AccountService accountService;
    private CharacterService characterService;

    @Before
    public void setUp() {
        accountService = Mockito.mock(AccountService.class);
        characterService = new CharacterServiceImpl(characterDao, accountService);
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Test
    public void onCharacterMessage_noStub() {
        Assert.assertThat(characterDao.count(), is(0L));

        Mockito.when(accountService.findById(1)).thenReturn(Optional.of(accountDao.findById(1).get()));
        final String character_name = "CHARACTER_NAME";
        characterService.onCharacterMessage(1, 1, LocalDateTime.now(), character_name, CharacterClass.CLASS_DRUID, true);

        Assert.assertThat(characterDao.count(), is(1L));
        Character character = characterDao.findAll().get(0);
        Assert.assertThat(character.getName(), is(character_name));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Test
    public void onCharacterMessage_stub() {
        Assert.assertThat(characterDao.count(), is(0L));

        final int accountId = 1;
        final int characterId = 2;
        final String character_name = "CHARACTER_NAME";
        Mockito.when(accountService.findById(accountId)).thenReturn(Optional.empty());
        Mockito.when(accountService.createStubAccount(accountId)).thenReturn(accountDao.findById(1).get());

        characterService.onCharacterMessage(accountId, characterId, LocalDateTime.now(), character_name, CharacterClass.CLASS_DRUID, true);

        Assert.assertThat(characterDao.count(), is(1L));
        Character character = characterDao.findAll().get(0);
        Assert.assertThat(character.getId(), is(characterId));
        Assert.assertThat(character.getAccount().getId(), is(accountId));
        Assert.assertThat(character.getName(), is(character_name));

        // ensure that the creation of a stub was initiated
        Mockito.verify(accountService, Mockito.times(1)).createStubAccount(accountId);
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void onCharacterMessage_update() {
        Character character;
        final int accountId = 3;
        final int characterId = 2;
        final String character_name = "CHARACTER_NAME";

        Assert.assertThat(characterDao.count(), is(4L));
        character = characterDao.findById(characterId).get();
        Assert.assertThat(character.getId(), is(characterId));
        Assert.assertThat(character.getName(), is("NAME_2"));
        Assert.assertThat(character.getCharacterClass(), is(CharacterClass.CLASS_PALADIN));
        Assert.assertThat(character.getAccount().getId(), is(accountId));
        Assert.assertThat(character.isDeleted(), is(false));

        Mockito.when(accountService.findById(accountId)).thenReturn(Optional.of(accountDao.findById(accountId).get()));
        characterService.onCharacterMessage(accountId, characterId, LocalDateTime.now(), character_name, CharacterClass.CLASS_DRUID, false);

        Assert.assertThat(characterDao.count(), is(4L));
        character = characterDao.findById(characterId).get();
        Assert.assertThat(character.getId(), is(characterId));
        Assert.assertThat(character.getName(), is(character_name));
        Assert.assertThat(character.getCharacterClass(), is(CharacterClass.CLASS_DRUID));
        Assert.assertThat(character.getAccount().getId(), is(accountId));
        Assert.assertThat(character.isDeleted(), is(true));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findById() {
        final Optional<Character> optionalCharacter = characterService.findById(1);
        Assert.assertThat(optionalCharacter.isPresent(), is(true));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findById_stub() {
        final Optional<Character> optionalCharacter = characterService.findById(4);
        Assert.assertThat(optionalCharacter.isPresent(), is(false));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findById_deleted() {
        final Optional<Character> optionalCharacter = characterService.findById(3);
        Assert.assertThat(optionalCharacter.isPresent(), is(false));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findByIdIncludeStub() {
        final Optional<Character> optionalCharacter = characterService.findByIdIncludeStub(1);
        Assert.assertThat(optionalCharacter.isPresent(), is(true));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findByIdIncludeStub_stub() {
        final Optional<Character> optionalCharacter = characterService.findByIdIncludeStub(4);
        Assert.assertThat(optionalCharacter.isPresent(), is(true));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findByIdIncludeStub_deleted() {
        final Optional<Character> optionalCharacter = characterService.findByIdIncludeStub(3);
        Assert.assertThat(optionalCharacter.isPresent(), is(false));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void onEquipmentUpdate() {
        Character character;
        character = characterDao.findById(1).get();
        Assert.assertThat(character.getEquipmentUpdateTimestamp(), nullValue());

        final LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        characterService.onEquipmentUpdate(character, timestamp);

        character = characterDao.findById(1).get();
        Assert.assertThat(character.getEquipmentUpdateTimestamp(), is(timestamp));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findActiveCharsByAccount() {
        final int accountId = 3;
        final List<Character> characterList = characterService.findActiveCharsByAccount(accountDao.findById(accountId).get());
        Assert.assertThat(characterList.size(), is(1));
        final Character character = characterList.get(0);
        Assert.assertThat(character.getId(), is(2));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void findActiveCharsByAccount_noResult() {
        final List<Character> characterList = characterService.findActiveCharsByAccount(accountDao.findById(6).get());
        Assert.assertThat(characterList.size(), is(0));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Test
    public void createStubCharacter() {
        Assert.assertThat(characterDao.count(), is(0L));

        final int characterId = 3;
        final int stubAccountId = Integer.MAX_VALUE;
        Mockito.when(accountService.findById(stubAccountId)).thenReturn(Optional.of(accountDao.findById(stubAccountId).get()));
        characterService.createStubCharacter(characterId);

        Assert.assertThat(characterDao.count(), is(1L));
        final Character character = characterDao.findAll().get(0);
        Assert.assertThat(character.getId(), is(characterId));
        Assert.assertThat(character.getName(), is("STUB"));
        Assert.assertThat(character.getAccount().getId(), is(stubAccountId));
        Assert.assertThat(character.isStub(), is(true));
        Assert.assertThat(character.isDeleted(), is(false));
    }
}
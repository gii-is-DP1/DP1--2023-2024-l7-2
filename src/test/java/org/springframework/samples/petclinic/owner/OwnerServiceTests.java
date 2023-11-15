package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.pet.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.user.AuthoritiesService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
//@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class),@ComponentScan.Filter(PasswordEncoder.class)})
@SpringBootTest
//@AutoConfigureTestDatabase
public class OwnerServiceTests {

	private OwnerService ownerService;
	private PetService petService;
	private AuthoritiesService authService;
	
	@Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Autowired
	public OwnerServiceTests(OwnerService ownerService, PetService petService, AuthoritiesService authService) {
		this.ownerService = ownerService;
		this.petService = petService;
		this.authService = authService;
	}

	@Test
	void shouldFindAllOwners() {
		List<Owner> owners = (List<Owner>) this.ownerService.findAll();
		assertEquals(14, owners.size());
	}

	@Test
	void shouldFindOwnersByLastName() {
		Collection<Owner> owners = this.ownerService.findOwnerByLastName("Davis");
		assertEquals(2, owners.size());

		owners = this.ownerService.findOwnerByLastName("Daviss");
		assertThat(owners.isEmpty()).isTrue();
	}

	@Test
	void shouldFindSingleOwnerWithPet() {
		Owner owner = this.ownerService.findOwnerById(1);
		List<Pet> pets = petService.findAllPetsByOwnerId(owner.getId());
		assertThat(owner.getLastName()).startsWith("Franklin");
		assertEquals(1, pets.size());
		assertNotNull(pets.get(0).getType());
		assertEquals("cat", pets.get(0).getType().getName());
	}

	@Test
	void shouldNotFindSingleOwnerWithBadID() {
		assertThrows(ResourceNotFoundException.class, () -> this.ownerService.findOwnerById(100));
	}

//	@Test
//	void shouldFindOwnerByUser() {
//		Owner owner = this.ownerService.findOwnerByUser(2);
//		assertEquals("Franklin", owner.getLastName());
//	}
//	
//	@Test
//	void shouldNotFindOwnerByIncorrectUser() {
//		assertThrows(ResourceNotFoundException.class, () -> this.ownerService.findOwnerByUser(34));
//	}

	@Test
	void shouldFindOptOwnerByUser() {
		Optional<Owner> owner = this.ownerService.optFindOwnerByUser(4);
		assertEquals("Franklin", owner.get().getLastName());
	}

	@Test
	void shouldNotFindOptOwnerByIncorrectUser() {
		assertThat(this.ownerService.optFindOwnerByUser(25)).isEmpty();
	}

	@Test
	@Transactional
	void shouldUpdateOwner() {
		Owner owner = this.ownerService.findOwnerById(1);
		owner.setAddress("Change");
		owner.setLastName("Update");
		ownerService.updateOwner(owner, 1);
		owner = this.ownerService.findOwnerById(1);
		assertEquals("Change", owner.getAddress());
		assertEquals("Update", owner.getLastName());
	}

	@Test
	@Transactional
	void shouldInsertOwner() {
		int initialCount = ((Collection<Owner>) this.ownerService.findAll()).size();

		Owner owner = createOwnerUser();
		assertNotEquals(0, owner.getId().longValue());

		int finalCount = ((Collection<Owner>) this.ownerService.findAll()).size();
		assertEquals(initialCount + 1, finalCount);
	}

	@Test
	@Transactional
	void shouldDeleteOwner() throws DataAccessException, DuplicatedPetNameException {
		Integer firstCount = ((Collection<Owner>) ownerService.findAll()).size();

		Owner owner = createOwnerUser();
		Pet pet = new Pet();
		pet.setName("Sisi");
		pet.setType(petService.findPetTypeByName("dog"));
		pet.setOwner(owner);
		petService.savePet(pet);

		Integer secondCount = ((Collection<Owner>) ownerService.findAll()).size();
		assertEquals(firstCount + 1, secondCount);
		ownerService.deleteOwner(owner.getId());
		Integer lastCount = ((Collection<Owner>) ownerService.findAll()).size();
		assertEquals(firstCount, lastCount);
	}

	private Owner createOwnerUser() {
		Owner owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Apellido");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("444444444");
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("supersecretpassword");
		user.setAuthority(authService.findByAuthority("OWNER"));
		owner.setUser(user);
		return this.ownerService.saveOwner(owner);
	}

	@Test
	@Transactional
	void shouldReturnStatsForAdmin() {
		Map<String, Object> stats = this.ownerService.getOwnersStats();
		assertTrue(stats.containsKey("totalOwners"));
		assertEquals(((Collection<Owner>) ownerService.findAll()).size(), stats.get("totalOwners"));
		assertTrue(stats.containsKey("basicOwners"));
		assertEquals(7, stats.get("basicOwners"));
		assertTrue(stats.containsKey("goldOwners"));
		assertEquals(3, stats.get("goldOwners"));
		assertTrue(stats.containsKey("platinumOwners"));
		assertEquals(4, stats.get("platinumOwners"));
		assertTrue(stats.containsKey("ownersVisits"));
	}

}


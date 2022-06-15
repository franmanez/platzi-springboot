package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;

	private UserPojo userPojo;

	private UserRepository userRepository;

	private UserService userService;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUserInDataBase();
		getInformationJpqlFromUser();

	}

	private void saveUserInDataBase(){
		User user1 = new User("Fran", "francisco.manez@upc.edu", LocalDate.of(1982, 1, 2));
		User user2 = new User("User1", "maria.manez@upc.edu", LocalDate.of(1980, 2, 23));
		User user3 = new User("User2", "dan.manez@upc.edu", LocalDate.of(1975, 3, 12));
		User user4 = new User("User3", "albert.manez@upc.edu", LocalDate.of(1999, 4, 29));
		User user5 = new User("User4", "jordina.manez@upc.edu", LocalDate.of(2001, 5, 11));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5);

		list.stream().forEach(userRepository::save);

	}

	public void getInformationJpqlFromUser(){
		LOGGER.info("Usuario con el método findByUserEmail: " +
				userRepository.findByUserEmail("francisco.manez@upc.edu")
						.orElseThrow(()->new RuntimeException("No se encontró el usario")));

		userRepository.findAndSort("User", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con método sort: " + user));


		userRepository.findByName("Fran").stream().forEach(user -> LOGGER.info("Usuario con queryMethod " + user));

		userRepository.findByEmailAndName("francisco.manez@upc.edu", "Fran").stream().forEach(user -> LOGGER.info("Usuario con queryMethod (email and name) " + user));

		userRepository.findByNameLike("%ser%").stream().forEach(user -> LOGGER.info("findByNameLike: " + user));

		userRepository.findByNameOrEmail(null, "dan.manez@upc.edu").stream().forEach(user -> LOGGER.info("findByNameOrEmail: " + user));

		userRepository.findByBirthDateBetween(LocalDate.of(1979, 1, 1), LocalDate.of(1999, 12, 12)).stream().forEach(user -> LOGGER.info("findByBirthDateBetween: " + user));

		userRepository.findByNameLikeOrderByIdDesc("%ser%").stream().forEach(user -> LOGGER.info("findByNameLikeOrderByIdDesc: " + user));

		userRepository.findByNameContainingOrderByIdDesc("ser").stream().forEach(user -> LOGGER.info("findByNameContainingOrderByIdDesc: " + user));

		LOGGER.info("Usuario a partir del member parameter: " +
				userRepository.getAllByBirthDateAndEmail(LocalDate.of(1980, 2, 23), "maria.manez@upc.edu").orElseThrow(() -> new RuntimeException("usuario no encontrado")));

		saveWithErrorTransactional();

	}

	private void saveWithErrorTransactional(){
		User user1 = new User("Test1", "test1@upc.edu", LocalDate.now());
		User user2 = new User("Test2", "test1@upc.edu", LocalDate.now());
		User user3 = new User("Test3", "test3@upc.edu", LocalDate.now());
		User user4 = new User("Test4", "test4@upc.edu", LocalDate.now());

		List<User> users = Arrays.asList(user1, user2, user3, user4);

		try{
			userService.saveTransactional(users);
		}catch (Exception e){
			LOGGER.error("Excepción capturada");
		}

		userService.getAllUsers().stream().forEach(user -> LOGGER.info("Este es el usuario dentro del método transaccional: " + user));
	}



	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + " - " + userPojo.getAge());
		try{
			int value = 10/0;
			LOGGER.debug("valor" + value);
		}catch (Exception e){
			LOGGER.error("Esto es un error al dividir por 0 - " + e.getMessage());
		}
	}
}

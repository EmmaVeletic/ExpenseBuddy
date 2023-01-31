package com.LadyBugs2.ExpenseBuddy.service;

import com.LadyBugs2.ExpenseBuddy.controller.exception.ItemAlreadyExistsException;
import com.LadyBugs2.ExpenseBuddy.controller.exception.RecordNotFoundException;
import com.LadyBugs2.ExpenseBuddy.models.dto.CategoryDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import com.LadyBugs2.ExpenseBuddy.repository.RecordRepository;
import com.LadyBugs2.ExpenseBuddy.util.DateTimeUtil;
import com.LadyBugs2.ExpenseBuddy.util.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;

	private final RecordRepository recordRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	private final CategoryService categoryService;

	@Autowired
	private final UserMapper userMapper;

	@Validated
	public UserDTO createUser(UserDTO userDTO) throws ParseException {
		if(userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ItemAlreadyExistsException("User is already register with email: " + userDTO.getEmail());
		} else if(userRepository.existsByUsername(userDTO.getUsername())) {
			throw new ItemAlreadyExistsException("User is already register with username: " + userDTO.getUsername());
		}
		User newUser = new User();
		BeanUtils.copyProperties(userDTO, newUser);
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		UserDTO savedUser = mapToDTO(userRepository.save(newUser));
		categoryService.createCategory(new CategoryDTO("INCOME",false, savedUser.getId()));
		categoryService.createCategory(new CategoryDTO("OUTCOME",false, savedUser.getId()));
		return savedUser;
	}

	private UserDTO mapToDTO(User user){
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return (userDTO);
	}

	public UserDTO getDTOById(long id) {
		return mapToDTO(userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found for the id :" + id)));
	}

	public UserDTO getDTOByEmail(String email) {
		return mapToDTO(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user email not found, please try again")));
	}

	@Override
	public User readUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found for the id :" + id));
	}

	@Override
	@Validated
	public User updateUser(UserDTO user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User existingUser = userRepository.findByEmail(auth.getName()).orElseThrow();
		//userMapper.updateCustomerFromDto(user,existingUser);
		existingUser.setUsername(user.getUsername() != null ? user.getUsername() : existingUser.getUsername());
		existingUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : existingUser.getFirstName());
		existingUser.setLastName(user.getLastName() != null ? user.getLastName() : existingUser.getLastName());
		existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
		existingUser.setPhone(user.getPhone() != null ? user.getPhone() : existingUser.getPhone());
		existingUser.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : existingUser.getPassword());
		return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(long id) {
		User existingUser = readUser(id);
		userRepository.delete(existingUser);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public UserDTO getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return getDTOByEmail(auth.getName());
	}

	public boolean checkMonthlyLimit() {
		UserDTO user = getLoggedInUser();
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date lastDayOfMonth = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfMonth = calendar.getTime();
		List<Record> list = recordRepository.findByDateBetweenAndUserId(new java.sql.Date(firstDayOfMonth.getTime()),
																		new java.sql.Date(lastDayOfMonth.getTime()),user.getId());
		double sum = 0;
		for(Record record : list) {
			sum = Double.sum(record.getAmount().doubleValue(),sum);
		}
		if(Double.compare(sum,user.getMonthlyLimit())>=0){
			return true;
		}
		return false;
	}
}


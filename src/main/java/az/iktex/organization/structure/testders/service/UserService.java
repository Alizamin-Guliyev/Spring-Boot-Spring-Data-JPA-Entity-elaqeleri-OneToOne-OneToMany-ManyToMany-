package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Passport;
import az.iktex.organization.structure.testders.entity.User;
import az.iktex.organization.structure.testders.enums.Gender;
import az.iktex.organization.structure.testders.mapper.PassportMapper;
import az.iktex.organization.structure.testders.mapper.UserMapper;
import az.iktex.organization.structure.testders.repository.PassportRepository;
import az.iktex.organization.structure.testders.repository.UserRepository;
import az.iktex.organization.structure.testders.request.PassportRequest;
import az.iktex.organization.structure.testders.request.UserRequest;
import az.iktex.organization.structure.testders.response.PassportResponse;
import az.iktex.organization.structure.testders.response.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PassportRepository passportRepository;
    private final UserMapper userMapper;
    private final PassportMapper passportMapper;

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists: " + request.email());
        }
        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUserWithPassport(id));
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmailWithPassport(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAll(Gender gender) {
        List<User> users = gender == null
                ? userRepository.findAllWithPassport()
                : userRepository.findAllByGenderWithPassport(gender);
        return userMapper.toResponseList(users);
    }

    public List<UserResponse> getWithoutPassport() {
        return userMapper.toResponseList(userRepository.findByPassportIsNull());
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findUserWithPassport(id);
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new IllegalStateException("Email already exists: " + request.email());
        }
        userMapper.updateEntity(request, user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findUserWithPassport(id));
    }

    @Transactional
    public PassportResponse addPassport(Long userId, PassportRequest request) {
        User user = findUserWithPassport(userId);
        if (user.getPassport() != null) {
            throw new IllegalStateException("User already has a passport: " + userId);
        }
        if (passportRepository.existsBySerialNumber(request.serialNumber())) {
            throw new IllegalStateException("Passport serial number already exists: " + request.serialNumber());
        }
        Passport passport = passportMapper.toEntity(request);
        user.setPassport(passport);
        return passportMapper.toResponse(passportRepository.save(passport));
    }

    public PassportResponse getPassport(Long userId) {
        return passportMapper.toResponse(findPassportOfUser(findUserWithPassport(userId)));
    }

    @Transactional
    public void deletePassport(Long userId) {
        User user = findUserWithPassport(userId);
        findPassportOfUser(user);
        user.setPassport(null);
    }

    private User findUserWithPassport(Long id) {
        return userRepository.findByIdWithPassport(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    private Passport findPassportOfUser(User user) {
        if (user.getPassport() == null) {
            throw new EntityNotFoundException("Passport not found for user: " + user.getId());
        }
        return user.getPassport();
    }
}

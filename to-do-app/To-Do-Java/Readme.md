# To-Do App - Java

Para executar o projeto é necessário a criação de um arquivo .env na raiz do projeto contendo os valores, ou criação das variáveis de ambiente a seguir

- MAIL_FROM - email da aplicação que enviará os emails de recuperação de senha ou ativação de usuário
- MAIL_PASSWORD - senha do email
- MAIL_SMTP_HOST - host do email (smtp.gmail.com por exemplo)
- MAIL_SMTP_PORT - porta de acesso ao host de email

As tabelas de banco de dados não são criadas ou alteradas pela aplicação, mas foi disponibilizado script criado para definição das tabelas.

![SQL script for the project](./assets/database_sql.jpg "SQL script")

A aplicação foi criada utilizando framework Spring e Spring Boot, além outras dependências como mostrado abaixo:

```
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>2.10.1</version>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>
	</dependencies>
```

A aplicação foi separada em camadas, sendo a primeira, que receberá as requisições do usuário, a camada de controller, como a exemplo da classe de controller para operações de autenticação, registro de usuário e recuperação de senha.
as exceções são tratadas em um tipo específico criado (BusinessException) que contemplará as respostas com erro, os retornos são feitos com DTO (não entidades) e a entrada de dados com forms (simples classes java, semelhantes aos DTOs)

```
@RestController()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO login(@RequestBody LoginForm form) {
        return authService.login(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null);
    }


    @PostMapping(value = "/api/auth/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void recoverPassword(@RequestBody RecoverPasswordForm form) {
        authService.recoverPassword(form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null);
    }


    @PostMapping(value = "/api/auth/passwordreset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO passwordReset(@RequestBody PasswordResetForm form) {
        return authService.resetPasswordByCode(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null,
                form.getNewPasswordCode() != null ? form.getNewPasswordCode().trim() : null);
    }


    @PostMapping(value = "/api/auth/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUser(@RequestBody UserRegistrationForm form) {
        authService.registerUser(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null,
                form.getFirstName() != null ? form.getFirstName().trim() : null,
                form.getLastName() != null ? form.getLastName().trim() : null);
    }


    @PostMapping(value = "/api/auth/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO activateUser(@RequestBody UserActivationForm form) {
        return authService.activateUser(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getActivationCode() != null ? form.getActivationCode().trim() : null);
    }


    @PostMapping(value = "/api/auth/useractivationrequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void requestUserActivation(@RequestBody RequestUserActivationForm form) {
        authService.requestUserActivation(form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessExceptions(BusinessException e) {
        return e.getResponseEntity();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(MessageUtil.getErrorMessage()));
    }

}
```

Essa camada de aplicação contém uma referência, injetada pelo Spring, de um serviço, aqui temos as interfaces.

```
public interface AuthService {

    public UserDTO login(String email, String password) throws BusinessException;

    public void recoverPassword(String email) throws BusinessException;

    public UserDTO resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException;

    public void registerUser(String email, String password, String name, String lastName) throws BusinessException;

    public UserDTO activateUser(String email, String activationCode) throws BusinessException;

    public void requestUserActivation(String email) throws BusinessException;

}
```

E as implementações, aqui temos a lógica de negócio e acionamento da camada de repositório e outros serviços, como para envio de emails.

```
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final UserDTOMapper userDTOMapper;
    private final UtilService utilService;
    private final TaskListService taskListService;

    public UserDTO login(String email, String password) throws BusinessException {
        if (!utilService.isMail(email) || password.length() < User.MIN_LENGTH_PASSWORD) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() ||
                !comparePasswordWithHash(userOptional.get().getPassword(), password)) {
            throw new BusinessException(MessageUtil.getErrorMessageLogin(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        if (userOptional.get().getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        return userDTOMapper.apply(userOptional.get());
    }

    public void recoverPassword(String email) throws BusinessException {
        if (!utilService.isMail(email)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        if (user.getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String newPasswordCode = utilService.generateRandomString();
        user.setNewPasswordCode(newPasswordCode);
        mailService.sendNewPasswordEmail(user.getEmail(), newPasswordCode);
        userRepository.save(user);
    }

    public UserDTO resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException {
        if (!validateResetPasswordInput(email, newPassword, newPasswordCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        if (!user.getNewPasswordCode().equals(newPasswordCode.trim())) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        user.setNewPasswordCode(null);
        user.setUserStatus(User.STATUS_ACTIVE);
        user.setActivationCode(null);
        user.setPassword(utilService.generateHashString(newPassword.trim()));
        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

    public void registerUser(String email, String password, String name, String lastName) throws BusinessException {
        if (!validateNewUserInput(email, password, name)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new BusinessException(MessageUtil.getErrorMessageEmailAlreadyExists(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = new User(null,
                name.trim(),
                lastName != null ? lastName.trim() : null,
                email.trim(),
                utilService.generateHashString(password.trim()),
                utilService.generateRandomString(),
                null,
                User.STATUS_INACTIVE
        );
        User newUser = userRepository.save(user);
        taskListService.createDefaultListForNewUser(newUser.getId());
        mailService.sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());
    }

    public UserDTO activateUser(String email, String activationCode) throws BusinessException {
        if (!utilService.isMail(email) || activationCode == null || activationCode.trim().length() != User.LENGTH_ACTIVATION_CODE) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email.trim());
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        if (!user.getActivationCode().equals(activationCode.trim())) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        user.setActivationCode(null);
        user.setUserStatus(User.STATUS_ACTIVE);
        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

    public void requestUserActivation(String email) throws BusinessException {
        if (!utilService.isMail(email)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email.trim());
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        user.setActivationCode(utilService.generateRandomString());
        userRepository.save(user);
        mailService.sendActivationEmail(user.getEmail(), user.getActivationCode());
    }

    private boolean comparePasswordWithHash(String hash, String password) {
        return hash.equals(utilService.generateHashString(password));
    }

    private boolean validateResetPasswordInput(String email, String newPassword, String newPasswordCode) {
        return utilService.isMail(email)
                && newPasswordCode != null
                && newPassword != null
                && newPasswordCode.trim().length() >= User.LENGTH_NEW_PASSWORD_CODE
                && newPassword.trim().length() >= User.MIN_LENGTH_PASSWORD;
    }

    private boolean validateNewUserInput(String email, String password, String name) {
        return utilService.isMail(email)
                && password != null
                && password.trim().length() >= User.MIN_LENGTH_PASSWORD
                && name != null
                && name.trim().length() > 0;
    }

}
```

Por fim temos a camada de acesso aos dados, aqui foi implementada utilizando SpringDataJPA.

```
public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.firstName = :name, u.lastName = :lastName WHERE u.id = :id")
    public void updateUserData(@Param("id") Integer id, @Param("name") String name, @Param("lastName") String lastName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.password = :password WHERE u.id = :id")
    public void updateUserPassword(@Param("id") Integer id, @Param("password") String password);

}
```


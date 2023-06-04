package com.cutegyuseok.freetalk.auth.service.impl;

import com.cutegyuseok.freetalk.auth.dto.MailDTO;
import com.cutegyuseok.freetalk.auth.dto.TokenDTO;
import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.enumType.UserStatus;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.global.jwt.JwtProvider;
import com.cutegyuseok.freetalk.global.redis.RedisTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisTemplateRepository redisTemplateRepository;
    private final JavaMailSender mailSender;
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public ResponseEntity<?> signup(UserDTO.UserSignupReqDTO reqDTO) {
        if (userRepository.existsByEmail(reqDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!reqDTO.getEmail().matches(EMAIL_PATTERN)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            String encodingPassword = encodingPassword(reqDTO.getPassword());
            User user = User.builder()
                    .email(reqDTO.getEmail())
                    .name(reqDTO.getName())
                    .password(encodingPassword)
                    .phone(reqDTO.getPhone())
                    .birthday(reqDTO.getBirthday())
                    .role(UserRole.ROLE_NOT_AUTHORIZED)
                    .nickName(reqDTO.getName())
                    .status(UserStatus.NOT_AUTHORIZED)
                    .build();
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> emailConfirmation(UserDTO.UserAccessDTO dto, String codeReq) {
        try {
            String code = redisTemplateRepository.getData(dto.getEmail());
            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(NoSuchElementException::new);
            if (code.equals(codeReq.toUpperCase())) {
                user.changeUserStatus(UserStatus.AVAILABLE);
                user.changeUserRole(UserRole.ROLE_USER);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> emailConfirmationCodeRequest(UserDTO.UserAccessDTO dto) {
        try {
            String code = makeCoe();
            redisTemplateRepository.setDataExpireByMinute(dto.getEmail(), code, 5L);
            MailDTO mailDTO = createMail(code, dto.getEmail());
            sendMail(mailDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> login(UserDTO.LoginReqDTO loginReqDTO) {
        try {
            User user = userRepository.findByEmail(loginReqDTO.getUserEmail())
                    .orElseThrow(NoSuchElementException::new);
            if (user.getStatus().equals(UserStatus.BANNED)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (user.getStatus().equals(UserStatus.DORMANT) || user.getStatus().equals(UserStatus.WITHDRAWAL)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            passwordMustBeSame(loginReqDTO.getUserPassword(), user.getPassword());
            TokenDTO tokenDTO = jwtProvider.makeJwtToken(user);
            redisTemplateRepository.setDataExpire(tokenDTO.getRefreshToken(), user.getEmail(), jwtProvider.getExpiration(tokenDTO.getRefreshToken()));
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUserProfile(UserDTO.UserAccessDTO userAccessDTO, UserDTO.ProfileUpdateReqDTO dto) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            user.changeUserProfile(dto.getNickName(), dto.getProfileImage(), dto.getSelfIntroduction());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> checkUserInfo(UserDTO.UserAccessDTO userAccessDTO) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            return new ResponseEntity<>(new UserDTO.ProfileCheckReqDTO(user), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public String makeCoe() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String pwd = "";

        int idx = 0;
        for (int i = 0; i < 6; i++) {
            idx = (int) (charSet.length * Math.random());
            pwd += charSet[idx];
        }
        return pwd;
    }

    private static final String title = "FreeTalk 이메일 인증 안내 이메일입니다.";
    private static final String message = "안녕하세요. FreeTalk 인증 안내 메일입니다. "
            + "\n" + "인증 코드는 아래와 같습니다.5분 이내에 입력해 주세요." + "\n";
    private static final String fromAddress = "FreeTalk";

    public MailDTO createMail(String code, String userEmail) {

        MailDTO mailDto = MailDTO.builder()
                .toAddress(userEmail)
                .title(title)
                .message(message + code)
                .fromAddress(fromAddress)
                .build();
        return mailDto;
    }

    public void sendMail(MailDTO mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDto.getToAddress());
        mailMessage.setSubject(mailDto.getTitle());
        mailMessage.setText(mailDto.getMessage());
        mailMessage.setFrom(mailDto.getFromAddress());
        mailMessage.setReplyTo(mailDto.getFromAddress());
        mailSender.send(mailMessage);
    }

    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void passwordMustBeSame(String requestPassword, String password) {
        if (!passwordEncoder.matches(requestPassword, password)) {
            throw new IllegalArgumentException();
        }
    }
}

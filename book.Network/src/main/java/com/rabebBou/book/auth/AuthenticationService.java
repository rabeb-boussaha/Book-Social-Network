package com.rabebBou.book.auth;

import com.rabebBou.book.email.EmailService;
import com.rabebBou.book.email.EmailTemplateName;
import com.rabebBou.book.role.RoleRepository;
import com.rabebBou.book.security.JwtService;
import com.rabebBou.book.user.Token;
import com.rabebBou.book.user.TokenRepository;
import com.rabebBou.book.user.User;
import com.rabebBou.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private  final EmailService emailService;
    private  final JwtService jwtService;

    private  final AuthenticationManager authenticationManager;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistractionRequest request) throws MessagingException {
         //few steps de ce code : need to assign it a role par default : c a d  the default role of user will be just use
        // so we need to fetch the user the role name and assign it to the user
        // next : create a user  object and then save it
        // finally :  send a validation email ; we need to implement an email sender service and create a  template or an email
     var userRole = roleRepository.findByName("USER")
             //todo-better execption handling
             .orElseThrow(()-> new IllegalStateException("ROLE USER  WAS NOT INI"));
     var user = User.builder()
             .firstname(request.getFirstname())
             .lastname(request.getLastname())
             .email(request.getEmail())
             .password(passwordEncoder.encode(request.getPassword()))
             .accountLocked(false)
             .enabled(false)
             .roles(List.of(userRole))
             .build();
     userRepository.save(user);
     sendValidationEamil(user);


    }

    private void sendValidationEamil(User user) throws MessagingException {
        var newToken = generateAndActiveToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                (String) newToken,
                "Account activation"
        );
    }

    private Object generateAndActiveToken(User user) {
        //generate a token
        String generatedToken = generateAndActiveCode(6);
        // cree un object de token avec les élèments néssesaire

        var token = Token.builder() //Utilise le design pattern Builder pour créer l' objet Toke
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();  //Construit l'objet Token.
        // save token dans tokenRepository
        tokenRepository.save(token);

                return generatedToken;
    }
//Cette méthode génère un code aléatoire de la longueur spécifiée.
    private String generateAndActiveCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder(); //Un StringBuilder pour construire le code aléatoire.
        SecureRandom secureRandom= new SecureRandom(); //: Utilise SecureRandom pour générer des nombres aléatoires de manière sécurisée.
        for (int i =0; i < length; i++){
            int randomIndex =  secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));


        }
        return  codeBuilder.toString();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
   var auth = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
                   request.getEmail(),
                   request.getPassword()
           )
   );
   var claims = new HashMap<String ,Object>();
   var user = ((User)auth.getPrincipal());
   claims.put("fullName", user.fullName());
   var jwtToken = jwtService.generateToken(claims, user);


           return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                //todo execption has to be defined
                // is token expired comment je faire
                .orElseThrow(()-> new RuntimeException("Invalide token "));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEamil(savedToken.getUser());
            throw new RuntimeException(("Activation token has expired , A new token has been sent to same email adresse "));

        }
        // si token is not expired
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()->new IllegalStateException("User not  found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}

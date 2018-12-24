package calendaracademic.controllers;

import calendaracademic.dto.AuthDTO;
import calendaracademic.model.User;
import calendaracademic.response.Login;
import calendaracademic.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import calendaracademic.dao.LoginDAO;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginDAO loginDAO;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> auth(@RequestBody AuthDTO auth) {
        Boolean correctCredentials = loginDAO.get(auth.getEmail(),auth.getPassword());
        if (correctCredentials) {
            Login jwtUser = new Login(auth.getEmail());
            return ResponseEntity.ok(jwtService.getToken(jwtUser));
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

}

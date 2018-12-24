package calendaracademic.controllers;

import calendaracademic.dto.AuthDTO;
import calendaracademic.dto.ChangeDTO;
import calendaracademic.model.User;
import calendaracademic.response.Details;
import calendaracademic.response.Login;
import calendaracademic.services.JwtFilter;
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

    @PostMapping(value = "/service/firstLogin")
    public ResponseEntity<?> firstLogging(@RequestBody ChangeDTO change, HttpServletRequest request)
    {
        if (loginDAO.setPassword (request,change.getPassword()))
            return ResponseEntity.ok("OK");
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/service/logout")
    public ResponseEntity<?> logout (HttpServletRequest request)
    {
        JwtFilter.logout(loginDAO.logout(request));
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/service/details")
    public ResponseEntity<?> getdescription (HttpServletRequest request)
    {
        Details d = loginDAO.getDetails(request);
        if (d != null)
        {
            return ResponseEntity.ok(d);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}

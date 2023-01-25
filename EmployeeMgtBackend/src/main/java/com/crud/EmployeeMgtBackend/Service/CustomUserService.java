package com.crud.EmployeeMgtBackend.Service;

import com.crud.EmployeeMgtBackend.criteriaQuery.EmployeeRepo;
import com.crud.EmployeeMgtBackend.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = this.employeeRepo.findByEmail(username);
        return employee;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user =
//                (User) this.employeeRepo.findByEmailId(username)
//                        .orElseThrow(() -> {
//                            return new ResourceNotFoundException("User","emailId:"+username, 0);
//                        });
//        return user;
//    }
}

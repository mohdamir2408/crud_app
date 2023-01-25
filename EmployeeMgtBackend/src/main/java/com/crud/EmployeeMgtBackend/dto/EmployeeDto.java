package com.crud.EmployeeMgtBackend.dto;

import com.crud.EmployeeMgtBackend.entities.Address;
import com.crud.EmployeeMgtBackend.entities.DS;
import com.crud.EmployeeMgtBackend.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDto {

    private int id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String mobileNo;
    private List<Address> addressList=new ArrayList<>();
    private DS ds;
    private Set<Role> roles=new HashSet<>();
}

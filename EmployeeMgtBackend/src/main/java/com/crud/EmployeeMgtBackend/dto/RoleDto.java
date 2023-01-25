package com.crud.EmployeeMgtBackend.dto;

import com.crud.EmployeeMgtBackend.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Integer id;
    private String roleName;
}
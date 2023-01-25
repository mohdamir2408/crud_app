package com.crud.EmployeeMgtBackend.Service;

import com.crud.EmployeeMgtBackend.dto.EmployeeDto;
import com.crud.EmployeeMgtBackend.entities.Employee;
import com.crud.EmployeeMgtBackend.entities.Pagination;
import com.crud.EmployeeMgtBackend.entities.ResponsePage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    public EmployeeDto saveEmployee(EmployeeDto employeeDto);
    public List<EmployeeDto> findAll();

    public EmployeeDto findById(int id);
    List<EmployeeDto> findByFirstName(String firstName);
    void delete(Integer id);
    EmployeeDto update(EmployeeDto employeeDetails);

//    List<EmployeeDto> findAllByPage(Pagination pagination);

    ResponsePage paging(Pagination pagination);

    //Service for Sorting anf searching,sorting & Paging...
//    @Override
//    public List<EmployeeDto> findAllByPage(Pagination pagination) {
//        List<Employee> emps = employeeRepo.findAllByPage(pagination);
//        List<EmployeeDto> empDtos = new ArrayList<>();
//        for(Employee emp: emps) empDtos.add(this.convertToDTO(emp));
//        return empDtos;
//    }
    Employee loadUserByName(String username) throws UsernameNotFoundException;
}

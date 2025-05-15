package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Employee findById(int id);

    @Modifying
    @Query(value = "UPDATE Employee e SET e.salary =salary*1.10 where e.id=?1", nativeQuery = true)
    void updateSalary(@Param("id") Integer id);
}

//SELECT name, COUNT(salary) from employee group by name;
//
//    SELECT name, count(*) FROM employee
//    group by name
//    having count(*)>1;
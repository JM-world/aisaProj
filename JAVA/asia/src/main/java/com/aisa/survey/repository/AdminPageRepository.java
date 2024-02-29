package com.aisa.survey.repository;

import com.aisa.survey.entity.AdminPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminPageRepository extends JpaRepository<AdminPage, String> {

    Optional<AdminPage> findByDate(String date);

    List<AdminPage> findAllByDateBetween(String date1, String date2);
}

package com.cos.blog.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;


public interface BoardRepository extends JpaRepository<Board, Integer>{
	
}

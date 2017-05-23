package com.fr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fr.commons.enumeration.SportListEnum;
import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.entities.RoleEntity;
import com.fr.entities.SportEntity;
import com.fr.repositories.RoleRepository;
import com.fr.repositories.SportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert Roles and sports in database.
 *
 * Created by djenanewail on 12/8/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InitMainData
{
	
	@Autowired
	private SportRepository sportRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final int sportListSize = SportListEnum.values().length;
	private static final int roleListSize = UserRoleTypeEnum.values().length;
	
	@Test
	public void insertSports() throws JsonProcessingException
	{
		
		final List<SportEntity> databaseSportList = this.sportRepository.findAll();
		if (!databaseSportList.isEmpty() || databaseSportList.size() != sportListSize) {
			// clear list
			
			final List<SportEntity> newSports = new ArrayList<>();
			final SportListEnum[] spList = SportListEnum.values().clone();

			/*
			 * loop the database to detect the new sports if no sports has been
			 * found in db, insert all
			 */
			if (databaseSportList.size() > 0) {
				/*
				 * check new one
				 */
				for (int i = 0; i < sportListSize; i++) {
					boolean exist = false;
					final String definedSport = spList[i].getSportType();
					
					for (final SportEntity sportEntity : databaseSportList) {
						
						if (sportEntity.getName().equals(definedSport)) {
							exist = true;
							
						}
						
					}
					if (!exist) {
						final SportEntity s = new SportEntity(definedSport);
						newSports.add(s);
					}
				}
				
			} else {
				/*
				 * insert all
				 */
				for (int i = 0; i < sportListSize; i++) {
					final String definedSport = spList[i].getSportType();
					final SportEntity s = new SportEntity(definedSport);
					newSports.add(s);
				}
			}
			
			/*
				 * Save all new sports
				 */
			this.sportRepository.save(newSports);
		}
		
	}
	
	@Test
	public void insertRoles() throws JsonProcessingException
	{
		
		final List<RoleEntity> databaseProfileList = this.roleRepository.findAll();
		if (!databaseProfileList.isEmpty() || databaseProfileList.size() != roleListSize) {
			// clear list
			
			final List<RoleEntity> newRoles = new ArrayList<>();
			final UserRoleTypeEnum[] uroleList = UserRoleTypeEnum.values().clone();

			/*
			 * loop the database to detect the new sports if no sports has been
			 * found in db, insert all
			 */
			if (databaseProfileList.size() > 0) {
				/*
				 * check new one
				 */
				for (int i = 0; i < roleListSize; i++) {
					boolean exist = false;
					final UserRoleTypeEnum definedRole = uroleList[i];
					
					for (final RoleEntity sport : databaseProfileList) {
						
						if (sport.getName().equals(definedRole)) {
							exist = true;
							
						}
						
					}
					if (!exist) {
						final RoleEntity s = new RoleEntity(definedRole);
						newRoles.add(s);
					}
				}
				
			} else {
				/*
				 * insert all
				 */
				for (int i = 0; i < roleListSize; i++) {
					final UserRoleTypeEnum definedUserRole = uroleList[i];
					final RoleEntity ur = new RoleEntity(definedUserRole);
					newRoles.add(ur);
				}
			}
			
			/*
				 * Save all new sports
				 */
			this.roleRepository.save(newRoles);
		}
	}
	
}

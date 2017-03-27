package com.fr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fr.entities.SportEntity;
import com.fr.models.SportList;
import com.fr.models.UserRoleType;
import com.fr.entities.RoleEntity;
import com.fr.repositories.RoleRepository;
import com.fr.repositories.SportRepository;
import org.apache.log4j.Logger;
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
@SpringBootTest
@org.springframework.boot.test.IntegrationTest("server.port:0")
public class InitMainData {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static Logger logger = Logger.getLogger(InitMainData.class);

    private static final int sportListSize = SportList.values().length;
    private static final int roleListSize = UserRoleType.values().length;

    @Test
    public void insertSports() throws JsonProcessingException {

        List<SportEntity> databaseSportList = sportRepository.findAll();
        if (!databaseSportList.isEmpty() || databaseSportList.size() != sportListSize) {
            // clear list

            List<SportEntity> newSports = new ArrayList<>();
            SportList[] spList = SportList.values().clone();

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
                    String definedSport = spList[i].getSportType();

                    for (SportEntity sportEntity : databaseSportList) {

                        if (sportEntity.getName().equals(definedSport)) {
                            exist = true;

                        }

                    }
                    if (!exist) {
                        SportEntity s = new SportEntity(definedSport);
                        newSports.add(s);
                    }
                }

            } else {
                /*
                 * insert all
				 */
                for (int i = 0; i < sportListSize; i++) {
                    String definedSport = spList[i].getSportType();
                    SportEntity s = new SportEntity(definedSport);
                    newSports.add(s);
                }
            }

            boolean isSaved = false;

            logger.info("New sports size " + newSports.size());

            for (int i = 0; i < newSports.size(); i++) {
                /*
                 * Save all new sports
				 */
                try {
                    sportRepository.save(newSports.get(i));

                    isSaved = true;

                } catch (Exception e) {
                    isSaved = false;

                }

            }

            if (isSaved)
                logger.info("All sports have been saved !!");
            else
                logger.error("At least one SportDTO is not saved !!");
        } else

        {
            logger.info("SportEntity list in database is up to date !!");
        }

    }

    @Test
    public void insertRoles() throws JsonProcessingException {

        List<RoleEntity> databaseProfileList = roleRepository.findAll();
        if (!databaseProfileList.isEmpty() || databaseProfileList.size() != roleListSize) {
            // clear list

            List<RoleEntity> newRoles = new ArrayList<>();
            UserRoleType[] uroleList = UserRoleType.values().clone();

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
                    String definedRole = uroleList[i].getUserProfileType();

                    for (RoleEntity sport : databaseProfileList) {

                        if (sport.getName().equals(definedRole)) {
                            exist = true;

                        }

                    }
                    if (!exist) {
                        RoleEntity s = new RoleEntity(definedRole);
                        newRoles.add(s);
                    }
                }

            } else {
				/*
				 * insert all
				 */
                for (int i = 0; i < roleListSize; i++) {
                    String definedUserRole = uroleList[i].getUserProfileType();
                    RoleEntity ur = new RoleEntity(definedUserRole);
                    newRoles.add(ur);
                }
            }

            boolean isSaved = false;

            logger.info("New role size " + newRoles.size());
            for (int i = 0; i < newRoles.size(); i++) {
				/*
				 * Save all new sports
				 */
                try {
                    roleRepository.save(newRoles.get(i));
                    isSaved = true;

                } catch (Exception e) {

                    isSaved = false;

                }
            }

            if (isSaved)
                logger.info("All roles have been saved !!");
            else
                logger.error("At least one role is not saved !!");
        } else

        {
            logger.info("Role list in database is up to date !!");
        }
    }

}

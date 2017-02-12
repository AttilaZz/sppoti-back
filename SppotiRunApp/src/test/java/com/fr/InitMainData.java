package com.fr; /**
 * Created by djenanewail on 12/8/16.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fr.models.SportList;
import com.fr.models.UserRoleType;
import com.fr.entities.Roles;
import com.fr.entities.Sport;
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

        List<Sport> databaseSportList = sportRepository.findAll();
        if (!databaseSportList.isEmpty() || databaseSportList.size() != sportListSize) {
            // clear list

            List<Sport> newSports = new ArrayList<>();
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

                    for (Sport sport : databaseSportList) {

                        if (sport.getName().equals(definedSport)) {
                            exist = true;

                        }

                    }
                    if (!exist) {
                        Sport s = new Sport(definedSport);
                        newSports.add(s);
                    }
                }

            } else {
                /*
                 * insert all
				 */
                for (int i = 0; i < sportListSize; i++) {
                    String definedSport = spList[i].getSportType();
                    Sport s = new Sport(definedSport);
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
                logger.error("At least one SportModelDTO is not saved !!");
        } else

        {
            logger.info("Sport list in database is up to date !!");
        }

    }

    @Test
    public void insertRoles() throws JsonProcessingException {

        List<Roles> databaseProfileList = roleRepository.findAll();
        if (!databaseProfileList.isEmpty() || databaseProfileList.size() != roleListSize) {
            // clear list

            List<Roles> newRoles = new ArrayList<>();
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

                    for (Roles sport : databaseProfileList) {

                        if (sport.getName().equals(definedRole)) {
                            exist = true;

                        }

                    }
                    if (!exist) {
                        Roles s = new Roles(definedRole);
                        newRoles.add(s);
                    }
                }

            } else {
				/*
				 * insert all
				 */
                for (int i = 0; i < roleListSize; i++) {
                    String definedUserRole = uroleList[i].getUserProfileType();
                    Roles ur = new Roles(definedUserRole);
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

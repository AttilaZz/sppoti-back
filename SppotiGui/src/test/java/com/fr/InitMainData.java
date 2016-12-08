package com.fr; /**
 * Created by djenanewail on 12/8/16.
 */

import java.util.ArrayList;
import java.util.List;

import com.fr.RepositoriesService.ProfileDaoService;
import com.fr.RepositoriesService.SportDaoService;
import com.fr.configApp.AppWebSocketConfig;
import com.fr.entities.Sport;
import com.fr.entities.UserRoles;
import com.fr.models.SportList;
import com.fr.models.UserRoleType;
import com.fr.security.SecurityConfiguration;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.boot.test.IntegrationTest("server.port:0")
public class InitMainData {

    @Autowired
    private SportDaoService sportDaoService;

    @Autowired
    private ProfileDaoService profileDaoService;

    private static Logger logger = Logger.getLogger(InitMainData.class);

    private static final int sportListSize = SportList.values().length;
    private static final int roleListSize = UserRoleType.values().length;

    @Test
    public void insertSports() throws JsonProcessingException {

        List<Sport> databaseSportList = sportDaoService.getAll();
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
                if (sportDaoService.saveOrUpdate(newSports.get(i))) {
                    isSaved = true;
                } else {
                    isSaved = false;
                }
            }

            if (isSaved)
                logger.info("All sports have been saved !!");
            else
                logger.error("At least one sport is not saved !!");
        } else

        {
            logger.info("Sport list in database is up to date !!");
        }

    }

    @Test
    public void insertRoles() throws JsonProcessingException {

        List<UserRoles> databaseProfileList = profileDaoService.getAll();
        if (!databaseProfileList.isEmpty() || databaseProfileList.size() != roleListSize) {
            // clear list

            List<UserRoles> newRoles = new ArrayList<>();
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

                    for (UserRoles sport : databaseProfileList) {

                        if (sport.getName().equals(definedRole)) {
                            exist = true;

                        }

                    }
                    if (!exist) {
                        UserRoles s = new UserRoles(definedRole);
                        newRoles.add(s);
                    }
                }

            } else {
				/*
				 * insert all
				 */
                for (int i = 0; i < roleListSize; i++) {
                    String definedUserRole = uroleList[i].getUserProfileType();
                    UserRoles ur = new UserRoles(definedUserRole);
                    newRoles.add(ur);
                }
            }

            boolean isSaved = false;

            logger.info("New role size " + newRoles.size());
            for (int i = 0; i < newRoles.size(); i++) {
				/*
				 * Save all new sports
				 */
                if (profileDaoService.saveOrUpdate(newRoles.get(i))) {
                    isSaved = true;
                } else {
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

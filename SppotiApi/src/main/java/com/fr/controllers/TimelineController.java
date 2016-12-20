/**
 *
 */
package com.fr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fr.aop.TraceAuthentification;
import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Jun 21, 2016
 */

@RestController
@RequestMapping("/actu")
public class TimelineController {


}

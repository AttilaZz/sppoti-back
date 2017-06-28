package com.fr.api;

import com.fr.versionning.ApiVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Add class description.
 *
 * Created by wdjenane on 28/06/2017.
 */

@RestController
@RequestMapping("/version")
@ApiVersion("1")
public class VersionController {

    @GetMapping
    ResponseEntity<Void> initTokens() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

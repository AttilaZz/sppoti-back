package com.fr.api.team.member;

import com.fr.versionning.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 5/14/17.
 */

@RestController
@RequestMapping("/team/{teamId}/member")
@ApiVersion("1")
public class MemberAddController
{
}

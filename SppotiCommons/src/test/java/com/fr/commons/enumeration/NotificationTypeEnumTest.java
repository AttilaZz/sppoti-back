package com.fr.commons.enumeration;

import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by djenanewail on 11/25/17.
 */
@RunWith(SpringRunner.class)
public class NotificationTypeEnumTest
{
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isTeamNotification_than_return_true() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isTeamNotification(), is(true));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isTeamNotification_than_return_true() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isTeamNotification(), is(true));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isTeamNotification_than_return_true() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isTeamNotification(), is(true));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isSppotiNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isChallengeNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isScoreNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isRatingNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isFriendNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isCommentNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_REFUSED_YOUR_TEAM_INVITATION_when_isPostNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isSppotiNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isChallengeNotification_than_return_false()
	{
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isScoreNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isRatingNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isFriendNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTEDD_YOUR_TEAM_INVITATION_when_isCommentNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_ACCEPTED_YOUR_TEAM_INVITATION_when_isPostNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isSppotiNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isChallengeNotification_than_return_false()
	{
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isScoreNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isRatingNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isFriendNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isCommentNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
	
	@Test
	public void given_X_INVITED_YOU_TO_JOIN_HIS_TEAM_when_isPostNotification_than_return_false() {
		assertThat(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM.isSppotiNotification(), is(false));
	}
}

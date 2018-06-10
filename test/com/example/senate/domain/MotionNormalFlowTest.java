package com.example.senate.domain;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.senate.enums.MotionStatus;
import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyExistsException;
import com.example.senate.exception.AlreadyVotedException;
import com.example.senate.exception.VotingNotOpenException;

public class MotionNormalFlowTest {

	static Senate senate = new Senate();
	static Motion motion = new Motion(10, "test motion");

	@BeforeClass
	public static void setupSenate() throws AlreadyExistsException {
		senate.addMotion(motion);
		for (int i = 0; i < 100; i++) {
			senate.addSenator(new Senator(i + ""));
		}
		senate.addVicePrecident("VP");
	}

	@Test(expected = VotingNotOpenException.class)
	public void voteBeforeMotionIsOpenTest() throws Exception {
		Senator voter = senate.getSenator(1);
		senate.getMotion(motion).vote(voter, Vote.YES);
	}

	@Test(expected = AlreadyVotedException.class)
	public void alreadyVotedTest() throws Exception {
		senate.openVoting(motion);
		Senator voter = senate.getSenator(1);
		senate.getMotion(motion).vote(voter, Vote.YES);
		senate.getMotion(motion).vote(voter, Vote.YES);
	}

	@Test
	public void closeVotingPassedTest() throws Exception {
		Senator voter1 = senate.getSenator(1);
		Senator voter2 = senate.getSenator(2);
		Senator voter3 = senate.getSenator(3);
		senate.openVoting(motion);
		senate.getMotion(motion).vote(voter1, Vote.YES);
		senate.getMotion(motion).vote(voter2, Vote.YES);
		senate.getMotion(motion).vote(voter3, Vote.NO);
		VoteResult result = senate.getMotion(motion).getCurrentStatus();
		assertEquals("Motion is not open for voting", MotionStatus.OPEN, motion.getStatus());
		assertEquals("Motion is not in progress", MotionStatus.IN_PROGRESS, result.getResult());
		assertEquals("Number of votes for are not correct", new Integer(2), result.getVotesFor());
		assertEquals("Number of votes against are not correct", new Integer(1), result.getVotesAgainst());

		Thread.sleep(61000);
		result = motion.closeForVoting();
		assertEquals("Motion status is not set to passed", MotionStatus.PASSED, motion.getStatus());
		assertEquals("Motion status is not set to passed", MotionStatus.PASSED, result.getResult());
		assertEquals("Number of votes for are not correct", new Integer(2), result.getVotesFor());
		assertEquals("Number of votes against are not correct", new Integer(1), result.getVotesAgainst());
		assertEquals("Start time is not correct not correct", motion.getStartTime(), result.getStartTime());
		assertEquals("End time is not correct not correct", motion.getEndTime(), result.getEndTime());
	}

}

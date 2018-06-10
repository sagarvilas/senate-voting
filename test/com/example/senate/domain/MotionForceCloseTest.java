package com.example.senate.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.senate.enums.MotionStatus;
import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyExistsException;

public class MotionForceCloseTest {
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

	@Test
	public void forceCloseTest() throws Exception {
		senate.openVoting(motion);
		for (int i = 0; i < 50; i++) {
			senate.getMotion(motion).vote(senate.getSenator(i), Vote.YES);
		}
		for (int i = 50; i < 100; i++) {
			senate.getMotion(motion).vote(senate.getSenator(i), Vote.NO);
		}
		Thread.sleep(61000);
		VoteResult result = senate.getMotion(motion).closeForVoting();
		assertEquals("Motion is not tied, cannot be force closed", MotionStatus.TIED, result.getResult());
		senate.toggleVPAvailability();
		assertFalse("VP is available, cannot be force closed", senate.getVicePrecident().isAvailable());
		result = senate.forceClose(motion);
		assertEquals("Status was not changed to FAILED state", MotionStatus.FAILED, motion.getStatus());
		assertEquals("Status was not changed to FAILED state", MotionStatus.FAILED, result.getResult());
		assertEquals("Number of votes for are not correct", new Integer(50), result.getVotesFor());
		assertEquals("Number of votes against are not correct", new Integer(50), result.getVotesAgainst());
		assertEquals("Start time is not correct not correct", motion.getStartTime(), result.getStartTime());
		assertEquals("End time is not correct not correct", motion.getEndTime(), result.getEndTime());
	}
}

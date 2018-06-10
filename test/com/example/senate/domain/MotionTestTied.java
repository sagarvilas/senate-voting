package com.example.senate.domain;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.senate.enums.MotionStatus;
import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyExistsException;

public class MotionTestTied {

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
	public void tieBreakerVPavailableTest() throws Exception {
		senate.openVoting(motion);
		for(int i=0;i<50;i++) {
			senate.getMotion(motion).vote(senate.getSenator(i), Vote.YES);			
		}
		for(int i=50;i<100;i++) {
			senate.getMotion(motion).vote(senate.getSenator(i), Vote.NO);			
		}
		Thread.sleep(61000);
		VoteResult result = senate.getMotion(motion).closeForVoting();
		assertEquals("Motion is not TIED",MotionStatus.TIED, result.getResult());
		assertTrue("VP is not available",senate.getVicePrecident().isAvailable());
		result = senate.resolveTie(motion, Vote.YES);
		assertEquals("Motion is not PASSED",MotionStatus.PASSED, motion.getStatus());
		assertEquals("Motion is not PASSED",MotionStatus.PASSED, result.getResult());
		assertEquals("Number of votes for are not correct",new Integer(51), result.getVotesFor());
		assertEquals("Number of votes against are not correct",new Integer(50), result.getVotesAgainst());
		assertEquals("Start time is not correct not correct",motion.getStartTime(), result.getStartTime());
		assertEquals("End time is not correct not correct",motion.getEndTime(), result.getEndTime());
	}
}

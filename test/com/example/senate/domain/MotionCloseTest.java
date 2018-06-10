package com.example.senate.domain;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyExistsException;
import com.example.senate.exception.CannotCloseException;
import com.example.senate.exception.NumberOfVotesExcededException;

public class MotionCloseTest {

	static Senate senate = new Senate();
	static Motion motion = new Motion(10, "test motion");

	@BeforeClass
	public static void setupSenate() throws AlreadyExistsException {
		senate.addMotion(motion);
		for (int i = 0; i <= 101; i++) {
			senate.addSenator(new Senator(i + ""));
		}
	}
	
	@Test(expected = NumberOfVotesExcededException.class)
	public void maxNumberExcededTest() throws Exception {
		senate.openVoting(motion);
		for (int i = 0; i <= 101; i++) {
			senate.getMotion(motion).vote(senate.getSenator(i), Vote.YES);
		}
	}

	@Test(expected = CannotCloseException.class)
	public void cannotBeclosedTest() throws Exception {
		senate.openVoting(motion);
		motion.closeForVoting();
	}
}

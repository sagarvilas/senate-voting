package com.example.senate.domain;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import com.example.senate.enums.MotionStatus;
import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyVotedException;
import com.example.senate.exception.CannotCloseException;
import com.example.senate.exception.NumberOfVotesExcededException;
import com.example.senate.exception.VotingNotOpenException;

public class Motion {
	private static final int MAX_ALLOWED_VOTES = 101;
	private static final int  CLOSE_DELAY_SECONDS = 60;
	private Integer id;
	private String name;
	private MotionStatus status;
	private int totalVotes = 0;
	private LocalTime startTime;
	private LocalTime endTime;
	private List<Voter> votedFor = new LinkedList();// list of senators who voted YES
	private List<Voter> votedAgainst = new LinkedList();// list of senators who voted NO

	// Initialize motion with id, name ans starting state as CLOSE
	public Motion(Integer i, String name) {
		this.setId(i);
		this.setName(name);
		status = MotionStatus.CLOSE;
	}

	/**
	 * Set status of motion as open
	 * set start time
	 */
	public void openForVoting() {
		this.status = MotionStatus.OPEN;
		startTime = LocalTime.now();
	}

	/**
	 * vote for or against a motion if voting is open and not already voted
	 * 
	 * @param senator
	 * @param vote
	 * @throws AlreadyVotedException
	 * @throws VotingNotOpenException
	 * @throws NumberOfVotesExcededException 
	 */
	public void vote(Voter senator, Vote vote) throws AlreadyVotedException, VotingNotOpenException, NumberOfVotesExcededException {
		if (!isOpen())
			throw new VotingNotOpenException("Motion is not open for voting");
		if (alreadyVoted(senator))
			throw new AlreadyVotedException("You have alredy voted");
		++totalVotes;
		if(totalVotes > MAX_ALLOWED_VOTES)
			throw new NumberOfVotesExcededException("Only "+MAX_ALLOWED_VOTES+" votes allowed on a motion");
		if (vote.equals(Vote.YES))
			votedFor.add(senator);
		else
			votedAgainst.add(senator);
	}

	private boolean alreadyVoted(Voter senator) {
		return votedFor.contains(senator) || votedAgainst.contains(senator);
	}

	public boolean isOpen() {
		return !status.equals(MotionStatus.CLOSE);
	}

	/**
	 * Close voting on motion after set delay time calculate result and set status
	 * accordingly
	 * 
	 * @return VoteResult
	 * @throws CannotCloseException
	 */
	public VoteResult closeForVoting() throws CannotCloseException {
		if (!canBeClosed())
			throw new CannotCloseException("Cannot close before " + CLOSE_DELAY_SECONDS + " seconds");
		VoteResult result = computeResult();

		status = result.getResult();
		return result;

	}

	/**
	 * Force close a motion if result is tied and vice president is not available
	 * for voting
	 * 
	 * @return
	 * @throws CannotCloseException
	 */
	public VoteResult forceClose() throws CannotCloseException {
		if (!canBeClosed())
			throw new CannotCloseException("Cannot close before " + CLOSE_DELAY_SECONDS + " seconds");
		VoteResult result = computeResult();
		endTime = LocalTime.now();
		result.setEndTime(endTime);
		status = MotionStatus.FAILED;
		result.setResult(status);
		return result;

	}

	public VoteResult getCurrentStatus() {
		VoteResult result = computeResult();
		result.setResult(MotionStatus.IN_PROGRESS);
		return result;
	}

	private boolean canBeClosed() {
		return ChronoUnit.SECONDS.between(startTime, LocalTime.now()) > CLOSE_DELAY_SECONDS;
	}

	private VoteResult computeResult() {
		VoteResult result = new VoteResult();
		if (votedFor.size() == votedAgainst.size())
			result.setResult(MotionStatus.TIED);
		else if (votedFor.size() > votedAgainst.size()) {
			result.setResult(MotionStatus.PASSED);
		} else {
			result.setResult(MotionStatus.FAILED);
		}
		result.setStartTime(startTime);
		result.setVotesFor(votedFor.size());
		result.setVotesAgainst(votedAgainst.size());
		return result;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public MotionStatus getStatus() {
		return status;
	}

	public void setStatus(MotionStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

}

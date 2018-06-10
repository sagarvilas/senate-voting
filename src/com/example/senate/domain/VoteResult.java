package com.example.senate.domain;

import java.time.LocalTime;

import com.example.senate.enums.MotionStatus;

public class VoteResult {
	MotionStatus result;
	Integer votesFor;
	Integer votesAgainst;
	private LocalTime startTime;
	private LocalTime endTime;

	public MotionStatus getResult() {
		return result;
	}

	public void setResult(MotionStatus result) {
		this.result = result;
	}

	public Integer getVotesFor() {
		return votesFor;
	}

	public void setVotesFor(Integer votesFor) {
		this.votesFor = votesFor;
	}

	public Integer getVotesAgainst() {
		return votesAgainst;
	}

	public void setVotesAgainst(Integer votesAgainst) {
		this.votesAgainst = votesAgainst;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

}

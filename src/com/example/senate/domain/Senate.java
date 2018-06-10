package com.example.senate.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.senate.enums.MotionStatus;
import com.example.senate.enums.Vote;
import com.example.senate.exception.AlreadyExistsException;
import com.example.senate.exception.AlreadyVotedException;
import com.example.senate.exception.CannotCloseException;
import com.example.senate.exception.NotTiedException;
import com.example.senate.exception.NumberOfVotesExcededException;
import com.example.senate.exception.VPNotAvailableException;
import com.example.senate.exception.VotingNotOpenException;

public class Senate {
	private Map<Integer, Motion> motions = new HashMap();
	private List<Voter> senators = new ArrayList();
	private VicePresident vp;

	/**
	 * Add a motion to senate for voting
	 * 
	 * @param motion
	 * @throws AlreadyExistsException
	 */
	public void addMotion(Motion motion) throws AlreadyExistsException {
		if (motions.containsKey(motion.getId()))
			throw new AlreadyExistsException("Motion already added");
		motions.put(motion.getId(), motion);
	}

	/**
	 * make given motion open for voting
	 * 
	 * @param motion
	 */
	public void openVoting(Motion motion) {
		motions.get(motion.getId()).openForVoting();
	}

	public Motion getMotion(Motion motion) {
		if (!motions.containsKey(motion.getId()))
			return null;
		return motions.get(motion.getId());
	}

	/**
	 * Try to resolve a tie with vice presidents vote if vice president is not
	 * available tie cannot be resolved
	 * 
	 * @param motion
	 * @param vpVote
	 * @return
	 * @throws AlreadyVotedException
	 * @throws CannotCloseException
	 * @throws VotingNotOpenException
	 * @throws VPNotAvailableException
	 * @throws NotTiedException
	 * @throws ` 
	 */
	public VoteResult resolveTie(Motion motion, Vote vpVote) throws AlreadyVotedException, CannotCloseException,
			VotingNotOpenException, VPNotAvailableException, NotTiedException, NumberOfVotesExcededException {
		Motion tiedMotion = motions.get(motion.getId());
		if (tiedMotion.getStatus() == MotionStatus.TIED) {
			if (!vp.isAvailable()) {
				throw new VPNotAvailableException("Cannot be resolved Vice precident is not available");
			}
			motion.vote(vp, vpVote);
			return motion.closeForVoting();
		} else
			throw new NotTiedException("Motion is not tied");
	}

	/**
	 * If motion is in tie state and VP is not available then a motion can be force
	 * closed and motion will fail
	 * 
	 * @param motion
	 * @return
	 * @throws CannotCloseException
	 */
	public VoteResult forceClose(Motion motion) throws CannotCloseException {
		Motion tiedMotion = motions.get(motion.getId());
		if (!vp.isAvailable()) {
			return tiedMotion.forceClose();
		} else
			throw new CannotCloseException("Vice precident is available, cannot force close");

	}

	public VoteResult getCurrentStatusOfMotion(Motion motion) {
		if (motions.get(motion.getId()) != null)
			return motions.get(motion.getId()).getCurrentStatus();
		return null;
	}

	public void addSenator(Senator senator) {
		senators.add(senator);
	}

	public Senator getSenator(int i) {
		return (Senator) senators.get(i);
	}

	public void addVicePrecident(String name) {
		this.vp = new VicePresident(name);
		vp.setAvailable(true);
	}

	public VicePresident getVicePrecident() {
		return vp;
	}

	public void toggleVPAvailability() {
		vp.setAvailable(!vp.isAvailable());
	}

}

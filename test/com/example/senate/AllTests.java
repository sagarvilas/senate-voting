package com.example.senate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.senate.domain.MotionNormalFlowTest;
import com.example.senate.domain.MotionCloseTest;
import com.example.senate.domain.MotionForceCloseTest;
import com.example.senate.domain.MotionTestTied;

@RunWith(Suite.class)
@SuiteClasses({ MotionNormalFlowTest.class, MotionTestTied.class, MotionCloseTest.class, MotionForceCloseTest.class })
public class AllTests {

}
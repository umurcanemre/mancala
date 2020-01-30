package com.umurcanemre.mancala;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MancalaApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void mainRuns() {
		new MancalaApplication().main(Arrays.array(""));
	}

}

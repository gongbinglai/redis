package com.jd.redission;


import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;


public class RedissionTest {

	
	@Test
	public void sayHello(){
		System.out.println("sayHello");
	}
	
    
    
    @Before
    public void setup() {
    }
    
    
    @Test
	public void getLock() throws Exception {
    	RedissonClient redisson = Redisson.create();

        RLock lock = redisson.getLock("haogrgr");
        lock.lock();
        try {
            System.out.println("hagogrgr");
        }
        finally {
            lock.unlock();
        }

        redisson.shutdown();
    }
    
    
}

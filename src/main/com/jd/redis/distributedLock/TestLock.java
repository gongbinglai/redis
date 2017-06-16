package com.jd.redis.distributedLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;

/**
 * Created by hspcadmin on 2017/3/21.
 */
public class TestLock {
	
    public static void main(String str[]){
    	
    	ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
        	
        	executor.execute(new RedisLockService("t_"+i));
        	
        }
        executor.shutdown();
    }
}


class RedisLockService implements Runnable{

	private String name;
	
	public RedisLockService(String name){
		this.name = name;
	}
	
	public void run() {
		
		Jedis jedis = new Jedis("127.0.0.1", 6379);
        //锁等待时间(等待获取锁)   锁超时时间(获取锁后的持有时间)
		
		
		//在20s内未获取锁的话，则认为获取锁超过   锁超时时间为10s
        RedisLock redisLock = new RedisLock(jedis,"distributeLock", 1000*50, 1000*20);
        
        boolean result = false;
        
        try {
			result = redisLock.lock();
			
			if(result){
				System.out.println("线程 "+name+" 获取了锁，进行文件修改");
		        Thread.sleep(3000);
			}else{
				System.out.println("线程 "+name+" 获取锁失败");
			}
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
		}finally{
			
			if(result){
				System.out.println("线程 "+name+" 释放了锁");
				redisLock.unlock();
			}else{
				System.out.println("线程 "+name+" 未获取锁，不进行锁的释放");
			}
			
		}
        
	}
	
}









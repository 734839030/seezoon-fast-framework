package com.seezoon.admin.modules.demo.task;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.seezoon.admin.common.job.BaseJob;

/**
 * 分片作业demo
 * @author hdf
 * 2018年5月27日
 */
@Component
public class TaskShardingDemo extends BaseJob implements SimpleJob{

	@Override
	public void execute(ShardingContext shardingContext) {
		switch (shardingContext.getShardingItem()) {
        case 0: 
        	logger.debug("TaskSharedingDemo running .....shardingItem =" + shardingContext.getShardingItem() + " ShardingParameter="+shardingContext.getShardingParameter());
            break;
        case 1: 
        	logger.debug("TaskSharedingDemo running .....shardingItem =" + shardingContext.getShardingItem() + " ShardingParameter="+shardingContext.getShardingParameter());
            break;
		}
	}

}

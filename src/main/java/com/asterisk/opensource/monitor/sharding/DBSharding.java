package com.asterisk.opensource.monitor.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

public class DBSharding implements SingleKeyDatabaseShardingAlgorithm<Long> {

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Long> shardValue) {
        for (String each : collection) {
            if (each.endsWith(String.valueOf((shardValue.getValue() / 100) % 2))) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Collection<Long> values = shardingValue.getValues();
        for (Long value : values) {
            for (String dataSourceName : collection) {
                if (dataSourceName.endsWith(String.valueOf((value / 100) % 2))) {
                    result.add(dataSourceName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : collection) {
                if (each.endsWith(String.valueOf((i / 100) % 2))) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}

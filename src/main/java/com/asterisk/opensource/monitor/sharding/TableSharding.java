package com.asterisk.opensource.monitor.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;


public class TableSharding implements SingleKeyTableShardingAlgorithm<Long> {

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Long> shardValue) {
        for (String each : collection) {
            if (each.endsWith(String.valueOf((shardValue.getValue() / 10) % 2))) {
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
            result.addAll(collection.stream().filter(tableNames -> tableNames.endsWith(String.valueOf((value / 10) % 2))).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : collection) {
                if (each.endsWith(String.valueOf((i / 10) % 2))) {
                    result.add(each);
                }
            }
        }
        return result;
    }

}

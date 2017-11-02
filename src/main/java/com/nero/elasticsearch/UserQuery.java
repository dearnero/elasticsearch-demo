package com.nero.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询
 * <p>
 * date : 2017/10/31
 * time : 16:22
 * </p>
 *
 * @author Nero
 */
@Component
public class UserQuery {

    /**
     * 索引名称
     */
    private static final String ES_INDEX = "my_test";

    /**
     * type
     */
    private static final String ES_TYPE = "user";

    /**
     * 起始页
     */
    private static final Integer FROM_PAGE = 0;

    /**
     * 页尺寸
     */
    private static final Integer PAGE_SIZE = 2;

    /**
     * 根据age 查询
     *
     * @param age id
     * @return 用户
     */
    public User findByAge(Integer age) {

        TransportClient client = ESClientSingleton.get();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(ES_INDEX).setTypes(ES_TYPE);

        QueryBuilder queryBuilder = QueryBuilders.termQuery("age", age);
        searchRequestBuilder.setQuery(queryBuilder);

        // 开始执行查询
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length == 0) {
            return null;
        }
        SearchHit hit = searchHits.getHits()[0];
        String json = hit.getSourceAsString();

        return JSON.parseObject(json, User.class);
    }

    /**
     * 根据年龄比较以及hid区间查询
     *
     * @param minAge  年龄
     * @param hidList hid区间
     * @return 用户列表
     */
    public List<User> findByAgeAndHid(Integer minAge, List<Long> hidList) {

        TransportClient client = ESClientSingleton.get();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(ES_INDEX).setTypes(ES_TYPE).setFrom(FROM_PAGE).setSize(PAGE_SIZE);

        // 组装查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 年龄
        RangeQueryBuilder ageQueryBuilder = QueryBuilders.rangeQuery("age").gte(minAge);
        boolQueryBuilder.must(ageQueryBuilder);
        // hid范围
        QueryBuilder statusQueryBuilder = QueryBuilders.termsQuery("hid", hidList);
        boolQueryBuilder.must(statusQueryBuilder);

        searchRequestBuilder.setQuery(boolQueryBuilder);
        // 开始执行查询
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        // 解析结果集
        List<User> userList = new ArrayList<User>();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits.getHits()) {
            String json = hit.getSourceAsString();
            userList.add(JSON.parseObject(json, User.class));
        }

        return userList;
    }

    /**
     * 关键字搜索
     *
     * @param keyword 关键字
     * @return 用户列表
     */
    public List<User> findByDesc(String keyword) {


        TransportClient client = ESClientSingleton.get();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(ES_INDEX).setTypes(ES_TYPE).setFrom(FROM_PAGE).setSize(PAGE_SIZE);

        // desc字段关键字查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("desc", keyword);
        matchQueryBuilder.operator(Operator.AND);
        searchRequestBuilder.setQuery(matchQueryBuilder);
        // 开始执行查询
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        // 解析结果集
        List<User> userList = new ArrayList<User>();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits.getHits()) {
            String json = hit.getSourceAsString();
            userList.add(JSON.parseObject(json, User.class));
        }

        return userList;
    }
}

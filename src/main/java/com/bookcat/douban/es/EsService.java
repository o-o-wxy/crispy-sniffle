package com.bookcat.douban.es;

import com.bookcat.douban.formbean.Book;
import com.bookcat.douban.repositories.ESRepository;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class EsService implements ESRepository {
    public static Book getBook(Map<String, Object> sourceAsMap){
        Book book = new Book();
        book.setName((String) sourceAsMap.get("name"));
        book.setSubName((String) sourceAsMap.get("sub_name"));
        book.setCover((String)sourceAsMap.get("cover"));
        book.setSeries((String)sourceAsMap.get("series"));
        book.setBinding((String)sourceAsMap.get("binding"));
        book.setTags((String)sourceAsMap.get("tags"));
        book.setDoubanVotes((int)sourceAsMap.get("douban_votes"));
        book.setAltName((String) sourceAsMap.get("alt_name"));
        book.setPublisher((String) sourceAsMap.get("publisher"));
        book.setAuthors((String)sourceAsMap.get("authors"));
        book.setAuthorIntro((String)sourceAsMap.get("author_intro"));
        book.setTranslators((String)sourceAsMap.get("translators"));
        if(sourceAsMap.get("douban_score") instanceof Integer)
            book.setDoubanScore((int)sourceAsMap.get("douban_score"));
        else
            book.setDoubanScore((double)sourceAsMap.get("douban_score"));
        book.setPublishDate((String)sourceAsMap.get("publish_date"));
        book.setPages((String)sourceAsMap.get("pages"));
        book.setPrice((String)sourceAsMap.get("price"));
        book.setIsbn((String)sourceAsMap.get("isbn"));
        book.setDoubanId((int)sourceAsMap.get("douban_id"));
        book.setCount((Integer) sourceAsMap.get("count"));
        book.setSummary((String)sourceAsMap.get("summary"));
        book.setId((int)sourceAsMap.get("id"));
        return book;
    }
    @Override
    public List<Book> findBykey(String searchString) {
        List<Book> bookList = new ArrayList<>();

        /**
         * 创建客户端
         * 参考网址：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.7/java-rest-high-search.html
         * java 文档：https://artifacts.elastic.co/javadoc/org/elasticsearch/elasticsearch/7.7.1/index.html
         */
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("39.97.176.82", 9200, "http")));

        //创建请求
        SearchRequest searchRequest = new SearchRequest();
        //要查询的index
        //也可写成earchRequest searchRequest = new SearchRequest("posts");
        searchRequest.indices("books");

        //设置搜索行为
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //从id为0开始查
        //searchSourceBuilder.from(0);
        //返回大小 默认10
        searchSourceBuilder.size(20);
        //这次查询行为最多花费时间
        //searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //还提供过滤结果、高亮结果的选项

        /**
         * 设置query
         * 不同的query:https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.7/java-rest-high-query-builders.html
         * 类文档 https://artifacts.elastic.co/javadoc/org/elasticsearch/elasticsearch/7.7.1/org/elasticsearch/index/query/MultiMatchQueryBuilder.html
         */
        MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(searchString);
        queryBuilder.field("name",5);
        queryBuilder.field("sub_name",4);
        queryBuilder.field("alt_name",4);
        queryBuilder.field("summary",2);
        queryBuilder.field("authors",5);
        queryBuilder.field("author_intro",1);
        queryBuilder.field("translators",3);
        queryBuilder.field("series",1);
        queryBuilder.field("publisher",1);
        queryBuilder.field("tags",2);

        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);

//        //设置滚动有效时间
//        //Set the scroll interval
//        searchRequest.scroll(TimeValue.timeValueMinutes(5L));//1L是1 L是长整数long

        //同步执行查询请求 返回response
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            //要访问返回的文档，我们首先需要获取响应中包含的SearchHits：
            SearchHits hits = searchResponse.getHits();

            //The SearchHits provides global information about all hits, like total number of hits or the maximum score
            //searchHits 提供关于Hits（命中）的全局信息，像是数量、最大的分数
            TotalHits totalHits = hits.getTotalHits();
            // the total number of hits, must be interpreted in the context of totalHits.relation
            long numHits = totalHits.value;
            // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
            float maxScore = hits.getMaxScore();
            //String scrollId = searchResponse.getScrollId();

            System.out.println("return "+numHits+" hits, the max score is "+maxScore);

            //嵌套在SearchHits中的是可以迭代的单个搜索结果
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                //SearchHit提供对基本信息的访问，例如索引，文档ID和每个搜索命中的分数：
                String id = hit.getId();
                float score = hit.getScore();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //System.out.println("the book name is "+sourceAsMap.get("name")+" score is "+score);
                Book book = getBook(sourceAsMap);
                bookList.add(book);
            }
            //第二页的获取
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public List<Book> moreLike(int id) {
        List<Book> bookList = new ArrayList<>();

        /**
         * 创建客户端
         * 参考网址：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.7/java-rest-high-search.html
         * java 文档：https://artifacts.elastic.co/javadoc/org/elasticsearch/elasticsearch/7.7.1/index.html
         */
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("39.97.176.82", 9200, "http")));

        //创建请求
        SearchRequest searchRequest = new SearchRequest();
        //要查询的index
        //也可写成earchRequest searchRequest = new SearchRequest("posts");
        searchRequest.indices("books");

        //设置搜索行为
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //从id为0开始查
        //searchSourceBuilder.from(0);
        //返回大小 默认10
        searchSourceBuilder.size(10);
        //这次查询行为最多花费时间
        //searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //还提供过滤结果、高亮结果的选项

        /**
         * 设置query MoreLikeThisQueryBuilder
         * java文档：https://artifacts.elastic.co/javadoc/org/elasticsearch/elasticsearch/7.7.1/org/elasticsearch/index/query/MoreLikeThisQueryBuilder.html
         * morelikethis查询说明：https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-mlt-query.html
         */
        String[] fields = {"summary"};
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = new MoreLikeThisQueryBuilder(
                fields,null,new MoreLikeThisQueryBuilder.Item[]{
                        new MoreLikeThisQueryBuilder.Item("books", String.valueOf(id))}).maxQueryTerms(12).minTermFreq(1);
        searchSourceBuilder.query(moreLikeThisQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        //同步执行查询请求 返回response
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            //要访问返回的文档，我们首先需要获取响应中包含的SearchHits：
            SearchHits hits = searchResponse.getHits();

            //The SearchHits provides global information about all hits, like total number of hits or the maximum score
            //searchHits 提供关于Hits（命中）的全局信息，像是数量、最大的分数
            TotalHits totalHits = hits.getTotalHits();
            // the total number of hits, must be interpreted in the context of totalHits.relation
            long numHits = totalHits.value;
            // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
            float maxScore = hits.getMaxScore();
            //String scrollId = searchResponse.getScrollId();

            System.out.println("return "+numHits+" hits, the max score is "+maxScore);

            //嵌套在SearchHits中的是可以迭代的单个搜索结果
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                //SearchHit提供对基本信息的访问，例如索引，文档ID和每个搜索命中的分数：
//                String id = hit.getId();
                float score = hit.getScore();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //System.out.println("the book name is "+sourceAsMap.get("name")+" score is "+score);
                Book book = getBook(sourceAsMap);
                bookList.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}

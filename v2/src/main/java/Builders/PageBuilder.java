package main.java.Builders;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.IOUtils;

import javax.management.AttributeValueExp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBuilder {

    AWSStaticCredentialsProvider provider;
    private static String BUCKET = "BUCKET NAME";
    private static String TABLE = "TABLE NAME";

    public PageBuilder() {
        String accessKey = "ACCESS KEY";
        String secretKey = "SECRET KEY";
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        provider = new AWSStaticCredentialsProvider(credentials);
    }

    public String buildImagePage(String url) {
        String page = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            page = IOUtils.toString(classLoader.getResourceAsStream("Assets/BuildImage.html"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (url.isEmpty() || page.isEmpty()) { return String.format(page, ""); }

        String image = "<p>Your image has built successfully!</p>" +
                "<p><img src=\"" + url + "\"></p>";

        return String.format(page, image);
    }

    public String buildEmail(String quarter, int week) {
        Map<String, String> images = retrieveImages(quarter, week);

        String body = "";

        for (Map.Entry<String, String> image : images.entrySet()) {

            body += "<a href=\"" + image.getValue().toString() + "\">\n"
                    + "<img src=\"" + image.getKey().toString()
                    + "\" style=\"width:100%;height:auto;padding-top:15px\">\n"
                    + "</a>\n";
        }

        String page = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            page = IOUtils.toString(classLoader.getResourceAsStream("Assets/EmailTemplate.html"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format(page, body);
    }

    public Map<String, String> retrieveImages(String quarter, int week) {
        AmazonS3ClientBuilder s3builder= AmazonS3ClientBuilder.standard().withCredentials(provider).withRegion("us-west-2");
        AmazonS3 s3 = s3builder.build();

        AmazonDynamoDBClientBuilder dbbuilder = AmazonDynamoDBClientBuilder.standard().withCredentials(provider)
                .withRegion("us-west-2");
        AmazonDynamoDB dynamo = dbbuilder.build();

        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(BUCKET)
                .withPrefix(quarter + "/Week " + week + "/img_");

        ObjectListing listing = s3.listObjects(request);
        List<S3ObjectSummary> objects = listing.getObjectSummaries();

        ArrayList<String> keys = new ArrayList<String>();

        for (S3ObjectSummary object : objects) {
            keys.add(object.getKey());
        }

        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();

        for (String key : keys) {
            System.out.println(key);
            HashMap<String, AttributeValue> value = new HashMap<String, AttributeValue>();
            value.put("Key", new AttributeValue(key));
            GetItemResult result = dynamo.getItem(TABLE, value);
            links.add(result.getItem().get("link").getS());
            urls.add(s3.getUrl(BUCKET, key).toString());
        }

        HashMap<String, String> result = new HashMap<String, String>();

        for (int k = 0; k < urls.size(); k++) {
            result.put(urls.get(k), links.get(k));
        }

        return result;
    }
}

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PageBuilder {
    public static void main(String[] args) {
        int week = 4;

        String accessKey = "AKIAIB6UVANUR6ASILEQ";
        String secretKey = "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";
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        AmazonS3ClientBuilder s3builder= AmazonS3ClientBuilder.standard().withCredentials(provider).withRegion("us-west-2");
        AmazonS3 s3 = s3builder.build();

        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName("emailgenbucket")
                .withPrefix("Week " + week + "/img_");

        ObjectListing listing = s3.listObjects(request);
        List<S3ObjectSummary> objects = listing.getObjectSummaries();

        ArrayList<String> keys = new ArrayList<>();

        for (S3ObjectSummary object : objects) {
            keys.add(object.getKey());
        }

        ArrayList<String> urls = new ArrayList<>();

        for (String key : keys) {
            urls.add(s3.getUrl("emailgenbucket", key).toString());
        }

        for (String url : urls) {
            System.out.println(url);
        }

        String template = "<a href=\"link here\">\n<img src=\"";
        String template2 = "\" style=\"width:100%;height:auto;padding-top:15px\">\n</a>";

        StringBuilder body = new StringBuilder();

        for (String url : urls) {
            body.append(template)
                    .append(url)
                    .append(template2);
        }

        System.out.println(body.toString());
        try {
            String page = readFile("email_template.html", StandardCharsets.UTF_8);
            String page2 = readFile("email_template2.html", StandardCharsets.UTF_8);
            PrintWriter result = new PrintWriter("test.html");
            result.println(page + body.toString() + page2);
            result.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}

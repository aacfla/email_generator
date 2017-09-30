import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.ArrayList;

public class HTML2IMG {
    public static void main(String[] args) {

        ArrayList<String> info = new ArrayList<String>();
        info.add("Date: Wednesday, 5/10");
        info.add("Time: 7:30 - 9:30 pm");
        info.add("Location: @CS24");
        String test = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.";
        ImgBuilder builder = new ImgBuilder("TITLE", "SUBTITLE", info, test);
        File file = builder.build();

        String accessKey = "accessKey";
        String secretKey = "secretKey";
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        AmazonS3ClientBuilder s3builder= AmazonS3ClientBuilder.standard().withCredentials(provider).withRegion("us-west-2");
        AmazonS3 s3 = s3builder.build();

        PutObjectRequest request = new PutObjectRequest("emailgenbucket", "Week 1/TEST", file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

    }
}

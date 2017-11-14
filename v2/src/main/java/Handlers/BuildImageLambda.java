package main.java.Handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import main.java.Builders.ImageBuilder;
import main.java.Builders.PageBuilder;
import main.java.Models.BuildImageRequest;

public class BuildImageLambda implements RequestHandler<BuildImageRequest, String> {
    @Override
    public String handleRequest(BuildImageRequest request, Context context) {
        PageBuilder builder = new PageBuilder();
        System.out.println(request.toString());
        System.out.println(request.getTitleSize());
        if (request.getTitle().isEmpty()) {
            return builder.buildImagePage("");
        } else {
            if (request.getLink().isEmpty()) request.setLink("#");
            String url = ImageBuilder.build(request);
            return builder.buildImagePage(url);
        }
    }
}

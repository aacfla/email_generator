package main.java.Handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import main.java.Builders.PageBuilder;
import main.java.Models.BuildPageRequest;

public class BuildPageLambda implements RequestHandler<BuildPageRequest, String> {
    @Override
    public String handleRequest(BuildPageRequest request, Context context) {
        PageBuilder builder = new PageBuilder();
        return builder.buildEmail(request.getQuarter(), request.getWeek());
    }
}

package main.java.Handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import main.java.Builders.PageBuilder;

public class BuildPageLambda implements RequestHandler<Integer, String> {
    @Override
    public String handleRequest(Integer week, Context context) {
        PageBuilder builder = new PageBuilder();
        return builder.buildEmail(week);
    }
}

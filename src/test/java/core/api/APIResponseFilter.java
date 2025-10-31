package core.api;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class APIResponseFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        Response response = ctx.next(requestSpec, responseSpec);

        String requestStr = printRequest(requestSpec);
        String responseStr = printResponse(response);

        ExtentTestManager.getTest()
                .log(Status.INFO, MarkupHelper.createCodeBlock(requestStr + "\n" + responseStr));

        return response;
    }

    private String printRequest(FilterableRequestSpecification requestSpec) {
        return RequestPrinter.print(
                requestSpec,
                requestSpec.getMethod(),
                requestSpec.getURI(),
                LogDetail.ALL,
                Collections.emptySet(),
                new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8),
                true
        );
    }

    private String printResponse(Response response) {
        return ResponsePrinter.print(
                response,
                response,
                new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8),
                LogDetail.ALL,
                true,
                Collections.emptySet()
        );
    }
}
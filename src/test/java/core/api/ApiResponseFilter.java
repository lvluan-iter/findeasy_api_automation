package core.api;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import core.report.ExtentTestManager;
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

public class ApiResponseFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        Response response = ctx.next(requestSpec, responseSpec);

        ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
        PrintStream requestPrintStream = new PrintStream(requestStream, true, StandardCharsets.UTF_8);
        RequestPrinter.print(
                requestSpec,
                requestSpec.getMethod(),
                requestSpec.getURI(),
                LogDetail.ALL,
                Collections.emptySet(),
                requestPrintStream,
                true
        );
        String requestStr = requestStream.toString(StandardCharsets.UTF_8);

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        PrintStream responsePrintStream = new PrintStream(responseStream, true, StandardCharsets.UTF_8);
        ResponsePrinter.print(
                response,
                response,
                responsePrintStream,
                LogDetail.ALL,
                true,
                Collections.emptySet()
        );
        String responseStr = responseStream.toString(StandardCharsets.UTF_8);

        ExtentTestManager.getTest()
                .log(Status.INFO,
                        MarkupHelper.createCodeBlock(
                                "📤 REQUEST:\n" + requestStr + "\n\n📥 RESPONSE:\n" + responseStr
                        )
                );

        return response;
    }
}
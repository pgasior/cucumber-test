package org.example.rest;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.Map;

import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.filter.log.LogDetail.URI;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class LoggingFilter implements Filter {

    private final Listener listener;

    public LoggingFilter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        RestQuery restQuery = recordRestSpecificationData(RestMethod.restMethodCalled(filterableRequestSpecification.getMethod()).get(), filterableRequestSpecification);

        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

        RestResponseRecordingHelper restResponseRecordingHelper =
                new RestResponseRecordingHelper(true, Collections.emptySet(), LogDetail.HEADERS, LogDetail.COOKIES);
        final Map<LogDetail, String> values = restResponseRecordingHelper.print(response);

        String renderedBody = new Prettifier().getPrettifiedBodyIfPossible(
                (ResponseOptions) response.getBody(), response.getBody());

        restQuery = restQuery.withResponse(renderedBody.isEmpty() ? response.asString() : renderedBody);

        restQuery = restQuery.withStatusCode(response.getStatusCode())
                .withResponseHeaders(firstNonNull(values.get(LogDetail.HEADERS), ""))
                .withResponseCookies(firstNonNull(values.get(LogDetail.COOKIES), ""));
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(restQuery);
        listener.onResponse(restQuery);
        return response;
    }

    public RestQuery recordRestSpecificationData(final RestMethod method, final FilterableRequestSpecification spec) {

        RestRequestRecordingHelper restRequestRecordingHelper =
                new RestRequestRecordingHelper(true, Collections.emptySet(),
                        HEADERS, COOKIES, BODY, PARAMS, METHOD, URI);
        Map<LogDetail, String> requestRecord = restRequestRecordingHelper.print(spec);

        final RestQuery query = RestQuery.
                withMethod(method).andPath(ObjectUtils.firstNonNull(requestRecord.get(LogDetail.URI).replaceFirst("^Request URI:\t", ""), "")).
                withContentType(String.valueOf(
                        ContentType.fromContentType(spec.getContentType()))
                ).
                withContent(firstNonNull(requestRecord.get(LogDetail.BODY), "")).
                withRequestCookies(firstNonNull(requestRecord.get(LogDetail.COOKIES), "")).
                withRequestHeaders(firstNonNull(requestRecord.get(LogDetail.HEADERS), ""));
        return query;
    }

    public interface Listener {
        void onResponse(RestQuery query);
    }
}

package org.example.rest;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.UrlDecoder;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RestRequestRecordingHelper {
    private final List<LogDetail> logDetail;
    private final boolean shouldPrettyPrint;
    private Set<String> blackListedHeaders;

    public RestRequestRecordingHelper(final boolean shouldPrettyPrint, final Set<String> blackListedHeaders, final LogDetail... details) {
        this.blackListedHeaders = blackListedHeaders;
        this.logDetail = new LinkedList<>();
        this.logDetail.addAll(Arrays.asList(details));
        this.shouldPrettyPrint = shouldPrettyPrint;
    }

    public Map<LogDetail, String> print(final FilterableRequestSpecification request) {
        Map<LogDetail, String> result = new HashMap();
        if (request != null) {
            String uri = UrlDecoder.urlDecode(request.getURI(), Charset.forName(request.getConfig().getEncoderConfig().defaultQueryParameterCharset()), true);
            try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                 PrintStream recordingStream = new PrintStream(output, true, StandardCharsets.UTF_8.toString())) {
                for (final LogDetail detail : logDetail) {
                    try {
                        RequestPrinter.print(request,
                                request.getMethod(),
                                uri, detail, Collections.emptySet(),
                                recordingStream, true);
                    } catch (NullPointerException e) {
                        //can be thrown if some field like cookies or headers are empty
                    }
                    recordingStream.flush();
                    String recorded = new String(output.toByteArray(), StandardCharsets.UTF_8);
                    output.reset();
                    recorded = recorded.replaceAll("^(" +
                            "(Proxy:)|(Body:)|(Cookies:)|(Headers:)|(Multiparts:)|(Request path:)" +
                            ")\\s*\\n*", "");
                    recorded = recorded.replaceAll("^(<none>)", "");
                    recorded = recorded.replaceAll("\n$", "");
                    result.put(detail, recorded);

                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Incorrect implementation, should be used correct charset", e);
            } catch (IOException e) {
                throw new RuntimeException("Some exception during recording fields", e);
            }
        }
        return result;
    }
}

package org.example.rest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class RESTTemplater {
    private final Configuration cfg;
    private final Formatter formatter = new Formatter();

    public RESTTemplater() {
        cfg = new Configuration(new Version(2, 3, 30));

        cfg.setClassForTemplateLoading(RESTTemplater.class, "/templates");

        cfg.setDefaultEncoding("UTF-8");
    }

    public String getHTMLOutput(RestQuery query) {
        try {
            Template template = cfg.getTemplate("rest.ftl");
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("formatter", formatter);
            input.put("query", query);
            StringWriter stringWriter = new StringWriter();
            template.process(input, stringWriter);
            System.out.println(stringWriter.toString());
            return stringWriter.toString().replaceAll("&", "&amp;");
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage());
        } catch (TemplateException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return null;
    }
}

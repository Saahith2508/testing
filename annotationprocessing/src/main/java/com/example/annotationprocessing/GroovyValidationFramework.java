package com.example.annotationprocessing;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

import java.io.IOException;

public class GroovyValidationFramework {
    private static final String GROOVY_SCRIPTS_FOLDER = "groovy";

    public Object validate(String scriptName, String methodName, Object... args) {
        try {
            GroovyScriptEngine engine = loadScript();
            Class<GroovyObject> scriptClass = engine.loadScriptByName(scriptName + ".groovy");
            GroovyObject scriptInstance = scriptClass.newInstance();

            return scriptInstance.invokeMethod(methodName, args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private GroovyScriptEngine loadScript() throws IOException {
        String scriptFolderPath = getClass().getClassLoader().getResource(GROOVY_SCRIPTS_FOLDER).getPath();
        GroovyScriptEngine engine = new GroovyScriptEngine(scriptFolderPath);
        return engine;
    }
}

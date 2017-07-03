package com.enviosya.logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LoggerEnviosYa {
    private Logger logger;
    public LoggerEnviosYa(Class name) {
        logger = Logger.getLogger(name.getName());
        BasicConfigurator.configure();
    }
    
    public void error(String message){
        logger.error("Whoops! Simple error: ");
        logger.error(message);
        logger.error("---------------------------");
    }
    
    public void errorWithException(String message,Exception exception){
        logger.error("Whoops! Error: ");
        logger.error(message, exception);
        logger.error("---------------------------");
    }
    
    public void success(String message){
        logger.error("Success! more information: ");
        logger.info(message);
        logger.error("---------------------------");
    }
    
    public void info(String message){
        logger.error("Incoming information: ");
        logger.info(message);
        logger.error("---------------------------");
    }
    
}

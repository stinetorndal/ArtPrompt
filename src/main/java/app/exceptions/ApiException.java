    package app.exceptions;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class ApiException extends RuntimeException {
        private int code;
        private static final Logger logger = LoggerFactory.getLogger(ApiException.class);

        public ApiException(int code, String msg){
            super(msg);
            this.code = code;
            //Nedenstående kan ændres til logger.error, hvis man vil have alt med
            logger.debug("ApiException (code={}): {}", code, msg);
        }
        public int getCode(){
            return code;
        }
    }
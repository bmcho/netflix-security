package com.bmcho.netflix.movie.validator;

import com.bmcho.netflix.enums.SubscriptionType;
import com.bmcho.netflix.exception.ErrorCode;
import com.bmcho.netflix.exception.NetflixException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMovieDownloadValidatorFactory {

    public interface Validator {
        boolean validate(long count);
    }

    public Validator getValidator(String role) {

        if (StringUtils.equalsIgnoreCase(SubscriptionType.BRONZE.getRole(), role)) {
            return new BronzeValidator();
        }
        if (StringUtils.equalsIgnoreCase(SubscriptionType.SILVER.getRole(), role)) {
            return new SilverValidator();
        }
        if (StringUtils.equalsIgnoreCase(SubscriptionType.GOLD.getRole(), role)) {
            return new GoldValidator();
        }

        throw new NetflixException(ErrorCode.ACCESS_DENIED);
    }

    private static class BronzeValidator implements Validator {
        @Override
        public boolean validate(long count) {
            return count < 5;
        }
    }

    private static class SilverValidator implements Validator {
        @Override
        public boolean validate(long count) {
            return count < 10;
        }
    }

    private static class GoldValidator implements Validator {
        @Override
        public boolean validate(long count) {
            return true;
        }
    }

}
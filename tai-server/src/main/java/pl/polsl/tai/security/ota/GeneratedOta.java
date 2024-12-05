package pl.polsl.tai.security.ota;

import pl.polsl.tai.domain.ota.OtaTokenEntity;

public record GeneratedOta(OtaTokenEntity entity, long expiredSeconds) {
}
